package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PingServletSetContentLength", urlPatterns = { "/servlet/PingServletSetContentLength" })
public class PingServletSetContentLength extends HttpServlet {

    private static final long serialVersionUID = 8731300373855056661L;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            res.setContentType("text/html");
            String lengthParam = req.getParameter("contentLength");
            Integer length;

            if (lengthParam == null) {
                length = 0;
            } else {
                length = Integer.parseInt(lengthParam);
            }

            ServletOutputStream out = res.getOutputStream();

            // Add characters (a's) to the SOS to equal the requested length
            // 167 is the smallest length possible.
            
            int i = 0;
            String buffer = "";

            while (i + 167 < length) {
                buffer = buffer + "a";
                i++;
            }

            out.println("<html><head><title>Ping Servlet</title></head>"
                    + "<body><HR><BR><FONT size=\"+2\" color=\"#000066\">Ping Servlet<BR></FONT><FONT size=\"+1\" color=\"#000066\">" + buffer
                    + "</B></body></html>");
        } catch (Exception e) {
            Log.error(e, "PingServlet.doGet(...): general exception caught");
            res.sendError(500, e.toString());

        }
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "Basic dynamic HTML generation through a servlet, with " + "contentLength set by contentLength parameter.";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
}
