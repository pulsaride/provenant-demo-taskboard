package com.ibm.websphere.samples.daytrader.web.prims;

import org.springframework.stereotype.Component;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.io.Serializable;

@PingInterceptorBinding
@Interceptor
@Priority(value = Interceptor.Priority.APPLICATION)
@Component
public class PingInterceptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @AroundInvoke
    public Object methodInterceptor(InvocationContext ctx) throws Exception {
        return ctx.proceed();
    }
}
