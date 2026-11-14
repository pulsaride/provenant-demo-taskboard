package com.ibm.websphere.samples.daytrader;

import jakarta.jms.BytesMessage;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSContext;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

/**
 * The JMSContext of the injected legacy code (engine-authored, RFC-039 section 2.3). Only
 * createProducer() and a producer chain of setPriority, setTimeToLive, setDeliveryMode,
 * setDeliveryDelay, setDisableMessageID, setDisableMessageTimestamp and send are carried; any
 * other method throws UnsupportedOperationException (no admitted body reaches one). A send goes
 * through a JmsTemplate with a transacted session: inside a Spring transaction the message is
 * committed after it, the Java EE ordering. Not request-scoped: a send may run on a listener
 * thread. Listed in reserved_to_emitter.engine_files; not patchable.
 */
@Configuration
public class EngineJmsContext {

    @Bean(destroyMethod = "")
    public JMSContext jmsContext(ConnectionFactory connectionFactory) {
        return (JMSContext)
                Proxy.newProxyInstance(
                        EngineJmsContext.class.getClassLoader(),
                        new Class<?>[] {JMSContext.class},
                        new ContextHandler(connectionFactory));
    }

    private static Object objectMethod(Object proxy, Method method, Object[] args) {
        return switch (method.getName()) {
            case "equals" -> proxy == args[0];
            case "hashCode" -> System.identityHashCode(proxy);
            default -> "EngineJmsContext";
        };
    }

    private static UnsupportedOperationException unsupported(String type, Method method) {
        return new UnsupportedOperationException(
                type + "." + method.getName() + " is not carried by the engine JMS context");
    }

    private static final class ContextHandler implements InvocationHandler {
        private final ConnectionFactory connectionFactory;

        ContextHandler(ConnectionFactory connectionFactory) {
            this.connectionFactory = connectionFactory;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            if (method.getDeclaringClass() == Object.class) {
                return objectMethod(proxy, method, args);
            }
            if (method.getParameterCount() == 0) {
                switch (method.getName()) {
                    case "createProducer" -> {
                        return producer(connectionFactory);
                    }
                    case "close" -> {
                        return null;
                    }
                    default -> {
                    }
                }
            }
            throw unsupported("JMSContext", method);
        }
    }

    private static Object producer(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setSessionTransacted(true);
        template.setExplicitQosEnabled(true);
        return Proxy.newProxyInstance(
                EngineJmsContext.class.getClassLoader(),
                new Class<?>[] {jakarta.jms.JMSProducer.class},
                new ProducerHandler(template));
    }

    private static final class ProducerHandler implements InvocationHandler {
        private final JmsTemplate template;

        ProducerHandler(JmsTemplate template) {
            this.template = template;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            if (method.getDeclaringClass() == Object.class) {
                return objectMethod(proxy, method, args);
            }
            switch (method.getName()) {
                case "setPriority" -> template.setPriority((Integer) args[0]);
                case "setTimeToLive" -> template.setTimeToLive((Long) args[0]);
                case "setDeliveryMode" -> template.setDeliveryMode((Integer) args[0]);
                case "setDeliveryDelay" -> template.setDeliveryDelay((Long) args[0]);
                case "setDisableMessageID" -> template.setMessageIdEnabled(!(Boolean) args[0]);
                case "setDisableMessageTimestamp" ->
                        template.setMessageTimestampEnabled(!(Boolean) args[0]);
                case "send" -> send((Destination) args[0], args[1]);
                default -> throw unsupported("JMSProducer", method);
            }
            return proxy;
        }

        private void send(Destination destination, Object body) {
            template.send(
                    destination,
                    session -> {
                        if (body instanceof Message message) {
                            return message;
                        }
                        if (body instanceof String text) {
                            return session.createTextMessage(text);
                        }
                        if (body instanceof byte[] bytes) {
                            BytesMessage message = session.createBytesMessage();
                            message.writeBytes(bytes);
                            return message;
                        }
                        if (body instanceof Map<?, ?> map) {
                            MapMessage message = session.createMapMessage();
                            for (Map.Entry<?, ?> entry : map.entrySet()) {
                                message.setObject(String.valueOf(entry.getKey()), entry.getValue());
                            }
                            return message;
                        }
                        if (body instanceof Serializable value) {
                            return session.createObjectMessage(value);
                        }
                        throw new IllegalArgumentException("unsupported JMS body: " + body);
                    });
        }
    }
}
