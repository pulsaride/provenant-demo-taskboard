package com.ibm.websphere.samples.daytrader.web.websocket;

import jakarta.jms.Message;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RecentStockChangeList {

    private static List<Message> stockChanges = Collections.synchronizedList(new LinkedList<Message>());

    public static void addStockChange(Message message) {
        stockChanges.add(0, message);
        if (stockChanges.size() > 5) {
            stockChanges.remove(5);
        }
    }

    public static JsonObject stockChangesInJSON() {
        JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();
        
        try {
            int i = 1;
            
            List<Message> temp = new LinkedList<Message>(stockChanges);
                        
            for (Iterator<Message> iterator = temp.iterator(); iterator.hasNext();) {
                Message message = iterator.next();
                            
                jObjectBuilder.add("change" + i + "_stock", message.getStringProperty("symbol"));
                jObjectBuilder.add("change" + i + "_price","$" + message.getStringProperty("price"));          
                            
                BigDecimal change = new BigDecimal(message.getStringProperty("price")).subtract(new BigDecimal(message.getStringProperty("oldPrice")));
                change.setScale(2, RoundingMode.HALF_UP);
                
                jObjectBuilder.add("change" + i + "_change", change.toString());
                
                i++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return jObjectBuilder.build();
    }

    public static boolean isEmpty() {
        return stockChanges.isEmpty();
    }
}
