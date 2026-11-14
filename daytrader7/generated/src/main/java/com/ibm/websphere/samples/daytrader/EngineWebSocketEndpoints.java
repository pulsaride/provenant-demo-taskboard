package com.ibm.websphere.samples.daytrader;

import jakarta.servlet.ServletContext;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.server.ServerContainer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import provenant.engine.EngineEndpointConfigurator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

/**
 * Deploys the legacy WebSocket server endpoints on the embedded container.
 * Engine-authored; the deployed set is exactly the admitted declarations.
 */
@Component
public class EngineWebSocketEndpoints implements SmartInitializingSingleton, BeanFactoryAware, ServletContextAware {

    private static final String CONTAINER_ATTRIBUTE = "jakarta.websocket.server.ServerContainer";

    private BeanFactory beanFactory;
    private ServletContext servletContext;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        if (!(servletContext.getAttribute(CONTAINER_ATTRIBUTE) instanceof ServerContainer container)) {
            throw new IllegalStateException("the embedded container provides no WebSocket server container");
        }
        EngineEndpointConfigurator.hold((AutowireCapableBeanFactory) beanFactory);
        try {
            container.addEndpoint(com.ibm.websphere.samples.daytrader.web.websocket.MarketSummaryWebSocket.class);
            container.addEndpoint(com.ibm.websphere.samples.daytrader.web.prims.PingWebSocketBinary.class);
            container.addEndpoint(com.ibm.websphere.samples.daytrader.web.prims.PingWebSocketTextAsync.class);
            container.addEndpoint(com.ibm.websphere.samples.daytrader.web.prims.PingWebSocketTextSync.class);
            container.addEndpoint(com.ibm.websphere.samples.daytrader.web.prims.PingWebSocketJson.class);
        } catch (DeploymentException e) {
            throw new IllegalStateException("a WebSocket endpoint could not be deployed", e);
        }
    }
}
