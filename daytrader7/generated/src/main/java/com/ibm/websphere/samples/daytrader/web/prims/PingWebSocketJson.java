package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.web.websocket.JsonDecoder;
import com.ibm.websphere.samples.daytrader.web.websocket.JsonEncoder;
import com.ibm.websphere.samples.daytrader.web.websocket.JsonMessage;
import jakarta.enterprise.concurrent.ManagedThreadFactory;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@ServerEndpoint(value = "/pingWebSocketJson", encoders = JsonEncoder.class, decoders = JsonDecoder.class)
public class PingWebSocketJson {

    private Session currentSession = null;

    private Integer sentHitCount = null;

    private Integer receivedHitCount = null;

    @OnOpen
    public void onOpen(Session session, EndpointConfig ec) {
        currentSession = session;
        sentHitCount = 0;
        receivedHitCount = 0;
        
        
        InitialContext context;
        ManagedThreadFactory mtf = null;
        
        try {
            context = new InitialContext();
            mtf = (ManagedThreadFactory) context.lookup("java:comp/DefaultManagedThreadFactory");
        
        } catch (NamingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        Thread thread = mtf.newThread(new Runnable() {

            @Override
            public void run() {
                
                try {
                
                    Thread.sleep(500);
                    
                    while (currentSession.isOpen()) {
                        sentHitCount++;
                    
                        JsonMessage response = new JsonMessage();
                        response.setKey("sentHitCount");
                        response.setValue(sentHitCount.toString());
                        currentSession.getAsyncRemote().sendObject(response);

                        Thread.sleep(100);
                    }
                    
                           
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                
        });
        
        thread.start();
    }

    @OnMessage
    public void ping(JsonMessage message) throws IOException {
        receivedHitCount++;
        JsonMessage response = new JsonMessage();
        response.setKey("receivedHitCount");
        response.setValue(receivedHitCount.toString());
        currentSession.getAsyncRemote().sendObject(response);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
    }
}
