package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PingServlet2Servlet", urlPatterns = { "/servlet/PingServlet2Servlet" })
public class PingServlet2Servlet extends HttpServlet {

    private static final long serialVersionUID = -955942781902636048L;

    private static int hitCount = 0;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PingBean ab;
        try {
            ab = new PingBean();
            hitCount++;
            ab.setMsg("Hit Count: " + hitCount);
            req.setAttribute("ab", ab);

            getServletConfig().getServletContext().getRequestDispatcher("/servlet/PingServlet2ServletRcv").forward(req, res);
        } catch (Exception ex) {
            Log.error(ex, "PingServlet2Servlet.doGet(...): general exception");
            res.sendError(500, "PingServlet2Servlet.doGet(...): general exception" + ex.toString());

        }
    }
}
