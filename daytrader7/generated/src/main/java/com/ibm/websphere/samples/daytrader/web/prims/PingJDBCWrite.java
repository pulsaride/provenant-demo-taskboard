package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "PingJDBCWrite", urlPatterns = { "/servlet/PingJDBCWrite" })
public class PingJDBCWrite extends HttpServlet {

    private static final long serialVersionUID = -4938035109655376503L;

    private static String initTime;

    private static int hitCount;

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String symbol = null;
        BigDecimal newPrice;
        StringBuffer output = new StringBuffer(100);
        res.setContentType("text/html");
        java.io.PrintWriter out = res.getWriter();

        try {
            // get a random symbol to update and a random price.
            symbol = TradeConfig.rndSymbol();
            newPrice = TradeConfig.getRandomPriceChangeFactor();

            // TradeJDBC makes use of prepared statements so I am going to reuse
            // the existing code.
            TradeDirect trade = new TradeDirect();

            // update the price of our symbol
            QuoteDataBean quoteData = null;
            int iter = TradeConfig.getPrimIterations();
            for (int ii = 0; ii < iter; ii++) {
                quoteData = trade.updateQuotePriceVolumeInt(symbol, newPrice, 100.0, false);
            }

            // write the output
            output.append("<html><head><title>Ping JDBC Write w/ Prepared Stmt.</title></head>"
                    + "<body><HR><FONT size=\"+2\" color=\"#000066\">Ping JDBC Write w/ Prep Stmt:</FONT><FONT size=\"-1\" color=\"#000066\"><HR>Init time : "
                    + initTime);
            hitCount++;
            output.append("<BR>Hit Count: " + hitCount);
            output.append("<HR>Update Information<BR>");
            output.append("<BR>" + quoteData.toHTML() + "<HR></FONT></BODY></HTML>");
            out.println(output.toString());

        } catch (Exception e) {
            Log.error(e, "PingJDBCWrite -- error updating quote for symbol", symbol);
            res.sendError(500, "PingJDBCWrite Exception caught: " + e.toString());
        }
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "Basic JDBC Write using a prepared statment makes use of TradeJDBC code.";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTime = new java.util.Date().toString();
        hitCount = 0;
    }

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
}
