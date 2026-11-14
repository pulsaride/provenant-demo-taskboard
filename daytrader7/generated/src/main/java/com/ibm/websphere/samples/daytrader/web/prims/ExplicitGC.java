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

@WebServlet(name = "ExplicitGC", urlPatterns = { "/servlet/ExplicitGC" })
public class ExplicitGC extends HttpServlet {

    private static final long serialVersionUID = -3758934393801102408L;

    private static String initTime;

    private static int hitCount;

    // TODO(body: retention_calls_stubbed_member:doGet)
    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        throw new provenant.engine.EngineStubException("body not extracted for doPost — see lower::lir's module docs");
    }

    // TODO(body: retention_unresolved_type_name)
    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        throw new provenant.engine.EngineStubException("body not extracted for doGet — see lower::lir's module docs");
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "Generate Explicit GC to VM";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTime = new java.util.Date().toString();
        hitCount = 0;
    }
}
