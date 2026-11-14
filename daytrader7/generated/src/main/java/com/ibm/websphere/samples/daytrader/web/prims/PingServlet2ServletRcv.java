package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "PingServlet2ServletRcv", urlPatterns = { "/servlet/PingServlet2ServletRcv" })
public class PingServlet2ServletRcv extends HttpServlet {

    private static final long serialVersionUID = -5241563129216549706L;

    private static String initTime = null;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PingBean ab;
        try {
            ab = (PingBean) req.getAttribute("ab");
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<html><head><title>Ping Servlet2Servlet</title></head>"
                    + "<body><HR><BR><FONT size=\"+2\" color=\"#000066\">PingServlet2Servlet:<BR></FONT><FONT size=\"+1\" color=\"#000066\">Init time: "
                    + initTime + "</FONT><BR><BR><B>Message from Servlet: </B>" + ab.getMsg() + "</body></html>");
        } catch (Exception ex) {
            Log.error(ex, "PingServlet2ServletRcv.doGet(...): general exception");
            res.sendError(500, "PingServlet2ServletRcv.doGet(...): general exception" + ex.toString());
        }
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTime = new java.util.Date().toString();
    }
}
