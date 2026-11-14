package com.ibm.websphere.samples.daytrader.web;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.TradeServices;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

@WebFilter(filterName = "OrdersAlertFilter", urlPatterns = "/app")
public class OrdersAlertFilter implements Filter {

    public OrdersAlertFilter() {
        super();
    }

    private FilterConfig filterConfig = null;

    // TODO(annotation: Override)
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    // TODO(annotation: Override)
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (filterConfig == null) {
            return;
        }

        if (TradeConfig.getDisplayOrderAlerts() == true) {

            try {
                String action = req.getParameter("action");
                if (action != null) {
                    action = action.trim();
                    if ((action.length() > 0) && (!action.equals("logout"))) {
                        String userID;
                        if (action.equals("login")) {
                            userID = req.getParameter("uid");
                        } else {
                            userID = (String) ((HttpServletRequest) req).getSession().getAttribute("uidBean");
                        }

                        if ((userID != null) && (userID.trim().length() > 0)) {
                            TradeServices tAction = null;
                            tAction = new TradeAction();
                            Collection<?> closedOrders = tAction.getClosedOrders(userID);
                            if ((closedOrders != null) && (closedOrders.size() > 0)) {
                                req.setAttribute("closedOrders", closedOrders);
                            }
                            if (Log.doTrace()) {
                                Log.printCollection("OrderAlertFilter: userID=" + userID + " closedOrders=", closedOrders);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.error(e, "OrdersAlertFilter - Error checking for closedOrders");
            }
        }

        chain.doFilter(req, resp/* wrapper */);
    }

    // TODO(annotation: Override)
    public void destroy() {
        this.filterConfig = null;
    }
}
