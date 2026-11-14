package com.ibm.websphere.samples.daytrader.web.prims.ejb3;

import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.annotation.Resource;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.TextMessage;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// TODO(field: jndi_resource_not_carried: tradeStreamerTopic)
@WebServlet(name = "ejb3.PingServlet2MDBTopic", urlPatterns = { "/ejb3/PingServlet2MDBTopic" })
public class PingServlet2MDBTopic extends HttpServlet {

    private static final long serialVersionUID = 5925470158886928225L;

    private static String initTime;

    private static int hitCount;

    @Resource(name = "jms/TopicConnectionFactory")
    private ConnectionFactory topicConnectionFactory;

    // TODO(body: retention_calls_stubbed_member:doGet)
    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        throw new provenant.engine.EngineStubException("body not extracted for doPost — see lower::lir's module docs");
    }

    // TODO(body: jms_classic_api_not_carried)
    // TODO(annotation: Override)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        throw new provenant.engine.EngineStubException("body not extracted for doGet — see lower::lir's module docs");
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "web primitive, configured with trade runtime configs, tests Servlet to Session EJB path";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        hitCount = 0;
        initTime = new java.util.Date().toString();
    }
}
