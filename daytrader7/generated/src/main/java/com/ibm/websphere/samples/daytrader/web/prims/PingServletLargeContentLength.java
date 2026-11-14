package com.ibm.websphere.samples.daytrader.web.prims;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PingServletLargeContentLength", urlPatterns = { "/servlet/PingServletLargeContentLength" })
public class PingServletLargeContentLength extends HttpServlet {

    private static final long serialVersionUID = -7979576220528252408L;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("Length: " + req.getContentLengthLong());
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
       doPost(req,res);    
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
