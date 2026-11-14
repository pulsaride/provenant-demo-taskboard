package com.ibm.websphere.samples.daytrader.web.prims.ejb3;

import com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBBean;
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
import javax.naming.InitialContext;

@WebServlet(name = "ejb3.PingServlet2Session2Entity2JSP", urlPatterns = { "/ejb3/PingServlet2Session2Entity2JSP" })
public class PingServlet2Session2Entity2JSP extends HttpServlet {

    private static final long serialVersionUID = -8966014710582651693L;

    // TODO(annotation: EJB, ejb_not_mapped)
    private TradeSLSBBean tradeSLSBLocal;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String symbol = null;
        QuoteDataBean quoteData = null;
        ServletContext ctx = getServletConfig().getServletContext();

        try {
            try {
                int iter = TradeConfig.getPrimIterations();
                for (int ii = 0; ii < iter; ii++) {
                    symbol = TradeConfig.rndSymbol();
                    // getQuote will call findQuote which will instaniate the
                    // Quote Entity Bean
                    // and then will return a QuoteObject
                    quoteData = tradeSLSBLocal.getQuote(symbol);
                }

                req.setAttribute("quoteData", quoteData);
                // req.setAttribute("hitCount", hitCount);
                // req.setAttribute("initTime", initTime);

                ctx.getRequestDispatcher("/quoteDataPrimitive.jsp").include(req, res);
            } catch (Exception ne) {
                Log.error(ne, "PingServlet2Session2Entity2JSP.goGet(...): exception getting QuoteData through Trade");
                throw ne;
            }

        } catch (Exception e) {
            Log.error(e, "PingServlet2Session2Entity2JSP.doGet(...): General Exception caught");
            res.sendError(500, "General Exception caught, " + e.toString());
        }
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "web primitive, tests Servlet to Session to Entity EJB to JSP path";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // hitCount = 0;
        // initTime = new java.util.Date().toString();

        if (tradeSLSBLocal == null) {
            Log.error("PingServlet2Session2Entity2JSP:init - Injection of tradeSLSBLocal failed - performing JNDI lookup!");

            try {
                InitialContext context = new InitialContext();
                tradeSLSBLocal = (TradeSLSBBean) context.lookup("java:comp/env/ejb/TradeSLSBBean");
            } catch (Exception ex) {
                Log.error("PingServlet2Session2EntityJSP:init - Lookup of tradeSLSBLocal failed!!!");
                ex.printStackTrace();
            }
        }
    }
}
