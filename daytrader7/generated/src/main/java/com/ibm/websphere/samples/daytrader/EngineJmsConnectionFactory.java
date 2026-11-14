package com.ibm.websphere.samples.daytrader;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.TopicConnectionFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * The connection factory of the injected and looked-up legacy code (engine-authored, ruling
 * 2026-11-13 v2-8b A.3). Only the no-argument createContext() is carried; its context carries
 * createTextMessage(), createTextMessage(String), createProducer() and close() (the producer is
 * the transacted one of EngineJmsContext); any other method throws
 * UnsupportedOperationException (no admitted body reaches one). Listed in
 * reserved_to_emitter.engine_files; not patchable.
 */
public final class EngineJmsConnectionFactory {

    private EngineJmsConnectionFactory() {}

    /** A factory of both domains, the one type a @Resource field of either kind receives. */
    public interface Both extends QueueConnectionFactory, TopicConnectionFactory {}

    public static Both of(ConnectionFactory broker, JMSContext engineContext) {
        return (Both)
                Proxy.newProxyInstance(
                        EngineJmsConnectionFactory.class.getClassLoader(),
                        new Class<?>[] {Both.class},
                        new FactoryHandler(broker, engineContext));
    }

    private static Object objectMethod(Object proxy, Method method, Object[] args) {
        return switch (method.getName()) {
            case "equals" -> proxy == args[0];
            case "hashCode" -> System.identityHashCode(proxy);
            default -> "EngineJmsConnectionFactory";
        };
    }

    private static UnsupportedOperationException unsupported(String type, Method method) {
        return new UnsupportedOperationException(
                type + "." + method.getName() + " is not carried by the engine JMS factory");
    }

    private static final class FactoryHandler implements InvocationHandler {
        private final ConnectionFactory broker;
        private final JMSContext engineContext;
        private JMSContext messages;

        FactoryHandler(ConnectionFactory broker, JMSContext engineContext) {
            this.broker = broker;
            this.engineContext = engineContext;
        }

        synchronized JMSContext messages() {
            if (messages == null) {
                messages = broker.createContext();
            }
            return messages;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            if (method.getDeclaringClass() == Object.class) {
                return objectMethod(proxy, method, args);
            }
            if (method.getName().equals("createContext") && method.getParameterCount() == 0) {
                return Proxy.newProxyInstance(
                        EngineJmsConnectionFactory.class.getClassLoader(),
                        new Class<?>[] {JMSContext.class},
                        new ContextHandler(this, engineContext));
            }
            throw unsupported("ConnectionFactory", method);
        }
    }

    private static final class ContextHandler implements InvocationHandler {
        private final FactoryHandler factory;
        private final JMSContext engineContext;

        ContextHandler(FactoryHandler factory, JMSContext engineContext) {
            this.factory = factory;
            this.engineContext = engineContext;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return objectMethod(proxy, method, args);
            }
            switch (method.getName()) {
                case "createTextMessage" -> {
                    if (method.getParameterCount() == 0) {
                        return factory.messages().createTextMessage();
                    }
                    if (method.getParameterCount() == 1 && args[0] instanceof String text) {
                        return factory.messages().createTextMessage(text);
                    }
                }
                case "createProducer", "close" -> {
                    if (method.getParameterCount() == 0) {
                        try {
                            return method.invoke(engineContext, args);
                        } catch (InvocationTargetException e) {
                            throw e.getCause();
                        }
                    }
                }
                default -> {
                }
            }
            throw unsupported("JMSContext", method);
        }
    }
}
