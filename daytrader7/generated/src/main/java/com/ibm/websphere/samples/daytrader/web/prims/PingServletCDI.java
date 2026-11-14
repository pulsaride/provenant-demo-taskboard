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
@WebServlet(value = "/servlet/PingServletCDI")
public class PingServletCDI extends HttpServlet {

    private static final long serialVersionUID = -1803544618879689949L;

    private static String initTime;

    @Inject
    PingCDIBean cdiBean;

    // TODO(annotation: EJB, ejb_not_mapped)
    PingEJBIFace ejb;

    // TODO(annotation: Override)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        pw.write((("<html><head><title>Ping Servlet CDI</title></head>" + "<body><HR><BR><FONT size=\"+2\" color=\"#000066\">Ping Servlet CDI<BR></FONT><FONT size=\"+1\" color=\"#000066\">Init time : ") + initTime) + "<BR><BR></FONT>");
        pw.write(("<B>hitCount: " + cdiBean.hello()) + "</B><BR>");
        pw.write(("<B>hitCount: " + ejb.getMsg()) + "</B><BR>");
        pw.flush();
        pw.close();
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTime = new java.util.Date().toString();
    }
}
