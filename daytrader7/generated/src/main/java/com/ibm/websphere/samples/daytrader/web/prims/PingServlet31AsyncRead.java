package com.ibm.websphere.samples.daytrader.web.prims;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PingServlet31AsyncRead", urlPatterns = { "/servlet/PingServlet31AsyncRead" }, asyncSupported = true)
public class PingServlet31AsyncRead extends HttpServlet {

    private static final long serialVersionUID = 8731300373855056660L;

    private static String initTime;

    private static int hitCount;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        AsyncContext ac = req.startAsync();
        ServletInputStream input = req.getInputStream();
        ReadListener readListener = new ReadListenerImpl(input, res, ac);
        input.setReadListener(readListener);
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

    public class ReadListenerImpl implements ReadListener {

        ReadListenerImpl(ServletInputStream in, HttpServletResponse r, AsyncContext c) {
            input = in;
            res = r;
            ac = c;
        }
        private ServletInputStream input = null;
        private HttpServletResponse res = null;
        private AsyncContext ac = null;
        // TODO(initializer: unsupported_form)
        private StringBuilder sb;

        // TODO(body: retention_shadowed_type_name)
        public void onDataAvailable() throws IOException {
            throw new provenant.engine.EngineStubException("body not extracted for onDataAvailable — see lower::lir's module docs");
        }

        // TODO(body: retention_shadowed_type_name)
        public void onAllDataRead() throws IOException {
            throw new provenant.engine.EngineStubException("body not extracted for onAllDataRead — see lower::lir's module docs");
        }

        public void onError(Throwable t) {
            ac.complete();
            t.printStackTrace();
        }
    }
}
