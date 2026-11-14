package com.ibm.websphere.samples.daytrader.web.websocket;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

public class JsonEncoder implements Encoder.Text<JsonMessage> {

    // TODO(annotation: Override)
    public void destroy() {
    }

    // TODO(annotation: Override)
    public void init(EndpointConfig ec) {
    }

    // TODO(annotation: Override)
    public String encode(JsonMessage message) throws EncodeException {
        JsonObject jsonObject = Json.createObjectBuilder().add("key", message.getKey()).add("value", message.getValue()).build();
        return jsonObject.toString();
    }
}
