package com.ibm.websphere.samples.daytrader.web.prims;

import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(asyncSupported = true, name = "PingManagedExecutor", urlPatterns = { "/servlet/PingManagedExecutor" })
public class PingManagedExecutor extends HttpServlet {

    private static final long serialVersionUID = -4695386150928451234L;

    private static String initTime;

    private static int hitCount;

    @Resource
    private ManagedExecutorService mes;

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
    		    		
    		out.println("<html><head><title>Ping ManagedExecutor</title></head>"
                    + "<body><HR><BR><FONT size=\"+2\" color=\"#000066\">Ping ManagedExecutor<BR></FONT><FONT size=\"+1\" color=\"#000066\">Init time : " + initTime
                    + "<BR><BR></FONT>  </body></html>");
    		   		   		    	
    		// Runnable task
    		mes.submit(new Runnable() {
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
    		
    			 
    	} catch (Exception e) {
			e.printStackTrace();
		}  
    }

    // TODO(annotation: Override)
    public String getServletInfo() {
        return "Tests a ManagedExecutor";
    }

    // TODO(annotation: Override)
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initTime = new java.util.Date().toString();
        hitCount = 0;
    }
}
