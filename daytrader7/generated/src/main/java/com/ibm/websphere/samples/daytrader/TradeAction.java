package com.ibm.websphere.samples.daytrader;

import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.beans.RunStatsDataBean;
import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBLocal;
import com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBRemote;
import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;
import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import java.math.BigDecimal;
import java.util.Collection;
import javax.naming.InitialContext;

// TODO(initializer_block_not_emitted: com.ibm.websphere.samples.daytrader.TradeAction)
public class TradeAction implements TradeServices {

    public TradeAction() {
        if (Log.doTrace()) {
            Log.trace("TradeAction:TradeAction()");
        }
        createTrade();
    }

    public TradeAction(TradeServices trade) {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:TradeAction(trade)");
        }
        TradeAction.trade = trade;
    }

    private static final Integer marketSummaryLock = new Integer(0);

    private static long nextMarketSummary = System.currentTimeMillis();

    private static MarketSummaryDataBean cachedMSDB = MarketSummaryDataBean.getRandomInstance();

    private static TradeServices trade = null;

    private static TradeServices tradeLocal = null;

    private static TradeServices tradeRemote = null;

    private void createTrade() {
        if (TradeConfig.runTimeMode == TradeConfig.EJB3) {
            try {	     
            	
            	if (tradeLocal == null && tradeRemote == null ) {
            		InitialContext context = new InitialContext();
            		tradeLocal = (TradeSLSBLocal) context.lookup("java:comp/env/ejb/TradeSLSBBean");
            		tradeRemote = (TradeSLSBRemote) context.lookup("java:comp/env/ejb/TradeSLSBBeanRemote");
            	}
            	
            	// Determine local or remote interface.
            	if (!TradeConfig.useRemoteEJBInterface()) {
            		if (!(trade instanceof TradeSLSBLocal)) {
            			trade = tradeLocal;
            		}
            	} else if (!(trade instanceof TradeSLSBRemote)) {
            		/* TODO: For split tier this will need to be changed
            			I have not tried this yet with DT7 */
            		trade = tradeRemote;
            	}
            } catch (Exception e) {
                Log.error("TradeAction:TradeAction() Creation of Trade EJB 3 failed\n" + e);
                e.printStackTrace();
            }
        } else if (TradeConfig.runTimeMode == TradeConfig.DIRECT) {
            try {
                trade = new TradeDirect();
            } catch (Exception e) {
                Log.error("TradeAction:TradeAction() Creation of Trade Direct failed\n" + e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public MarketSummaryDataBean getMarketSummary() throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:getMarketSummary()");
        }

        // If EJB3 mode, then have the Singleton Bean handle this.
        if (TradeConfig.getRunTimeMode() == TradeConfig.EJB3)
        {
            if (Log.doActionTrace()) {
                Log.trace("TradeAction:getMarketSummary() -- EJB3 mode, using Singleton Bean");
            }
            return trade.getMarketSummary();
        }
                        
        if (TradeConfig.getMarketSummaryInterval() == 0) {
            return getMarketSummaryInternal();
        }
        if (TradeConfig.getMarketSummaryInterval() < 0) {
            return cachedMSDB;
        }

        /**
         * This is a little funky. If its time to fetch a new Market summary
         * then we'll synchronize access to make sure only one requester does
         * it. Others will merely return the old copy until the new
         * MarketSummary has been executed.
         */

        long currentTime = System.currentTimeMillis();

        if (currentTime > nextMarketSummary) {
            long oldNextMarketSummary = nextMarketSummary;
            boolean fetch = false;

            synchronized (marketSummaryLock) {
                /**
                 * Is it still ahead or did we miss lose the race? If we lost
                 * then let's get out of here as the work has already been done.
                 */
                if (oldNextMarketSummary == nextMarketSummary) {
                    fetch = true;
                    nextMarketSummary += TradeConfig.getMarketSummaryInterval() * 1000;

                    /**
                     * If the server has been idle for a while then its possible
                     * that nextMarketSummary could be way off. Rather than try
                     * and play catch up we'll simply get in sync with the
                     * current time + the interval.
                     */
                    if (nextMarketSummary < currentTime) {
                        nextMarketSummary = currentTime + TradeConfig.getMarketSummaryInterval() * 1000;
                    }
                }
            }

            /**
             * If we're the lucky one then let's update the MarketSummary
             */
            if (fetch) {
                cachedMSDB = getMarketSummaryInternal();
            }
        }

        return cachedMSDB;
    }

    public MarketSummaryDataBean getMarketSummaryInternal() throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:getMarketSummaryInternal()");
        }
        MarketSummaryDataBean marketSummaryData = null;
        marketSummaryData = trade.getMarketSummary();
        return marketSummaryData;
    }

    @Override
    public OrderDataBean buy(String userID, String symbol, double quantity, int orderProcessingMode) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:buy", userID, symbol, new Double(quantity), new Integer(orderProcessingMode));
        }
        OrderDataBean orderData = trade.buy(userID, symbol, quantity, orderProcessingMode);
        
        // after the purchase or sell of a stock, update the stocks volume and
        // price

        updateQuotePriceVolume(symbol, TradeConfig.getRandomPriceChangeFactor(), quantity);

        return orderData;
    }

    public OrderDataBean sell(String userID, int holdingID, int orderProcessingMode) throws Exception {
        return sell(userID, new Integer(holdingID), orderProcessingMode);
    }

    @Override
    public OrderDataBean sell(String userID, Integer holdingID, int orderProcessingMode) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:sell", userID, holdingID, new Integer(orderProcessingMode));
        }
        OrderDataBean orderData = trade.sell(userID, holdingID, orderProcessingMode);
       

        if (!orderData.getOrderStatus().equalsIgnoreCase("cancelled")) {
            updateQuotePriceVolume(orderData.getSymbol(), TradeConfig.getRandomPriceChangeFactor(), orderData.getQuantity());
        }

        return orderData;
    }

    @Override
    public void queueOrder(Integer orderID, boolean twoPhase) {
        throw new UnsupportedOperationException("TradeAction: queueOrder method not supported");
    }

    @Override
    public OrderDataBean completeOrder(Integer orderID, boolean twoPhase) {
        throw new UnsupportedOperationException("TradeAction: completeOrder method not supported");
    }

    @Override
    public void cancelOrder(Integer orderID, boolean twoPhase) {
        throw new UnsupportedOperationException("TradeAction: cancelOrder method not supported");
    }

    @Override
    public void orderCompleted(String userID, Integer orderID) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:orderCompleted", userID, orderID);
        }
        if (Log.doTrace()) {
            Log.trace("OrderCompleted", userID, orderID);
        }
    }

    @Override
    public Collection<?> getOrders(String userID) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:getOrders", userID);
        }
        Collection<?> orderDataBeans = trade.getOrders(userID);
        return orderDataBeans;
    }

    @Override
    public Collection<?> getClosedOrders(String userID) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:getClosedOrders", userID);
        }
        Collection<?> orderDataBeans = trade.getClosedOrders(userID);
        return orderDataBeans;
    }

    @Override
    public QuoteDataBean createQuote(String symbol, String companyName, BigDecimal price) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:createQuote", symbol, companyName, price);
        }
        return trade.createQuote(symbol, companyName, price);
    }

    @Override
    public Collection<?> getAllQuotes() throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:getAllQuotes");
        }
        return trade.getAllQuotes();
    }

    @Override
    public QuoteDataBean getQuote(String symbol) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:getQuote", symbol);
        }
        if (((symbol == null) || (symbol.length() == 0)) || (symbol.length() > 10)) {
            if (Log.doActionTrace()) {
                Log.trace("TradeAction:getQuote   ---  primitive workload");
            }
            return new QuoteDataBean("Invalid symbol", "", 0.0, FinancialUtils.ZERO, FinancialUtils.ZERO, FinancialUtils.ZERO, FinancialUtils.ZERO, 0.0);
        }
        QuoteDataBean quoteData = trade.getQuote(symbol);
        return quoteData;
    }

    @Override
    public QuoteDataBean updateQuotePriceVolume(String symbol, BigDecimal changeFactor, double sharesTraded) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:updateQuotePriceVolume", symbol, changeFactor, new Double(sharesTraded));
        }
        QuoteDataBean quoteData = null;
        try {
        	quoteData = trade.updateQuotePriceVolume(symbol, changeFactor, sharesTraded);
        } catch (Exception e) {
            Log.error("TradeAction:updateQuotePrice -- ", e);
        }

        return quoteData;
    }

    @Override
    public Collection<?> getHoldings(String userID) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:getHoldings", userID);
        }
        Collection<?> holdingDataBeans = trade.getHoldings(userID);
        return holdingDataBeans;
    }

    @Override
    public HoldingDataBean getHolding(Integer holdingID) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:getHolding", holdingID);
        }
        return trade.getHolding(holdingID);
    }

    @Override
    public AccountDataBean getAccountData(String userID) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:getAccountData", userID);
        }
        AccountDataBean accountData = trade.getAccountData(userID);
        return accountData;
    }

    @Override
    public AccountProfileDataBean getAccountProfileData(String userID) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:getAccountProfileData", userID);
        }
        AccountProfileDataBean accountProfileData = trade.getAccountProfileData(userID);
        return accountProfileData;
    }

    @Override
    public AccountProfileDataBean updateAccountProfile(AccountProfileDataBean accountProfileData) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:updateAccountProfile", accountProfileData);
        }
        accountProfileData = trade.updateAccountProfile(accountProfileData);
        return accountProfileData;
    }

    @Override
    public AccountDataBean login(String userID, String password) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:login", userID, password);
        }
        AccountDataBean accountData = trade.login(userID, password);
        return accountData;
    }

    @Override
    public void logout(String userID) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:logout", userID);
        }
        trade.logout(userID);
    }

    @Override
    public AccountDataBean register(String userID, String password, String fullname, String address, String email, String creditCard, BigDecimal openBalance) throws Exception {
        if (Log.doActionTrace()) {
            Log.trace("TradeAction:register", userID, password, fullname, address, email, creditCard, openBalance);
        }
        return trade.register(userID, password, fullname, address, email, creditCard, openBalance);
    }

    public AccountDataBean register(String userID, String password, String fullname, String address, String email, String creditCard, String openBalanceString) throws Exception {
        BigDecimal openBalance = new BigDecimal(openBalanceString);
        return register(userID, password, fullname, address, email, creditCard, openBalance);
    }

    @Override
    public RunStatsDataBean resetTrade(boolean deleteAll) throws Exception {
        RunStatsDataBean runStatsData = trade.resetTrade(deleteAll);
        return runStatsData;
    }
}
