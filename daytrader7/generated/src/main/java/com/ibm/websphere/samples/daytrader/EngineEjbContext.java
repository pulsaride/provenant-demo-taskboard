package com.ibm.websphere.samples.daytrader;

import jakarta.ejb.EJBContext;
import jakarta.ejb.MessageDrivenContext;
import jakarta.ejb.SessionContext;
import jakarta.transaction.UserTransaction;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Map;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Engine-authored (lot V2-5a, RFC-042 section 5): the container's context object a bare
 * {@code @Resource SessionContext}, {@code MessageDrivenContext} or {@code EJBContext}
 * field is injected with. {@code getBusinessObject} answers the bean of the view from the
 * bean factory (the Spring proxy); {@code setRollbackOnly} and {@code getRollbackOnly}
 * read and mark the transaction status of the calling thread, and throw
 * {@link IllegalStateException} when no actual transaction is active. Every other method
 * throws {@link UnsupportedEjbContextOperation}. The five types of the interfaces' own signatures
 * that the kept row does not list ({@code EJBHome}, {@code EJBLocalHome}, {@code EJBLocalObject},
 * {@code EJBObject}, {@code TimerService}) are written qualified, never imported.
 */
public final class EngineEjbContext implements SessionContext, MessageDrivenContext {

    /** Thrown by every method outside the admitted table. */
    public static final class UnsupportedEjbContextOperation extends UnsupportedOperationException {

        private static final long serialVersionUID = 1L;

        UnsupportedEjbContextOperation(String method) {
            super("the engine EJB context does not carry " + method);
        }
    }

    /**
     * Thrown after a listener method returned normally having asked its transaction to roll
     * back: the listener container rolls the delivery back and the message is redelivered.
     */
    public static final class RollbackRequestedException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        RollbackRequestedException(String method) {
            super("the listener asked for a rollback: " + method);
        }
    }

    /** Whether {@code setRollbackOnly} was called during the listener method of this thread, if any. */
    private static final ThreadLocal<boolean[]> LISTENER = new ThreadLocal<>();

    /** The annotation of a listener method, matched by name so the file needs no JMS dependency. */
    private static final String LISTENER_ANNOTATION = "org.springframework.jms.annotation.JmsListener";

    /** Registers the object as a resolvable dependency, not as a bean definition. */
    @Component
    public static class Registrar implements BeanFactoryPostProcessor {

        private EngineEjbContext instance;

        /** The object this registrar made. */
        public EngineEjbContext instance() {
            return instance;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            EngineEjbContext context = new EngineEjbContext(beanFactory);
            beanFactory.registerResolvableDependency(SessionContext.class, context);
            beanFactory.registerResolvableDependency(MessageDrivenContext.class, context);
            beanFactory.registerResolvableDependency(EJBContext.class, context);
            this.instance = context;
        }
    }

    /**
     * Around a listener method, outside the transaction advisor: throws the engine exception after
     * a normal return when the method asked for a rollback, whichever bean it belongs to.
     */
    @Component
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public static class RedeliveryAdvisor extends AbstractPointcutAdvisor {

        private static final long serialVersionUID = 1L;

        private final transient Pointcut pointcut;
        private final transient Advice advice;

        public RedeliveryAdvisor() {
            setOrder(Ordered.LOWEST_PRECEDENCE - 3);
            ClassFilter classes = ClassFilter.TRUE;
            MethodMatcher methods = new StaticMethodMatcher() {
                @Override
                public boolean matches(Method method, Class<?> targetClass) {
                    return AnnotatedElementUtils.isAnnotated(
                            AopUtils.getMostSpecificMethod(method, targetClass), LISTENER_ANNOTATION);
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
            this.advice = (MethodInterceptor) RedeliveryAdvisor::invoke;
        }

        @Override
        public Pointcut getPointcut() {
            return pointcut;
        }

        @Override
        public Advice getAdvice() {
            return advice;
        }

        private static Object invoke(MethodInvocation invocation) throws Throwable {
            boolean[] enclosing = LISTENER.get();
            boolean[] own = new boolean[1];
            LISTENER.set(own);
            try {
                Object result = invocation.proceed();
                if (own[0]) {
                    throw new RollbackRequestedException(invocation.getMethod().getName());
                }
                return result;
            } finally {
                if (enclosing == null) {
                    LISTENER.remove();
                } else {
                    LISTENER.set(enclosing);
                }
            }
        }
    }

    private final BeanFactory beanFactory;

    EngineEjbContext(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    // --- the admitted table -------------------------------------------------

    @Override
    public <T> T getBusinessObject(Class<T> businessInterface) throws IllegalStateException {
        try {
            return beanFactory.getBean(businessInterface);
        } catch (BeansException e) {
            throw new IllegalStateException("no business object of " + businessInterface.getName(), e);
        }
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException {
        activeStatus().setRollbackOnly();
        boolean[] listener = LISTENER.get();
        if (listener != null) {
            listener[0] = true;
        }
    }

    @Override
    public boolean getRollbackOnly() throws IllegalStateException {
        return activeStatus().isRollbackOnly();
    }

    private static TransactionStatus activeStatus() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("no transaction is active");
        }
        try {
            return TransactionAspectSupport.currentTransactionStatus();
        } catch (NoTransactionException e) {
            throw new IllegalStateException("no container-managed transaction is active", e);
        }
    }

    // --- every other method: not carried ------------------------------------

    @Override
    public jakarta.ejb.EJBHome getEJBHome() {
        throw new UnsupportedEjbContextOperation("getEJBHome");
    }

    @Override
    public jakarta.ejb.EJBLocalHome getEJBLocalHome() {
        throw new UnsupportedEjbContextOperation("getEJBLocalHome");
    }

    @Override
    public Principal getCallerPrincipal() {
        throw new UnsupportedEjbContextOperation("getCallerPrincipal");
    }

    @Override
    public boolean isCallerInRole(String roleName) {
        throw new UnsupportedEjbContextOperation("isCallerInRole");
    }

    @Override
    public UserTransaction getUserTransaction() {
        throw new UnsupportedEjbContextOperation("getUserTransaction");
    }

    @Override
    public jakarta.ejb.TimerService getTimerService() {
        throw new UnsupportedEjbContextOperation("getTimerService");
    }

    @Override
    public Object lookup(String name) {
        throw new UnsupportedEjbContextOperation("lookup");
    }

    @Override
    public Map<String, Object> getContextData() {
        throw new UnsupportedEjbContextOperation("getContextData");
    }

    @Override
    public jakarta.ejb.EJBLocalObject getEJBLocalObject() {
        throw new UnsupportedEjbContextOperation("getEJBLocalObject");
    }

    @Override
    public jakarta.ejb.EJBObject getEJBObject() {
        throw new UnsupportedEjbContextOperation("getEJBObject");
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class getInvokedBusinessInterface() {
        throw new UnsupportedEjbContextOperation("getInvokedBusinessInterface");
    }

    @Override
    public boolean wasCancelCalled() {
        throw new UnsupportedEjbContextOperation("wasCancelCalled");
    }
}
