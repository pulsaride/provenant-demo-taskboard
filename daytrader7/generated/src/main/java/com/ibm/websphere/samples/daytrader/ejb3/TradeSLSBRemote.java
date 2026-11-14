package com.ibm.websphere.samples.daytrader.ejb3;

import com.ibm.websphere.samples.daytrader.TradeServices;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import java.math.BigDecimal;

// TODO(annotation: Remote, ejb_not_mapped)
public interface TradeSLSBRemote extends TradeServices {

    double investmentReturn(double investment, double NetValue) throws Exception;

    QuoteDataBean pingTwoPhase(String symbol) throws Exception;

    void publishQuotePriceChange(QuoteDataBean quote, BigDecimal oldPrice, BigDecimal changeFactor, double sharesTraded);
}
