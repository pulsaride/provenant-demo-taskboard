package com.ibm.websphere.samples.daytrader.web.prims;

// TODO(ejb_adapter: ejb_stateful_session)
public class PingEJBLocal implements PingEJBIFace {

    private static int hitCount;

    @Override
    public String getMsg() {
        return "PingEJBLocal: " + hitCount++;
    }
}
