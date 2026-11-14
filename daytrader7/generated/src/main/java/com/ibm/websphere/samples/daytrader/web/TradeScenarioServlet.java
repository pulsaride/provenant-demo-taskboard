package com.ibm.websphere.samples.daytrader.web;

import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

@WebServlet(name = "TradeScenarioServlet", urlPatterns = { "/scenario" })
public class TradeScenarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1410005249314201829L;

    private static final String tasPathPrefix = "/app?action=";

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        java.util.Enumeration<String> en = config.getInitParameterNames();
        while (en.hasMoreElements()) {
            String parm = en.nextElement();
            String value = config.getInitParameter(parm);
            TradeConfig.setConfigParam(parm, value);
        }
    }

    // TODO(annotation: Override)
    public java.lang.String getServletInfo() {
        return "TradeScenarioServlet emulates a population of web users";
    }

    // TODO(annotation: Override)
    public void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        performTask(request, response);
    }

    // TODO(annotation: Override)
    public void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        performTask(request, response);
    }

    public void performTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Scenario generator for Trade2
        char action = ' ';
        String userID = null;

        // String to create full dispatch path to TradeAppServlet w/ request
        // Parameters
        String dispPath = null; // Dispatch Path to TradeAppServlet

        resp.setContentType("text/html");

        String scenarioAction = req.getParameter("action");
        if ((scenarioAction != null) && (scenarioAction.length() >= 1)) {
            action = scenarioAction.charAt(0);
            if (action == 'n') { // null;
                try {
                    // resp.setContentType("text/html");
                    PrintWriter out = new PrintWriter(resp.getOutputStream());
                    out.println("<HTML><HEAD>TradeScenarioServlet</HEAD><BODY>Hello</BODY></HTML>");
                    out.close();
                    return;

                } catch (Exception e) {
                    Log.error("trade_client.TradeScenarioServlet.service(...)" + "error creating printwriter from responce.getOutputStream", e);

                    resp.sendError(500,
                            "trade_client.TradeScenarioServlet.service(...): erorr creating and writing to PrintStream created from response.getOutputStream()");
                } // end of catch

            } // end of action=='n'
        }

        ServletContext ctx = null;
        HttpSession session = null;
        try {
            ctx = getServletConfig().getServletContext();
            // These operations require the user to be logged in. Verify the
            // user and if not logged in
            // change the operation to a login
            session = req.getSession(true);
            userID = (String) session.getAttribute("uidBean");
        } catch (Exception e) {
            Log.error("trade_client.TradeScenarioServlet.service(...): performing " + scenarioAction
                    + "error getting ServletContext,HttpSession, or UserID from session" + "will make scenarioAction a login and try to recover from there", e);
            userID = null;
            action = 'l';
        }

        if (userID == null) {
            action = 'l'; // change to login
            TradeConfig.incrementScenarioCount();
        } else if (action == ' ') {
            // action is not specified perform a random operation according to
            // current mix
            // Tell getScenarioAction if we are an original user or a registered
            // user
            // -- sellDeficits should only be compensated for with original
            // users.
            action = TradeConfig.getScenarioAction(userID.startsWith(TradeConfig.newUserPrefix));
        }
        switch (action) {

        case 'q': // quote
            dispPath = tasPathPrefix + "quotes&symbols=" + TradeConfig.rndSymbols();
            ctx.getRequestDispatcher(dispPath).include(req, resp);
            break;
        case 'a': // account
            dispPath = tasPathPrefix + "account";
            ctx.getRequestDispatcher(dispPath).include(req, resp);
            break;
        case 'u': // update account profile
            dispPath = tasPathPrefix + "account";
            ctx.getRequestDispatcher(dispPath).include(req, resp);

            String fullName = "rnd" + System.currentTimeMillis();
            String address = "rndAddress";
            String password = "xxx";
            String email = "rndEmail";
            String creditcard = "rndCC";
            dispPath = tasPathPrefix + "update_profile&fullname=" + fullName + "&password=" + password + "&cpassword=" + password + "&address=" + address
                    + "&email=" + email + "&creditcard=" + creditcard;
            ctx.getRequestDispatcher(dispPath).include(req, resp);
            break;
        case 'h': // home
            dispPath = tasPathPrefix + "home";
            ctx.getRequestDispatcher(dispPath).include(req, resp);
            break;
        case 'l': // login
            userID = TradeConfig.getUserID();
            String password2 = "xxx";
            dispPath = tasPathPrefix + "login&inScenario=true&uid=" + userID + "&passwd=" + password2;
            ctx.getRequestDispatcher(dispPath).include(req, resp);

            // login is successful if the userID is written to the HTTP session
            if (session.getAttribute("uidBean") == null) {
                System.out.println("TradeScenario login failed. Reset DB between runs");
            }
            break;
        case 'o': // logout
            dispPath = tasPathPrefix + "logout";
            ctx.getRequestDispatcher(dispPath).include(req, resp);
            break;
        case 'p': // portfolio
            dispPath = tasPathPrefix + "portfolio";
            ctx.getRequestDispatcher(dispPath).include(req, resp);
            break;
        case 'r': // register
            // Logout the current user to become a new user
            // see note in TradeServletAction
            req.setAttribute("TSS-RecreateSessionInLogout", Boolean.TRUE);
            dispPath = tasPathPrefix + "logout";
            ctx.getRequestDispatcher(dispPath).include(req, resp);

            userID = TradeConfig.rndNewUserID();
            String passwd = "yyy";
            fullName = TradeConfig.rndFullName();
            creditcard = TradeConfig.rndCreditCard();
            String money = TradeConfig.rndBalance();
            email = TradeConfig.rndEmail(userID);
            String smail = TradeConfig.rndAddress();
            dispPath = tasPathPrefix + "register&Full Name=" + fullName + "&snail mail=" + smail + "&email=" + email + "&user id=" + userID + "&passwd="
                    + passwd + "&confirm passwd=" + passwd + "&money=" + money + "&Credit Card Number=" + creditcard;
            ctx.getRequestDispatcher(dispPath).include(req, resp);
            break;
        case 's': // sell
            dispPath = tasPathPrefix + "portfolioNoEdge";
            ctx.getRequestDispatcher(dispPath).include(req, resp);

            Collection<?> holdings = (Collection<?>) req.getAttribute("holdingDataBeans");
            int numHoldings = holdings.size();
            if (numHoldings > 0) {
                // sell first available security out of holding

                Iterator<?> it = holdings.iterator();
                boolean foundHoldingToSell = false;
                while (it.hasNext()) {
                    HoldingDataBean holdingData = (HoldingDataBean) it.next();
                    if (!(holdingData.getPurchaseDate().equals(new java.util.Date(0)))) {
                        Integer holdingID = holdingData.getHoldingID();

                        dispPath = tasPathPrefix + "sell&holdingID=" + holdingID;
                        ctx.getRequestDispatcher(dispPath).include(req, resp);
                        foundHoldingToSell = true;
                        break;
                    }
                }
                if (foundHoldingToSell) {
                    break;
                }
                if (Log.doTrace()) {
                    Log.trace("TradeScenario: No holding to sell -switch to buy -- userID = " + userID + "  Collection count = " + numHoldings);
                }

            }
            // At this point: A TradeScenario Sell was requested with No Stocks
            // in Portfolio
            // This can happen when a new registered user happens to request a
            // sell before a buy
            // In this case, fall through and perform a buy instead

            /*
             * Trade 2.037: Added sell_deficit counter to maintain correct
             * buy/sell mix. When a users portfolio is reduced to 0 holdings, a
             * buy is requested instead of a sell. This throws off the buy/sell
             * mix by 1. This results in unwanted holding table growth To fix
             * this we increment a sell deficit counter to maintain the correct
             * ratio in getScenarioAction The 'z' action from getScenario
             * denotes that this is a sell action that was switched from a buy
             * to reduce a sellDeficit
             */
            if (userID.startsWith(TradeConfig.newUserPrefix) == false) {
                TradeConfig.incrementSellDeficit();
            }
        case 'b': // buy
            String symbol = TradeConfig.rndSymbol();
            String amount = TradeConfig.rndQuantity() + "";

            dispPath = tasPathPrefix + "quotes&symbols=" + symbol;
            ctx.getRequestDispatcher(dispPath).include(req, resp);

            dispPath = tasPathPrefix + "buy&quantity=" + amount + "&symbol=" + symbol;
            ctx.getRequestDispatcher(dispPath).include(req, resp);
            break;
        } // end of switch statement
    }
}
