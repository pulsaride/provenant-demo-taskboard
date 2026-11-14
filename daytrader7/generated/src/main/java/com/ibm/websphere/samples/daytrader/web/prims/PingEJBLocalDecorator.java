package com.ibm.websphere.samples.daytrader.web.prims;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;

// TODO(interceptor: cdi_decorator_not_mapped)
// TODO(constructor_injection: ctor_injection_dependency_cycle)
// TODO(annotation: Decorator, decorator_api_type_not_emitted)
@Priority(value = Interceptor.Priority.APPLICATION)
public class PingEJBLocalDecorator implements PingEJBIFace {

    // TODO(annotation: Delegate, decorator_api_type_not_emitted)
    @Inject
    PingEJBIFace ejb;

    @Override
    public String getMsg() {
        return "Decorated " + ejb.getMsg();
    }
}
