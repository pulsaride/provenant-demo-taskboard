package com.ibm.websphere.samples.daytrader.web.websocket;

import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.json.Json;
import jakarta.json.stream.JsonParser;
import java.io.StringReader;

public class ActionMessage {

    public ActionMessage() {
    }

    String decodedAction = null;

    public void doDecoding(String jsonText) {
        String keyName = null;
        try 
        {
            // JSON parse
            JsonParser parser = Json.createParser(new StringReader(jsonText));
            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch(event) {
                case KEY_NAME:
                    keyName=parser.getString();
                    break;
                case VALUE_STRING:
                    if (keyName != null && keyName.equals("action")) {
                        decodedAction=parser.getString();
                    }
                    break;
                default:
                    break;
                }
            }
        } catch (Exception e) {
            Log.error("ActionMessage:doDecoding(" + jsonText + ") --> failed", e);
        }
        
        if (Log.doTrace()) {
            if (decodedAction != null ) {
                Log.trace("ActionMessage:doDecoding -- decoded action -->" + decodedAction + "<--");
            } else {
                Log.trace("ActionMessage:doDecoding -- decoded action -->null<--");
            }
        }
    }

    public String getDecodedAction() {
        return decodedAction;
    }
}
