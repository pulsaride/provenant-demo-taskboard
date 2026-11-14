package com.ibm.websphere.samples.daytrader.ejb3;

import com.ibm.websphere.samples.daytrader.TradeServices;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import java.math.BigDecimal;

public interface TradeSLSBLocal extends TradeServices {

    double investmentReturn(double investment, double NetValue) throws Exception;

    QuoteDataBean pingTwoPhase(String symbol) throws Exception;

    void publishQuotePriceChange(QuoteDataBean quote, BigDecimal oldPrice, BigDecimal changeFactor, double sharesTraded);
}
