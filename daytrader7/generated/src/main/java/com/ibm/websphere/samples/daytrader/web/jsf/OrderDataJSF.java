package com.ibm.websphere.samples.daytrader.web.jsf;

import org.springframework.context.annotation.Scope;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;

// TODO(field: injection_point_jsf_api_type_not_emitted: facesExternalContext: ExternalContext)
// TODO(constructor_injection: ctor_injection_explicit_constructor)
@Named(value = "orderdata")
@Scope("prototype")
public class OrderDataJSF {

    public OrderDataJSF() {
    }

    @Inject
    private TradeAction tradeAction;

    private OrderData[] allOrders;

    private OrderData orderData;

    // TODO(body: retention_calls_stubbed_member:facesExternalContext)
    public void getAllOrder() {
        throw new provenant.engine.EngineStubException("body not extracted for getAllOrder — see lower::lir's module docs");
    }

    // TODO(body: retention_calls_stubbed_member:facesExternalContext)
    // TODO(annotation: PostConstruct, body_not_extracted)
    public void getOrder() {
        throw new provenant.engine.EngineStubException("body not extracted for getOrder — see lower::lir's module docs");
    }

    public void setAllOrders(OrderData[] allOrders) {
        this.allOrders = allOrders;
    }

    public OrderData[] getAllOrders() {
        return allOrders;
    }

    public void setOrderData(OrderData orderData) {
        this.orderData = orderData;
    }

    public OrderData getOrderData() {
        return orderData;
    }
}
