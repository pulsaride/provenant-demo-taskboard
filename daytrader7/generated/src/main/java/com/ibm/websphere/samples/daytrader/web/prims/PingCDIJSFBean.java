package com.ibm.websphere.samples.daytrader.web.prims;

import org.springframework.web.context.annotation.SessionScope;

import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScope
public class PingCDIJSFBean implements Serializable {

    private static final long serialVersionUID = -7475815494313679416L;

    private int hitCount = 0;

    public int getHitCount() {
        return ++hitCount;
    }
}
