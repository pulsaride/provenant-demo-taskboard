package com.ibm.websphere.samples.daytrader;

import jakarta.interceptor.InvocationContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Engine-authored (lot V2-2a, RFC-042 section 2): runs the legacy EE
 * interceptor chain of each method listed in {@link EngineInterceptorBindings}, and nothing
 * else. An advisor of infrastructure role at the lowest precedence: the
 * transaction advisor, when one is configured, is outside the chain.
 */
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class EngineInterceptorChain extends AbstractPointcutAdvisor
        implements ApplicationContextAware, SmartInitializingSingleton {

    /** One element of a chain: an interceptor class and its around-invoke method. */
    public static final class Step {
        private final Class<?> interceptor;
        private final String aroundInvoke;

        private Step(Class<?> interceptor, String aroundInvoke) {
            this.interceptor = interceptor;
            this.aroundInvoke = aroundInvoke;
        }
    }

    /** One intercepted method: bean class, name, erased parameter types, chain. */
    public static final class Binding {
        private final Class<?> target;
        private final String method;
        private final Class<?>[] parameters;
        private final List<Step> steps;

        private Binding(Class<?> target, String method, Class<?>[] parameters, List<Step> steps) {
            this.target = target;
            this.method = method;
            this.parameters = parameters;
            this.steps = steps;
        }
    }

    public static Step step(Class<?> interceptor, String aroundInvoke) {
        return new Step(interceptor, aroundInvoke);
    }

    public static Binding binding(Class<?> target, String method, Class<?>[] parameters, Step... steps) {
        return new Binding(target, method, parameters, List.of(steps));
    }

    /** A chain element with its around-invoke method resolved. */
    private static final class Resolved {
        private final Class<?> type;
        private final Method aroundInvoke;

        private Resolved(Class<?> type, Method aroundInvoke) {
            this.type = type;
            this.aroundInvoke = aroundInvoke;
        }
    }

    /** Lookup only: bean class, then most specific method. */
    private final Map<Class<?>, Map<Method, List<Resolved>>> table = new HashMap<>();
    private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();
    private final Pointcut pointcut;
    private final Advice advice;
    private ApplicationContext context;

    public EngineInterceptorChain() {
        setOrder(Ordered.LOWEST_PRECEDENCE);
        for (Binding binding : EngineInterceptorBindings.bindings()) {
            Method method = ReflectionUtils.findMethod(binding.target, binding.method, binding.parameters);
            if (method == null) {
                throw new IllegalStateException("interceptor binding names no method: "
                        + binding.target.getName() + "#" + binding.method);
            }
            List<Resolved> chain = new ArrayList<>();
            for (Step step : binding.steps) {
                Method around = ReflectionUtils.findMethod(step.interceptor, step.aroundInvoke,
                        InvocationContext.class);
                if (around == null) {
                    throw new IllegalStateException("interceptor has no around-invoke method: "
                            + step.interceptor.getName() + "#" + step.aroundInvoke);
                }
                ReflectionUtils.makeAccessible(around);
                chain.add(new Resolved(step.interceptor, around));
            }
            Map<Method, List<Resolved>> byMethod = table.computeIfAbsent(binding.target, k -> new HashMap<>());
            if (byMethod.put(method, Collections.unmodifiableList(chain)) != null) {
                throw new IllegalStateException("interceptor binding listed twice: "
                        + binding.target.getName() + "#" + binding.method);
            }
        }
        ClassFilter classes = type -> table.containsKey(ClassUtils.getUserClass(type));
        MethodMatcher methods = new StaticMethodMatcher() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return chainOf(method, targetClass) != null;
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

    @Override
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    /** Every interceptor bean is resolved once the singletons exist: a missing one fails the start. */
    @Override
    public void afterSingletonsInstantiated() {
        for (Map<Method, List<Resolved>> byMethod : table.values()) {
            for (List<Resolved> chain : byMethod.values()) {
                for (Resolved step : chain) {
                    instanceOf(step.type);
                }
            }
        }
    }

    private Object instanceOf(Class<?> type) {
        return instances.computeIfAbsent(type, context::getBean);
    }

    private List<Resolved> chainOf(Method method, Class<?> targetClass) {
        Class<?> user = ClassUtils.getUserClass(targetClass);
        Map<Method, List<Resolved>> byMethod = table.get(user);
        if (byMethod == null) {
            return null;
        }
        return byMethod.get(AopUtils.getMostSpecificMethod(method, user));
    }

    private Object invoke(MethodInvocation invocation) throws Throwable {
        Object target = invocation.getThis();
        Class<?> user = ClassUtils.getUserClass(target == null ? Object.class : target.getClass());
        List<Resolved> chain = chainOf(invocation.getMethod(), user);
        if (chain == null) {
            return invocation.proceed();
        }
        if (!(invocation instanceof ProxyMethodInvocation proxied)) {
            throw new IllegalStateException("interceptor chain needs a proxy invocation: "
                    + invocation.getMethod());
        }
        State state = new State(proxied, AopUtils.getMostSpecificMethod(invocation.getMethod(), user),
                chain, invocation.getArguments());
        return new Context(state, 0).proceed();
    }

    /** What every position of one invocation shares. */
    private static final class State {
        private final ProxyMethodInvocation invocation;
        private final Method method;
        private final List<Resolved> chain;
        private final Map<String, Object> contextData = new HashMap<>();
        private Object[] parameters;

        private State(ProxyMethodInvocation invocation, Method method, List<Resolved> chain,
                Object[] parameters) {
            this.invocation = invocation;
            this.method = method;
            this.chain = chain;
            this.parameters = parameters;
        }
    }

    private static Exception rethrown(Throwable thrown) {
        if (thrown instanceof Exception exception) {
            return exception;
        }
        if (thrown instanceof Error error) {
            throw error;
        }
        return new UndeclaredThrowableException(thrown);
    }

    /** The engine InvocationContext: one view per chain position. */
    private final class Context implements InvocationContext {
        private final State state;
        private final int position;

        private Context(State state, int position) {
            this.state = state;
            this.position = position;
        }

        @Override
        public Object getTarget() {
            return state.invocation.getThis();
        }

        @Override
        public Object getTimer() {
            throw new UnsupportedOperationException("interceptor_context_method_unsupported: getTimer");
        }

        @Override
        public Method getMethod() {
            return state.method;
        }

        @Override
        public Constructor<?> getConstructor() {
            throw new UnsupportedOperationException("interceptor_context_method_unsupported: getConstructor");
        }

        @Override
        public Object[] getParameters() {
            return state.parameters;
        }

        @Override
        public void setParameters(Object[] parameters) {
            Class<?>[] types = state.method.getParameterTypes();
            if (parameters == null || parameters.length != types.length) {
                throw new IllegalArgumentException("wrong number of parameters for " + state.method);
            }
            for (int i = 0; i < types.length; i++) {
                if (!ClassUtils.isAssignableValue(types[i], parameters[i])) {
                    throw new IllegalArgumentException("parameter " + i + " is not assignable to "
                            + types[i].getName() + " for " + state.method);
                }
            }
            state.parameters = parameters;
        }

        @Override
        public Map<String, Object> getContextData() {
            return state.contextData;
        }

        @Override
        public Object proceed() throws Exception {
            if (position < state.chain.size()) {
                Resolved next = state.chain.get(position);
                try {
                    return next.aroundInvoke.invoke(instanceOf(next.type), new Context(state, position + 1));
                } catch (InvocationTargetException e) {
                    throw rethrown(e.getCause());
                }
            }
            try {
                return state.invocation.invocableClone(state.parameters).proceed();
            } catch (Throwable thrown) {
                throw rethrown(thrown);
            }
        }
    }
}
