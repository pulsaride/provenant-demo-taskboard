package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@WebServlet(name = "PingJSONP", urlPatterns = { "/servlet/PingJSONP" })
public class PingJSONP extends HttpServlet {

    private static final long serialVersionUID = -5348806619121122708L;

    private static String initTime;

    private static int hitCount;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            res.setContentType("text/html");

            ServletOutputStream out = res.getOutputStream();
            
            hitCount++;
            
            // JSON generate
            StringWriter sw = new StringWriter();
            JsonGenerator generator = Json.createGenerator(sw);
             
            generator.writeStartObject();
            generator.write("initTime",initTime);
            generator.write("hitCount", hitCount);
            generator.writeEnd();
            generator.flush();
            
            String generatedJSON =  sw.toString();
            StringBuffer parsedJSON = new StringBuffer(); 
            
            // JSON parse
            JsonParser parser = Json.createParser(new StringReader(generatedJSON));
            while (parser.hasNext()) {
               JsonParser.Event event = parser.next();
               switch(event) {
                  case START_ARRAY:
                  case END_ARRAY:
                  case START_OBJECT:
                  case END_OBJECT:
                  case VALUE_FALSE:
                  case VALUE_NULL:
                  case VALUE_TRUE:
                     break;
                  case KEY_NAME:
                      parsedJSON.append(parser.getString() + ":");
                     break;
                  case VALUE_STRING:
                  case VALUE_NUMBER:
                      parsedJSON.append(parser.getString() + " ");
                     break;
               }
            }
            
            out.println("<html><head><title>Ping JSONP</title></head>"
                    + "<body><HR><BR><FONT size=\"+2\" color=\"#000066\">Ping JSONP</FONT><BR>Generated JSON: " + generatedJSON + "<br>Parsed JSON: " + parsedJSON + "</body></html>");
        } catch (Exception e) {
            Log.error(e, "PingJSONP.doGet(...): general exception caught");
            res.sendError(500, e.toString());

        }
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "Basic JSON generation and parsing in a servlet";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTime = new java.util.Date().toString();
        hitCount = 0;
    }
}
