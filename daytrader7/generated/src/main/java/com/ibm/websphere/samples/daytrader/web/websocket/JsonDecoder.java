package com.ibm.websphere.samples.daytrader.web.websocket;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;
import java.io.StringReader;

public class JsonDecoder implements Decoder.Text<JsonMessage> {

    // TODO(annotation: Override)
    public void destroy() {
    }

    // TODO(annotation: Override)
    public void init(EndpointConfig ec) {
    }

    // TODO(annotation: Override)
    public JsonMessage decode(String json) throws DecodeException {
        JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
        JsonMessage message = new JsonMessage();
        message.setKey(jsonObject.getString("key"));
        message.setValue(jsonObject.getString("value"));
        return message;
    }

    // TODO(annotation: Override)
    public boolean willDecode(String json) {
        try {
            Json.createReader(new StringReader(json)).readObject();
            return true;
          } catch (Exception e) {
            return false;
          }
    }
}
