package com.ibm.websphere.samples.daytrader.web.prims;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/pingTextAsync")
public class PingWebSocketTextAsync {

    private Session currentSession = null;

    private Integer hitCount = null;

    @OnOpen
    public void onOpen(Session session, EndpointConfig ec) {
        currentSession = session;
        hitCount = 0;
    }

    @OnMessage
    public void ping(String text) {
        hitCount++;
        currentSession.getAsyncRemote().sendText(hitCount.toString());
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
    }
}
