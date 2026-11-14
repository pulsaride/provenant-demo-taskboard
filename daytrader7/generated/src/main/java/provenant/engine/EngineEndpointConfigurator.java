package provenant.engine;

import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * Creates each endpoint instance through Spring (constructor, injection,
 * post-construct), one new instance per connection as JSR-356 requires. Without
 * a bean factory the handshake fails closed.
 */
public class EngineEndpointConfigurator extends ServerEndpointConfig.Configurator {

    private static volatile AutowireCapableBeanFactory factory;

    /** Set once, before the first endpoint is deployed. */
    public static void hold(AutowireCapableBeanFactory beanFactory) {
        factory = beanFactory;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        AutowireCapableBeanFactory held = factory;
        if (held == null) {
            throw new InstantiationException("no bean factory to create " + endpointClass.getName());
        }
        return held.createBean(endpointClass);
    }
}
