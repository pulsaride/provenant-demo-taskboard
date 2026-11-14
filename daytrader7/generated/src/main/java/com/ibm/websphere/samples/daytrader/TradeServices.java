package com.ibm.websphere.samples.daytrader;

import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.beans.RunStatsDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;
import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import java.math.BigDecimal;
import java.util.Collection;

public interface TradeServices {

    MarketSummaryDataBean getMarketSummary() throws Exception;

    OrderDataBean buy(String userID, String symbol, double quantity, int orderProcessingMode) throws Exception;

    OrderDataBean sell(String userID, Integer holdingID, int orderProcessingMode) throws Exception;

    void queueOrder(Integer orderID, boolean twoPhase) throws Exception;

    OrderDataBean completeOrder(Integer orderID, boolean twoPhase) throws Exception;

    void cancelOrder(Integer orderID, boolean twoPhase) throws Exception;

    void orderCompleted(String userID, Integer orderID) throws Exception;

    Collection<?> getOrders(String userID) throws Exception;

    Collection<?> getClosedOrders(String userID) throws Exception;

    QuoteDataBean createQuote(String symbol, String companyName, BigDecimal price) throws Exception;

    QuoteDataBean getQuote(String symbol) throws Exception;

    Collection<?> getAllQuotes() throws Exception;

    QuoteDataBean updateQuotePriceVolume(String symbol, BigDecimal newPrice, double sharesTraded) throws Exception;

    Collection<?> getHoldings(String userID) throws Exception;

    HoldingDataBean getHolding(Integer holdingID) throws Exception;

    AccountDataBean getAccountData(String userID) throws Exception;

    AccountProfileDataBean getAccountProfileData(String userID) throws Exception;

    AccountProfileDataBean updateAccountProfile(AccountProfileDataBean profileData) throws Exception;

    AccountDataBean login(String userID, String password) throws Exception;

    void logout(String userID) throws Exception;

    AccountDataBean register(String userID, String password, String fullname, String address, String email, String creditcard, BigDecimal openBalance) throws Exception;

    RunStatsDataBean resetTrade(boolean deleteAll) throws Exception;
}
