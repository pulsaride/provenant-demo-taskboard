package com.ibm.websphere.samples.daytrader.web.prims.ejb3;

import com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBRemote;
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

@WebServlet(name = "ejb3.PingServlet2SessionRemote", urlPatterns = { "/ejb3/PingServlet2SessionRemote" })
public class PingServlet2SessionRemote extends HttpServlet {

    private static final long serialVersionUID = -6328388347808212784L;

    private static String initTime;

    private static int hitCount;

    // TODO(annotation: EJB, ejb_not_mapped)
    private TradeSLSBRemote tradeSLSBRemote;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        java.io.PrintWriter out = res.getWriter();
        // use a stringbuffer to avoid concatenation of Strings
        StringBuffer output = new StringBuffer(100);
        output.append("<html><head><title>PingServlet2SessionRemote</title></head>" + "<body><HR><FONT size=\"+2\" color=\"#000066\">PingServlet2SessionRemote<BR></FONT>"
                + "<FONT size=\"-1\" color=\"#000066\">" + "Tests the basis path from a Servlet to a Session Bean.");

        try {

            try {
                // create three random numbers
                double rnd1 = Math.random() * 1000000;
                double rnd2 = Math.random() * 1000000;

                // use a function to do some work.
                double increase = 0.0;
                int iter = TradeConfig.getPrimIterations();
                for (int ii = 0; ii < iter; ii++) {
                    increase = tradeSLSBRemote.investmentReturn(rnd1, rnd2);
                }

                // write out the output
                output.append("<HR>initTime: " + initTime);
                output.append("<BR>Hit Count: " + hitCount++);
                output.append("<HR>Investment Return Information <BR><BR>investment: " + rnd1);
                output.append("<BR>current Value: " + rnd2);
                output.append("<BR>investment return " + increase + "<HR></FONT></BODY></HTML>");
                out.println(output.toString());

            } catch (Exception e) {
                Log.error("PingServlet2Session.doGet(...):exception calling trade.investmentReturn ");
                throw e;
            }
        } // this is where I actually handle the exceptions
        catch (Exception e) {
            Log.error(e, "PingServlet2Session.doGet(...): error");
            res.sendError(500, "PingServlet2Session.doGet(...): error, " + e.toString());

        }
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "web primitive, configured with trade runtime configs, tests Servlet to Session EJB path";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        hitCount = 0;
        initTime = new java.util.Date().toString();

        if (tradeSLSBRemote == null) {
            Log.error("PingServlet2Session:init - Injection of tradeSLSBRemote failed - performing JNDI lookup!");

            try {
                InitialContext context = new InitialContext();
                tradeSLSBRemote = (TradeSLSBRemote) context.lookup("java:comp/env/ejb/TradeSLSBBeanRemote");
            } catch (Exception ex) {
                Log.error("PingServlet2Session:init - Lookup of tradeSLSBRemote failed!!!");
                ex.printStackTrace();
            }
        }
    }
}
