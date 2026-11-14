package com.ibm.websphere.samples.daytrader.web;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "TestServlet", urlPatterns = { "/TestServlet" })
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = -2927579146688173127L;

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    // TODO(annotation: Override)
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        performTask(request, response);
    }

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        performTask(request, response);
    }

    public void performTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Log.debug("Enter TestServlet doGet");
            TradeConfig.runTimeMode = TradeConfig.DIRECT;
            for (int i = 0; i < 10; i++) {
                new TradeAction().createQuote("s:" + i, "Company " + i, new BigDecimal(i * 1.1));
            }
            /*
             *
             * AccountDataBean accountData = new TradeAction().register("user1",
             * "password", "fullname", "address", "email", "creditCard", new
             * BigDecimal(123.45), false);
             *
             * OrderDataBean orderData = new TradeAction().buy("user1", "s:1",
             * 100.0); orderData = new TradeAction().buy("user1", "s:2", 200.0);
             * Thread.sleep(5000); accountData = new
             * TradeAction().getAccountData("user1"); Collection
             * holdingDataBeans = new TradeAction().getHoldings("user1");
             * PrintWriter out = resp.getWriter();
             * resp.setContentType("text/html");
             * out.write("<HEAD></HEAD><BODY><BR><BR>");
             * out.write(accountData.toString());
             * Log.printCollection("user1 Holdings", holdingDataBeans);
             * ServletContext sc = getServletContext();
             * req.setAttribute("results", "Success");
             * req.setAttribute("accountData", accountData);
             * req.setAttribute("holdingDataBeans", holdingDataBeans);
             * getServletContext
             * ().getRequestDispatcher("/tradehome.jsp").include(req, resp);
             * out.write("<BR><BR>done.</BODY>");
             */
        } catch (Exception e) {
            Log.error("TestServletException", e);
        }
    }
}
