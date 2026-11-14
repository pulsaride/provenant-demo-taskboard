package com.ibm.websphere.samples.daytrader.web.jsf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.ibm.websphere.samples.daytrader.TradeAction;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class TradeActionProducer {

    @Bean(destroyMethod = "") @RequestScope
    public TradeAction produceTradeAction() {
        return new TradeAction();
    }
}
