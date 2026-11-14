package com.ibm.websphere.samples.daytrader.web.prims;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;
import javax.naming.InitialContext;

@PingInterceptorBinding
@Component
@RequestScope
public class PingCDIBean {

    private static int helloHitCount = 0;

    private static int getBeanManagerHitCountJNDI = 0;

    private static int getBeanManagerHitCountSPI = 0;

    public int hello() {
        return ++helloHitCount;
    }

    // TODO(body: retention_unprovided_library_type)
    public int getBeanMangerViaJNDI() throws Exception {
        throw new provenant.engine.EngineStubException("body not extracted for getBeanMangerViaJNDI — see lower::lir's module docs");
    }

    // TODO(body: retention_unprovided_library_type)
    public int getBeanMangerViaCDICurrent() throws Exception {
        throw new provenant.engine.EngineStubException("body not extracted for getBeanMangerViaCDICurrent — see lower::lir's module docs");
    }
}
