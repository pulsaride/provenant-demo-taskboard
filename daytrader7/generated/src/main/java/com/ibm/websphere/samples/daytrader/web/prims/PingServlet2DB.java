package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PingServlet2DB", urlPatterns = { "/servlet/PingServlet2DB" })
public class PingServlet2DB extends HttpServlet {

    private static final long serialVersionUID = -6456675185605592049L;

    private static String initTime;

    private static int hitCount;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        java.io.PrintWriter out = res.getWriter();
        String symbol = null;
        StringBuffer output = new StringBuffer(100);

        try {
            // TradeJDBC uses prepared statements so I am going to make use of
            // it's code.
            TradeDirect trade = new TradeDirect();
            trade.getConnPublic();

            output.append("<html><head><title>PingServlet2DB.</title></head>"
                    + "<body><HR><FONT size=\"+2\" color=\"#000066\">PingServlet2DB:</FONT><HR><FONT size=\"-1\" color=\"#000066\">Init time : " + initTime);
            hitCount++;
            output.append("<BR>Hit Count: " + hitCount);
            output.append("<HR></body></html>");
            out.println(output.toString());
        } catch (Exception e) {
            Log.error(e, "PingServlet2DB -- error getting connection to the database", symbol);
            res.sendError(500, "PingServlet2DB Exception caught: " + e.toString());
        }
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "Basic JDBC Read using a prepared statment, makes use of TradeJDBC class";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        hitCount = 0;
        initTime = new java.util.Date().toString();
    }
}
