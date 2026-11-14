package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.WebConnection;
import java.io.IOException;

@WebServlet(name = "PingUpgradeServlet", urlPatterns = { "/servlet/PingUpgradeServlet" }, asyncSupported = true)
public class PingUpgradeServlet extends HttpServlet {

    private static final long serialVersionUID = -6955518532146927509L;

    // TODO(annotation: Override)
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req,res);
    }

    // TODO(annotation: Override)
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (Log.doTrace()) {
            Log.trace("PingUpgradeServlet:doPost");
        }
        
        if ("echo".equals(req.getHeader("Upgrade"))) {
            
            if (Log.doTrace()) {
                Log.trace("PingUpgradeServlet:doPost -- found echo, doing upgrade");
            }
            
            res.setStatus(101);
            res.setHeader("Upgrade", "echo");
            res.setHeader("Connection", "Upgrade");

            req.upgrade(Handler.class);          
          
        } else {
            
            if (Log.doTrace()) {
                Log.trace("PingUpgradeServlet:doPost -- did not find echo, no upgrade");
            }
            
            res.getWriter().println("No upgrade: " + req.getHeader("Upgrade"));
        }
    }

    public static class Handler implements HttpUpgradeHandler {

        // TODO(annotation: Override)
        // TODO(body: retention_shadowed_type_name)
        public void init(WebConnection wc) {
            throw new provenant.engine.EngineStubException("body not extracted for init — see lower::lir's module docs");
        }

        // TODO(annotation: Override)
        public void destroy() {
            if (Log.doTrace()) {
                Log.trace("PingUpgradeServlet$Handler.destroy() -- Destroying Handler");
            }
        }
    }

    public static class Listener implements ReadListener {

        private Listener(WebConnection connection) throws IOException {
            this.connection = connection;
            this.input = connection.getInputStream();
            this.output = connection.getOutputStream();
        }
        private WebConnection connection;
        private ServletInputStream input = null;
        private ServletOutputStream output = null;

        // TODO(annotation: Override)
        // TODO(body: retention_shadowed_type_name)
        public void onDataAvailable() throws IOException {
            throw new provenant.engine.EngineStubException("body not extracted for onDataAvailable — see lower::lir's module docs");
        }

        // TODO(body: retention_shadowed_type_name)
        private void closeConnection() {
            throw new provenant.engine.EngineStubException("body not extracted for closeConnection — see lower::lir's module docs");
        }

        // TODO(annotation: Override)
        // TODO(body: retention_calls_stubbed_member:closeConnection)
        public void onAllDataRead() throws IOException {
            throw new provenant.engine.EngineStubException("body not extracted for onAllDataRead — see lower::lir's module docs");
        }

        // TODO(annotation: Override)
        // TODO(body: retention_calls_stubbed_member:closeConnection)
        public void onError(Throwable t) {
            throw new provenant.engine.EngineStubException("body not extracted for onError — see lower::lir's module docs");
        }
    }
}
