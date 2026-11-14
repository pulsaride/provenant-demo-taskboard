package com.ibm.websphere.samples.daytrader.web.prims;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PingServlet2IncludeRcv", urlPatterns = { "/servlet/PingServlet2IncludeRcv" })
public class PingServlet2IncludeRcv extends HttpServlet {

    private static final long serialVersionUID = 2628801298561220872L;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    }
}
