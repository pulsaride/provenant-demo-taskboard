package com.ibm.websphere.samples.daytrader.web.websocket;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.jms.Message;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ServerEndpoint(value = "/marketsummary", decoders = ActionDecoder.class, configurator = provenant.engine.EngineEndpointConfigurator.class)
public class MarketSummaryWebSocket {

    @Resource
    private ManagedScheduledExecutorService managedScheduledExecutorService;

    private static final List<Session> SESSIONS = new CopyOnWriteArrayList<>();

    private static final int SCHEDULER_PERIOD = Integer.parseInt(System.getProperty("dt.ws.period", "2"));

    private final CountDownLatch latch = new CountDownLatch(1);

    private static boolean sendRecentQuotePriceChangeList = false;

    private static ScheduledFuture<?> scheduler = null;

    @OnOpen
    public void onOpen(Session session, EndpointConfig ec) {
        if (Log.doTrace()) {
            Log.trace("MarketSummaryWebSocket:onOpen -- session -->" + session + "<--");
        }

        synchronized(SESSIONS) {
            if (SESSIONS.size() == 0) {
                 if (Log.doTrace()) {
                    Log.trace("MarketSummaryWebSocket:onOpen -- start scheduler");
                 }
                startScheduler();
            }

            SESSIONS.add(session);
        }
  
        latch.countDown();
    }

    @OnMessage
    public void sendMarketSummary(ActionMessage message, Session currentSession) {
        String action = message.getDecodedAction();
        
        if (Log.doTrace()) {
            if (action != null ) {
                Log.trace("MarketSummaryWebSocket:sendMarketSummary -- received -->" + action + "<--");
            } else {
                Log.trace("MarketSummaryWebSocket:sendMarketSummary -- received -->null<--");
            }
        }

        // Make sure onopen is finished
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        
        if (action != null && action.equals("update")) {
            TradeAction tAction = new TradeAction();
                            
            JsonObject mkSummary = null;
            try {
                mkSummary = tAction.getMarketSummary().toJSON();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            if (Log.doTrace()) {
                Log.trace("MarketSummaryWebSocket:sendMarketSummary -- sending -->" + mkSummary + "<--");
            }
                            
            if (RecentStockChangeList.isEmpty()) {
                synchronized (currentSession) {
                    if (currentSession.isOpen()) {
                        try {
                            currentSession.getBasicRemote().sendText(mkSummary.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }   
            }
            else { // Merge Objects 
                JsonObject recentChangeList = RecentStockChangeList.stockChangesInJSON();
                if (currentSession.isOpen()) {
                    try {
                        currentSession.getBasicRemote().sendText(mergeJsonObjects(mkSummary,recentChangeList).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } 
            }
        }
    }

    @OnError
    public void onError(Throwable t, Session currentSession) {
        if (Log.doTrace()) {
            Log.trace(("MarketSummaryWebSocket:onError -- session -->" + currentSession) + "<--");
        }
        t.printStackTrace();
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        if (Log.doTrace()) {
            Log.trace("MarketSummaryWebSocket:onClose -- session -->" + session + "<--");
        }

        synchronized(SESSIONS) {
            SESSIONS.remove(session);
            if (SESSIONS.size() == 0) {  
                if (Log.doTrace()) {
                    Log.trace("MarketSummaryWebSocket:onClose -- cancel scheduler");
                }
                scheduler.cancel(false);
            }
        }
    }

    // TODO(param_annotation: message: Observes, cdi_not_mapped)
    // TODO(param_annotation: message: WebSocketJMSMessage, not_mapped)
    public static void onJMSMessage(Message message) {
        if (Log.doTrace()) {
            Log.trace("MarketSummaryWebSocket:onJMSMessage");
        }
        RecentStockChangeList.addStockChange(message);
        sendRecentQuotePriceChangeList = true;
    }

    private void sendRecentQuotePriceChangeList() {
        JsonObject stockChangeJson = RecentStockChangeList.stockChangesInJSON();
  
        for (Session session : SESSIONS) {
            synchronized (session) {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(stockChangeJson.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }      
                }
            }
        }
    }

    private void startScheduler() {
		scheduler = managedScheduledExecutorService.scheduleAtFixedRate(() -> {
        Log.trace("MarketSummaryWebSocket: Executing static scheduled task at: " + System.currentTimeMillis());
        if (sendRecentQuotePriceChangeList) {
          Log.trace("MarketSummaryWebSocket: sendList = true");
          sendRecentQuotePriceChangeList();
          sendRecentQuotePriceChangeList = false;
        } else {
          Log.trace("MarketSummaryWebSocket: sendList = false");
        }
      }, 1, SCHEDULER_PERIOD, TimeUnit.SECONDS);
    }

    private JsonObject mergeJsonObjects(JsonObject obj1, JsonObject obj2) {
        JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();
        
        Set<String> keys1 = obj1.keySet();
        Iterator<String> iter1 = keys1.iterator();
        
        while(iter1.hasNext()) {
            String key = (String)iter1.next();
            JsonValue value = obj1.get(key);
            
            jObjectBuilder.add(key, value);
            
        }
        
        Set<String> keys2 = obj2.keySet();
        Iterator<String> iter2 = keys2.iterator();
        
        while(iter2.hasNext()) {
            String key = (String)iter2.next();
            JsonValue value = obj2.get(key);
            
            jObjectBuilder.add(key, value);
            
        }
        
        return jObjectBuilder.build();
    }
}
