package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PingJDBCRead2JSP", urlPatterns = { "/servlet/PingJDBCRead2JSP" })
public class PingJDBCRead2JSP extends HttpServlet {

    private static final long serialVersionUID = 1118803761565654806L;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String symbol = null;
        QuoteDataBean quoteData = null;
        ServletContext ctx = getServletConfig().getServletContext();

        try {
            // TradeJDBC uses prepared statements so I am going to make use of
            // it's code.
            TradeDirect trade = new TradeDirect();
            symbol = TradeConfig.rndSymbol();

            int iter = TradeConfig.getPrimIterations();
            for (int ii = 0; ii < iter; ii++) {
                quoteData = trade.getQuote(symbol);
            }

            req.setAttribute("quoteData", quoteData);
            // req.setAttribute("hitCount", hitCount);
            // req.setAttribute("initTime", initTime);

            ctx.getRequestDispatcher("/quoteDataPrimitive.jsp").include(req, res);
        } catch (Exception e) {
            Log.error(e, "PingJDBCRead2JPS -- error getting quote for symbol", symbol);
            res.sendError(500, "PingJDBCRead2JSP Exception caught: " + e.toString());
        }
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "Basic JDBC Read using a prepared statment forwarded to a JSP, makes use of TradeJDBC class";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // hitCount = 0;
        // initTime = new java.util.Date().toString();
    }
}
