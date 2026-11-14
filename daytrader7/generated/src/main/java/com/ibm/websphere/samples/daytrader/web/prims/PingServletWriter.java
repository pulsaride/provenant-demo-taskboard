package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PingServletWriter", urlPatterns = { "/servlet/PingServletWriter" })
public class PingServletWriter extends HttpServlet {

    private static final long serialVersionUID = -267847365014523225L;

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

            // The following 2 lines are the difference between PingServlet and
            // PingServletWriter
            // the latter uses a PrintWriter for output versus a binary output
            // stream.
            // ServletOutputStream out = res.getOutputStream();
            java.io.PrintWriter out = res.getWriter();
            hitCount++;
            out.println("<html><head><title>Ping Servlet Writer</title></head>"
                    + "<body><HR><BR><FONT size=\"+2\" color=\"#000066\">Ping Servlet Writer:<BR></FONT><FONT size=\"+1\" color=\"#000066\">Init time : "
                    + initTime + "<BR><BR></FONT>  <B>Hit Count: " + hitCount + "</B></body></html>");
        } catch (Exception e) {
            Log.error(e, "PingServletWriter.doGet(...): general exception caught");
            res.sendError(500, e.toString());
        }
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "Basic dynamic HTML generation through a servlet using a PrintWriter";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        hitCount = 0;
        initTime = new java.util.Date().toString();
    }
}
