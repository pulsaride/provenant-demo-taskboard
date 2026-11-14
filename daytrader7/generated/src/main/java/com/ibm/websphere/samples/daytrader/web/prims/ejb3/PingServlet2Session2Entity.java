package com.ibm.websphere.samples.daytrader.web.prims.ejb3;

import com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBBean;
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
import javax.naming.InitialContext;

@WebServlet(name = "ejb3.PingServlet2Session2Entity", urlPatterns = { "/ejb3/PingServlet2Session2Entity" })
public class PingServlet2Session2Entity extends HttpServlet {

    private static final long serialVersionUID = -5043457201022265012L;

    private static String initTime;

    private static int hitCount;

    // TODO(annotation: EJB, ejb_not_mapped)
    private TradeSLSBBean tradeSLSBLocal;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        java.io.PrintWriter out = res.getWriter();
        String symbol = null;
        QuoteDataBean quoteData = null;
        StringBuffer output = new StringBuffer(100);

        output.append("<html><head><title>PingServlet2Session2Entity</title></head>"
                + "<body><HR><FONT size=\"+2\" color=\"#000066\">PingServlet2Session2Entity<BR></FONT>" + "<FONT size=\"-1\" color=\"#000066\">"
                + "PingServlet2Session2Entity tests the common path of a Servlet calling a Session EJB " + "which in turn calls an Entity EJB.<BR>");

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
            } catch (Exception ne) {
                Log.error(ne, "PingServlet2Session2Entity.goGet(...): exception getting QuoteData through Trade");
                throw ne;
            }

            output.append("<HR>initTime: " + initTime).append("<BR>Hit Count: " + hitCount++);
            output.append("<HR>Quote Information<BR><BR>" + quoteData.toHTML());
            out.println(output.toString());

        } catch (Exception e) {
            Log.error(e, "PingServlet2Session2Entity.doGet(...): General Exception caught");
            res.sendError(500, "General Exception caught, " + e.toString());
        }
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "web primitive, tests Servlet to Session to Entity EJB path";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        hitCount = 0;
        initTime = new java.util.Date().toString();

        if (tradeSLSBLocal == null) {
            Log.error("PingServlet2Session2Entity:init - Injection of tradeSLSBLocal failed - performing JNDI lookup!");

            try {
                InitialContext context = new InitialContext();
                tradeSLSBLocal = (TradeSLSBBean) context.lookup("java:comp/env/ejb/TradeSLSBBean");
            } catch (Exception ex) {
                Log.error("PingServlet2Session2Entity:init - Lookup of tradeSLSBLocal failed!!!");
                ex.printStackTrace();
            }
        }
    }
}
