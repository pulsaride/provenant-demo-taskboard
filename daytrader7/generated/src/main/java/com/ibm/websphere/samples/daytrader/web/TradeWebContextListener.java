package com.ibm.websphere.samples.daytrader.web;

import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class TradeWebContextListener implements ServletContextListener {

    // TODO(annotation: Override)
    public void contextInitialized(ServletContextEvent event) {
        Log.trace("TradeWebContextListener contextInitialized -- initializing TradeDirect");
        
        // Load settings from properties file (if it exists)
        Properties prop = new Properties();
        InputStream stream =  event.getServletContext().getResourceAsStream("/properties/daytrader.properties");
        
        try {
            prop.load(stream);
            System.out.println("Settings from daytrader.properties: " + prop);
            TradeConfig.setRunTimeMode(Integer.parseInt(prop.getProperty("runtimeMode")));
            TradeConfig.setUseRemoteEJBInterface(Boolean.parseBoolean(prop.getProperty("useRemoteEJBInterface")));
            TradeConfig.setOrderProcessingMode(Integer.parseInt(prop.getProperty("orderProcessingMode")));
            TradeConfig.setWebInterface(Integer.parseInt(prop.getProperty("webInterface")));
            //TradeConfig.setCachingType(Integer.parseInt(prop.getProperty("cachingType")));
            //TradeConfig.setDistributedMapCacheSize(Integer.parseInt(prop.getProperty("cacheSize")));
            TradeConfig.setMAX_USERS(Integer.parseInt(prop.getProperty("maxUsers")));
            TradeConfig.setMAX_QUOTES(Integer.parseInt(prop.getProperty("maxQuotes")));
            TradeConfig.setMarketSummaryInterval(Integer.parseInt(prop.getProperty("marketSummaryInterval")));
            TradeConfig.setPrimIterations(Integer.parseInt(prop.getProperty("primIterations")));
            TradeConfig.setPublishQuotePriceChange(Boolean.parseBoolean(prop.getProperty("publishQuotePriceChange")));
            TradeConfig.setPercentSentToWebsocket(Integer.parseInt(prop.getProperty("percentSentToWebsocket")));
            TradeConfig.setDisplayOrderAlerts(Boolean.parseBoolean(prop.getProperty("displayOrderAlerts")));
            TradeConfig.setLongRun(Boolean.parseBoolean(prop.getProperty("longRun")));
            TradeConfig.setTrace(Boolean.parseBoolean(prop.getProperty("trace")));
            TradeConfig.setActionTrace(Boolean.parseBoolean(prop.getProperty("actionTrace")));
        } catch (Exception e) {
            System.out.println("daytrader.properties not found");
        }
        
        TradeDirect.init();
    }

    // TODO(annotation: Override)
    public void contextDestroyed(ServletContextEvent event) {
        Log.trace("TradeWebContextListener  contextDestroy calling TradeDirect:destroy()");
    }
}
