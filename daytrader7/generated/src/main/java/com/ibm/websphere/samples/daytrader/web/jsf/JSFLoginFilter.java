package com.ibm.websphere.samples.daytrader.web.jsf;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "JSFLoginFilter", urlPatterns = "*.faces")
public class JSFLoginFilter implements Filter {

    public JSFLoginFilter() {
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

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("uidBean");

        // If user has not logged in and is trying access account information,
        // redirect to login page.
        if (userID == null) {
            String url = request.getServletPath();

            if (url.contains("home") || url.contains("account") || url.contains("portfolio") || url.contains("quote") || url.contains("order")
                    || url.contains("marketSummary")) {
                System.out.println("JSF service error: User Not Logged in");
                response.sendRedirect("welcome.faces");
                return;
            }
        }

        chain.doFilter(req, resp/* wrapper */);
    }

    // TODO(annotation: Override)
    public void destroy() {
        this.filterConfig = null;
    }
}
