package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "PingSession2", urlPatterns = { "/servlet/PingSession2" })
public class PingSession2 extends HttpServlet {

    private static final long serialVersionUID = -273579463475455800L;

    private static String initTime;

    private static int hitCount;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = null;
        try {
            try {
                session = request.getSession(true);
            } catch (Exception e) {
                Log.error(e, "PingSession2.doGet(...): error getting session");
                // rethrow the exception for handling in one place.
                throw e;

            }

            // Get the session data value
            Integer ival = (Integer) session.getAttribute("sessiontest.counter");
            // if there is not a counter then create one.
            if (ival == null) {
                ival = new Integer(1);
            } else {
                ival = new Integer(ival.intValue() + 1);
            }
            session.setAttribute("sessiontest.counter", ival);
            // if the session count is equal to five invalidate the session
            if (ival.intValue() == 5) {
                session.invalidate();
            }

            try {
                // Output the page
                response.setContentType("text/html");
                response.setHeader("SessionTrackingTest-counter", ival.toString());

                PrintWriter out = response.getWriter();
                out.println("<html><head><title>Session Tracking Test 2</title></head><body><HR><BR><FONT size=\"+2\" color=\"#000066\">HTTP Session Test 2: Session create/invalidate <BR></FONT><FONT size=\"+1\" color=\"#000066\">Init time: "
                        + initTime + "</FONT><BR><BR>");
                hitCount++;
                out.println("<B>Hit Count: " + hitCount + "<BR>Session hits: " + ival + "</B></body></html>");
            } catch (Exception e) {
                Log.error(e, "PingSession2.doGet(...): error getting session information");
                // rethrow the exception for handling in one place.
                throw e;
            }

        }

        catch (Exception e) {
            // log the excecption
            Log.error(e, "PingSession2.doGet(...): error.");
            // set the server responce to 500 and forward to the web app defined
            // error page
            response.sendError(500, "PingSession2.doGet(...): error. " + e.toString());
        }
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "HTTP Session Key: Tests management of a read/write unique id";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        hitCount = 0;
        initTime = new java.util.Date().toString();
    }
}
