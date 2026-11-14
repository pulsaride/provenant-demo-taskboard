package com.ibm.websphere.samples.daytrader.web.websocket;

import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;

public class ActionDecoder implements Decoder.Text<ActionMessage> {

    public ActionDecoder() {
    }

    // TODO(annotation: Override)
    public void destroy() {
    }

    // TODO(annotation: Override)
    public void init(EndpointConfig config) {
    }

    // TODO(annotation: Override)
    public ActionMessage decode(String jsonText) throws DecodeException {
        if (Log.doTrace()) {
            Log.trace(("ActionDecoder:decode -- received -->" + jsonText) + "<--");
        }
        ActionMessage actionMessage = new ActionMessage();
        actionMessage.doDecoding(jsonText);
        return actionMessage;
    }

    // TODO(annotation: Override)
    public boolean willDecode(String s) {
        return true;
    }
}
