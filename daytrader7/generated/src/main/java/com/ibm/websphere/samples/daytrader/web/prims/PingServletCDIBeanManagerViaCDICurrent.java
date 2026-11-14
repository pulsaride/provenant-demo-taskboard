package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.web.prims.PingCDIBean;
import jakarta.inject.Inject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// TODO(constructor_injection: ctor_injection_inheritance)
@WebServlet(value = "/servlet/PingServletCDIBeanManagerViaCDICurrent")
public class PingServletCDIBeanManagerViaCDICurrent extends HttpServlet {

    private static final long serialVersionUID = -1803544618879689949L;

    private static String initTime;

    @Inject
    PingCDIBean cdiBean;

    // TODO(body: retention_calls_stubbed_member:getBeanMangerViaCDICurrent)
    // TODO(annotation: Override)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        throw new provenant.engine.EngineStubException("body not extracted for doGet — see lower::lir's module docs");
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTime = new java.util.Date().toString();
    }
}
