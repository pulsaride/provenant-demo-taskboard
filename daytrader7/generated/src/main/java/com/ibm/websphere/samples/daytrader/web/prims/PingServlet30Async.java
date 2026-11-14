package com.ibm.websphere.samples.daytrader.web.prims;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PingServlet30Async", urlPatterns = { "/servlet/PingServlet30Async" }, asyncSupported = true)
public class PingServlet30Async extends HttpServlet {

    private static final long serialVersionUID = 8731300373855056660L;

    private static String initTime;

    private static int hitCount;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
                        
        AsyncContext ac = req.startAsync();
        StringBuilder sb = new StringBuilder();
        
        ServletInputStream input = req.getInputStream();
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = input.read(b)) != -1) {
            String data = new String(b, 0, len);
            sb.append(data);
        }

        ServletOutputStream output = res.getOutputStream();
        
        output.println("<html><head><title>Ping Servlet 3.0 Async</title></head>"
                + "<body><hr/><br/><font size=\"+2\" color=\"#000066\">Ping Servlet 3.0 Async</font><br/>"
                + "<font size=\"+1\" color=\"#000066\">Init time : " + initTime
                + "</font><br/><br/><b>Hit Count: " + ++hitCount + "</b><br/>Data Received: "+ sb.toString() + "</body></html>");
        
        ac.complete();
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req,res);
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "Basic dynamic HTML generation through a servlet";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTime = new java.util.Date().toString();
        hitCount = 0;
    }
}
