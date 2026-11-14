package com.ibm.websphere.samples.daytrader;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Engine-authored (lot V2-4, RFC-042 section 4 A.5): the container-managed
 * concurrency of each admitted EJB singleton. Every business method listed
 * below takes the bean's read or write lock on entry through the proxy and
 * releases it on return; the advisor is outside the transaction advisor and
 * outside the interceptor chain, so the lock covers the commit.
 */
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class EngineSingletonLocks extends AbstractPointcutAdvisor {

    /**
     * Thrown where the EJB container throws {@code IllegalLoopbackException}:
     * a thread that holds only the read lock asks for the write lock.
     */
    public static final class LockUpgradeException extends IllegalStateException {
        private static final long serialVersionUID = 1L;

        public LockUpgradeException(String message) {
            super(message);
        }
    }

    /** One locked business method: bean class, name, erased parameter types, kind. */
    public static final class Row {
        private final Class<?> bean;
        private final String method;
        private final Class<?>[] parameters;
        private final boolean write;

        private Row(Class<?> bean, String method, Class<?>[] parameters, boolean write) {
            this.bean = bean;
            this.method = method;
            this.parameters = parameters;
            this.write = write;
        }
    }

    static Row read(Class<?> bean, String method, Class<?>... parameters) {
        return new Row(bean, method, parameters, false);
    }

    static Row write(Class<?> bean, String method, Class<?>... parameters) {
        return new Row(bean, method, parameters, true);
    }

    /** Sorted by bean class, method name and parameter types. */
    static List<Row> rows() {
        return List.of(
                read(com.ibm.websphere.samples.daytrader.ejb3.MarketSummarySingleton.class, "getMarketSummaryDataBean"),
                write(com.ibm.websphere.samples.daytrader.ejb3.MarketSummarySingleton.class, "setMarketSummaryDataBean", com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean.class));
    }

    /** The classes of the carried timer rows that take a lock (lot V2-9, RFC-042 section 9). */
    static List<Class<?>> timerBeans() {
        return List.of(com.ibm.websphere.samples.daytrader.ejb3.MarketSummarySingleton.class);
    }

    /** Lookup only: bean class, then method, then whether the lock is the write lock. */
    private final Map<Class<?>, Map<Method, Boolean>> table = new HashMap<>();
    private final Map<Class<?>, ReentrantReadWriteLock> locks = new HashMap<>();
    private final Pointcut pointcut;
    private final Advice advice;

    public EngineSingletonLocks() {
        setOrder(Ordered.LOWEST_PRECEDENCE - 2);
        for (Row row : rows()) {
            Method method = ReflectionUtils.findMethod(row.bean, row.method, row.parameters);
            if (method == null || !method.getDeclaringClass().equals(row.bean)) {
                throw new IllegalStateException("singleton lock row names no method: "
                        + row.bean.getName() + "#" + row.method);
            }
            Map<Method, Boolean> byMethod = table.computeIfAbsent(row.bean, k -> new HashMap<>());
            if (byMethod.put(method, row.write) != null) {
                throw new IllegalStateException("singleton lock row listed twice: "
                        + row.bean.getName() + "#" + row.method);
            }
            locks.computeIfAbsent(row.bean, k -> new ReentrantReadWriteLock());
        }
        for (Class<?> bean : timerBeans()) {
            locks.computeIfAbsent(bean, k -> new ReentrantReadWriteLock());
        }
        ClassFilter classes = type -> table.containsKey(ClassUtils.getUserClass(type));
        MethodMatcher methods = new StaticMethodMatcher() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return kindOf(method, targetClass) != null;
            }
        };
        this.pointcut = new Pointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return classes;
            }

            @Override
            public MethodMatcher getMethodMatcher() {
                return methods;
            }
        };
        this.advice = (MethodInterceptor) this::invoke;
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    private Boolean kindOf(Method method, Class<?> targetClass) {
        Class<?> user = ClassUtils.getUserClass(targetClass);
        Map<Method, Boolean> byMethod = table.get(user);
        if (byMethod == null) {
            return null;
        }
        return byMethod.get(AopUtils.getMostSpecificMethod(method, user));
    }

    /**
     * Lot V2-9 (RFC-042 section 9): runs {@code body} under the read or write lock of {@code bean}, with the
     * loopback check of a business method. {@code EngineTimers} calls it around a timer callback, outside the
     * callback's transaction, as the advisor is outside the transaction advisor.
     */
    <T> T callLocked(Class<?> bean, boolean write, Callable<T> body) throws Exception {
        ReentrantReadWriteLock lock = locks.get(bean);
        if (lock == null) {
            throw new IllegalStateException("no singleton lock for " + bean.getName());
        }
        if (write) {
            if (lock.getReadHoldCount() > 0 && !lock.isWriteLockedByCurrentThread()) {
                throw new LockUpgradeException("write lock asked while holding only the read lock: "
                        + bean.getName());
            }
            lock.writeLock().lock();
            try {
                return body.call();
            } finally {
                lock.writeLock().unlock();
            }
        }
        lock.readLock().lock();
        try {
            return body.call();
        } finally {
            lock.readLock().unlock();
        }
    }

    private Object invoke(MethodInvocation invocation) throws Throwable {
        Object target = invocation.getThis();
        Class<?> user = ClassUtils.getUserClass(target == null ? invocation.getMethod().getDeclaringClass() : target.getClass());
        Boolean write = kindOf(invocation.getMethod(), user);
        ReentrantReadWriteLock lock = locks.get(user);
        if (write == null || lock == null) {
            return invocation.proceed();
        }
        if (write) {
            if (lock.getReadHoldCount() > 0 && !lock.isWriteLockedByCurrentThread()) {
                throw new LockUpgradeException("write lock asked while holding only the read lock: "
                        + user.getName() + "#" + invocation.getMethod().getName());
            }
            lock.writeLock().lock();
            try {
                return invocation.proceed();
            } finally {
                lock.writeLock().unlock();
            }
        }
        lock.readLock().lock();
        try {
            return invocation.proceed();
        } finally {
            lock.readLock().unlock();
        }
    }
}
