package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PingServlet2Include", urlPatterns = { "/servlet/PingServlet2Include" })
public class PingServlet2Include extends HttpServlet {

    private static final long serialVersionUID = 1063447780151198793L;

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

            int iter = TradeConfig.getPrimIterations();
            for (int ii = 0; ii < iter; ii++) {
                getServletConfig().getServletContext().getRequestDispatcher("/servlet/PingServlet2IncludeRcv").include(req, res);
            }

            // ServletOutputStream out = res.getOutputStream();
            java.io.PrintWriter out = res.getWriter();
            out.println("<html><head><title>Ping Servlet 2 Include</title></head>"
                    + "<body><HR><BR><FONT size=\"+2\" color=\"#000066\">Ping Servlet 2 Include<BR></FONT><FONT size=\"+1\" color=\"#000066\">Init time : "
                    + initTime + "<BR><BR></FONT>  <B>Hit Count: " + hitCount++ + "</B></body></html>");
        } catch (Exception ex) {
            Log.error(ex, "PingServlet2Include.doGet(...): general exception");
            res.sendError(500, "PingServlet2Include.doGet(...): general exception" + ex.toString());
        }
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTime = new java.util.Date().toString();
        hitCount = 0;
    }
}
