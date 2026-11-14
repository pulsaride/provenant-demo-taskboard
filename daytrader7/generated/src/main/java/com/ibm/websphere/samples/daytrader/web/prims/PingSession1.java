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

@WebServlet(name = "PingSession1", urlPatterns = { "/servlet/PingSession1" })
public class PingSession1 extends HttpServlet {

    private static final long serialVersionUID = -3703858656588519807L;

    private static int count;

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
                // get the users session, if the user does not have a session
                // create one.
                session = request.getSession(true);
            } catch (Exception e) {
                Log.error(e, "PingSession1.doGet(...): error getting session");
                // rethrow the exception for handling in one place.
                throw e;
            }

            // Get the session data value
            Integer ival = (Integer) session.getAttribute("sessiontest.counter");
            // if their is not a counter create one.
            if (ival == null) {
                ival = new Integer(count++);
                session.setAttribute("sessiontest.counter", ival);
            }
            String SessionID = "SessionID:" + ival.toString();

            // Output the page
            response.setContentType("text/html");
            response.setHeader("SessionKeyTest-SessionID", SessionID);

            PrintWriter out = response.getWriter();
            out.println("<html><head><title>HTTP Session Key Test</title></head><body><HR><BR><FONT size=\"+2\" color=\"#000066\">HTTP Session Test 1: Session Key<BR></FONT><FONT size=\"+1\" color=\"#000066\">Init time: "
                    + initTime + "</FONT><BR><BR>");
            hitCount++;
            out.println("<B>Hit Count: " + hitCount + "<BR>Your HTTP Session key is " + SessionID + "</B></body></html>");
        } catch (Exception e) {
            // log the excecption
            Log.error(e, "PingSession1.doGet(..l.): error.");
            // set the server responce to 500 and forward to the web app defined
            // error page
            response.sendError(500, "PingSession1.doGet(...): error. " + e.toString());
        }
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "HTTP Session Key: Tests management of a read only unique id";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        count = 0;
        hitCount = 0;
        initTime = new java.util.Date().toString();
    }
}
