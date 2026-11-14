package com.ibm.websphere.samples.daytrader.web.prims;

import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedThreadFactory;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(asyncSupported = true, name = "PingManagedThread", urlPatterns = { "/servlet/PingManagedThread" })
public class PingManagedThread extends HttpServlet {

    private static final long serialVersionUID = -4695386150928451234L;

    private static String initTime;

    private static int hitCount;

    @Resource
    private ManagedThreadFactory managedThreadFactory;

    // TODO(annotation: Override)
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    // TODO(annotation: Override)
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		final AsyncContext asyncContext = req.startAsync();
		final ServletOutputStream out = res.getOutputStream();	
		
		try {
			
			res.setContentType("text/html");
					
			out.println("<html><head><title>Ping ManagedThread</title></head>"
                    + "<body><HR><BR><FONT size=\"+2\" color=\"#000066\">Ping ManagedThread<BR></FONT><FONT size=\"+1\" color=\"#000066\">Init time : " + initTime + "<BR/><BR/></FONT>");			
		
			Thread thread = managedThreadFactory.newThread(new Runnable() {
    			@Override
    			public void run() {
    				try {
						out.println("<b>HitCount: " + ++hitCount  +"</b><br/>");
					} catch (IOException e) {
						e.printStackTrace();
					}
    				asyncContext.complete();
    			}
    		});   		    		
			
			thread.start();
		
		} catch (Exception e) {
			Log.error(e, "PingManagedThreadServlet.doGet(...): general exception caught");
			res.sendError(500, e.toString());
		}
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "Tests a ManagedThread asynchronous servlet";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTime = new java.util.Date().toString();
        hitCount = 0;
    }
}
