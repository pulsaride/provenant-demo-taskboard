package com.ibm.websphere.samples.daytrader;

import jakarta.servlet.DispatcherType;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import org.apache.catalina.WebResourceRoot;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers the legacy servlets, filters and context listeners on the embedded container.
 * Engine-authored; the registered set is exactly the admitted declarations.
 */
@Configuration
public class ServletRegistrations {

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingJDBCRead servlet_com_ibm_websphere_samples_daytrader_web_prims_PingJDBCRead() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingJDBCRead();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingJDBCRead> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingJDBCRead(com.ibm.websphere.samples.daytrader.web.prims.PingJDBCRead servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingJDBCRead> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingJDBCRead");
        registration.setName("PingJDBCRead");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingJDBCRead2JSP servlet_com_ibm_websphere_samples_daytrader_web_prims_PingJDBCRead2JSP() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingJDBCRead2JSP();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingJDBCRead2JSP> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingJDBCRead2JSP(com.ibm.websphere.samples.daytrader.web.prims.PingJDBCRead2JSP servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingJDBCRead2JSP> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingJDBCRead2JSP");
        registration.setName("PingJDBCRead2JSP");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingJDBCWrite servlet_com_ibm_websphere_samples_daytrader_web_prims_PingJDBCWrite() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingJDBCWrite();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingJDBCWrite> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingJDBCWrite(com.ibm.websphere.samples.daytrader.web.prims.PingJDBCWrite servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingJDBCWrite> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingJDBCWrite");
        registration.setName("PingJDBCWrite");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingJSONP servlet_com_ibm_websphere_samples_daytrader_web_prims_PingJSONP() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingJSONP();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingJSONP> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingJSONP(com.ibm.websphere.samples.daytrader.web.prims.PingJSONP servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingJSONP> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingJSONP");
        registration.setName("PingJSONP");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingManagedExecutor servlet_com_ibm_websphere_samples_daytrader_web_prims_PingManagedExecutor() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingManagedExecutor();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingManagedExecutor> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingManagedExecutor(com.ibm.websphere.samples.daytrader.web.prims.PingManagedExecutor servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingManagedExecutor> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingManagedExecutor");
        registration.setName("PingManagedExecutor");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(true);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingManagedThread servlet_com_ibm_websphere_samples_daytrader_web_prims_PingManagedThread() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingManagedThread();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingManagedThread> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingManagedThread(com.ibm.websphere.samples.daytrader.web.prims.PingManagedThread servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingManagedThread> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingManagedThread");
        registration.setName("PingManagedThread");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(true);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingReentryServlet servlet_com_ibm_websphere_samples_daytrader_web_prims_PingReentryServlet() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingReentryServlet();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingReentryServlet> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingReentryServlet(com.ibm.websphere.samples.daytrader.web.prims.PingReentryServlet servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingReentryServlet> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingReentryServlet");
        registration.setName("PingReentryServlet");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServlet servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServlet() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServlet();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServlet(com.ibm.websphere.samples.daytrader.web.prims.PingServlet servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServlet");
        registration.setName("PingServlet");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServlet2DB servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2DB() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServlet2DB();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2DB> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2DB(com.ibm.websphere.samples.daytrader.web.prims.PingServlet2DB servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2DB> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServlet2DB");
        registration.setName("PingServlet2DB");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServlet2Include servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2Include() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServlet2Include();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2Include> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2Include(com.ibm.websphere.samples.daytrader.web.prims.PingServlet2Include servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2Include> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServlet2Include");
        registration.setName("PingServlet2Include");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServlet2IncludeRcv servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2IncludeRcv() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServlet2IncludeRcv();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2IncludeRcv> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2IncludeRcv(com.ibm.websphere.samples.daytrader.web.prims.PingServlet2IncludeRcv servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2IncludeRcv> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServlet2IncludeRcv");
        registration.setName("PingServlet2IncludeRcv");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServlet2JNDI servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2JNDI() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServlet2JNDI();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2JNDI> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2JNDI(com.ibm.websphere.samples.daytrader.web.prims.PingServlet2JNDI servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2JNDI> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServlet2JNDI");
        registration.setName("PingServlet2JNDI");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServlet2Jsp servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2Jsp() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServlet2Jsp();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2Jsp> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2Jsp(com.ibm.websphere.samples.daytrader.web.prims.PingServlet2Jsp servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2Jsp> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServlet2Jsp");
        registration.setName("PingServlet2Jsp");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServlet2PDF servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2PDF() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServlet2PDF();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2PDF> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServlet2PDF(com.ibm.websphere.samples.daytrader.web.prims.PingServlet2PDF servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet2PDF> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServlet2PDF");
        registration.setName("PingServlet2PDF");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServlet30Async servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServlet30Async() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServlet30Async();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet30Async> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServlet30Async(com.ibm.websphere.samples.daytrader.web.prims.PingServlet30Async servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet30Async> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServlet30Async");
        registration.setName("PingServlet30Async");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(true);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServlet31Async servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServlet31Async() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServlet31Async();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet31Async> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServlet31Async(com.ibm.websphere.samples.daytrader.web.prims.PingServlet31Async servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet31Async> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServlet31Async");
        registration.setName("PingServlet31Async");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(true);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServlet31AsyncRead servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServlet31AsyncRead() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServlet31AsyncRead();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet31AsyncRead> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServlet31AsyncRead(com.ibm.websphere.samples.daytrader.web.prims.PingServlet31AsyncRead servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServlet31AsyncRead> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServlet31AsyncRead");
        registration.setName("PingServlet31AsyncRead");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(true);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServletLargeContentLength servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServletLargeContentLength() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServletLargeContentLength();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServletLargeContentLength> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServletLargeContentLength(com.ibm.websphere.samples.daytrader.web.prims.PingServletLargeContentLength servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServletLargeContentLength> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServletLargeContentLength");
        registration.setName("PingServletLargeContentLength");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServletSetContentLength servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServletSetContentLength() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServletSetContentLength();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServletSetContentLength> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServletSetContentLength(com.ibm.websphere.samples.daytrader.web.prims.PingServletSetContentLength servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServletSetContentLength> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServletSetContentLength");
        registration.setName("PingServletSetContentLength");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingServletWriter servlet_com_ibm_websphere_samples_daytrader_web_prims_PingServletWriter() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingServletWriter();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServletWriter> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingServletWriter(com.ibm.websphere.samples.daytrader.web.prims.PingServletWriter servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingServletWriter> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingServletWriter");
        registration.setName("PingServletWriter");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingSession1 servlet_com_ibm_websphere_samples_daytrader_web_prims_PingSession1() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingSession1();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingSession1> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingSession1(com.ibm.websphere.samples.daytrader.web.prims.PingSession1 servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingSession1> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingSession1");
        registration.setName("PingSession1");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingSession2 servlet_com_ibm_websphere_samples_daytrader_web_prims_PingSession2() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingSession2();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingSession2> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingSession2(com.ibm.websphere.samples.daytrader.web.prims.PingSession2 servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingSession2> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingSession2");
        registration.setName("PingSession2");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingSession3 servlet_com_ibm_websphere_samples_daytrader_web_prims_PingSession3() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingSession3();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingSession3> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingSession3(com.ibm.websphere.samples.daytrader.web.prims.PingSession3 servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingSession3> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingSession3");
        registration.setName("PingSession3");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.PingUpgradeServlet servlet_com_ibm_websphere_samples_daytrader_web_prims_PingUpgradeServlet() {
        return new com.ibm.websphere.samples.daytrader.web.prims.PingUpgradeServlet();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingUpgradeServlet> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_PingUpgradeServlet(com.ibm.websphere.samples.daytrader.web.prims.PingUpgradeServlet servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.PingUpgradeServlet> registration = new ServletRegistrationBean<>(servlet, false, "/servlet/PingUpgradeServlet");
        registration.setName("PingUpgradeServlet");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(true);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.TestServlet servlet_com_ibm_websphere_samples_daytrader_web_TestServlet() {
        return new com.ibm.websphere.samples.daytrader.web.TestServlet();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.TestServlet> servletRegistration_com_ibm_websphere_samples_daytrader_web_TestServlet(com.ibm.websphere.samples.daytrader.web.TestServlet servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.TestServlet> registration = new ServletRegistrationBean<>(servlet, false, "/TestServlet");
        registration.setName("TestServlet");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.TradeAppServlet servlet_com_ibm_websphere_samples_daytrader_web_TradeAppServlet() {
        return new com.ibm.websphere.samples.daytrader.web.TradeAppServlet();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.TradeAppServlet> servletRegistration_com_ibm_websphere_samples_daytrader_web_TradeAppServlet(com.ibm.websphere.samples.daytrader.web.TradeAppServlet servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.TradeAppServlet> registration = new ServletRegistrationBean<>(servlet, false, "/app");
        registration.setName("TradeAppServlet");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.TradeConfigServlet servlet_com_ibm_websphere_samples_daytrader_web_TradeConfigServlet() {
        return new com.ibm.websphere.samples.daytrader.web.TradeConfigServlet();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.TradeConfigServlet> servletRegistration_com_ibm_websphere_samples_daytrader_web_TradeConfigServlet(com.ibm.websphere.samples.daytrader.web.TradeConfigServlet servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.TradeConfigServlet> registration = new ServletRegistrationBean<>(servlet, false, "/config");
        registration.setName("TradeConfigServlet");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.prims.ejb3.PingServlet2SessionLocal servlet_com_ibm_websphere_samples_daytrader_web_prims_ejb3_PingServlet2SessionLocal() {
        return new com.ibm.websphere.samples.daytrader.web.prims.ejb3.PingServlet2SessionLocal();
    }

    @Bean
    public ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.ejb3.PingServlet2SessionLocal> servletRegistration_com_ibm_websphere_samples_daytrader_web_prims_ejb3_PingServlet2SessionLocal(com.ibm.websphere.samples.daytrader.web.prims.ejb3.PingServlet2SessionLocal servlet) {
        ServletRegistrationBean<com.ibm.websphere.samples.daytrader.web.prims.ejb3.PingServlet2SessionLocal> registration = new ServletRegistrationBean<>(servlet, false, "/ejb3/PingServlet2SessionLocal");
        registration.setName("ejb3.PingServlet2SessionLocal");
        registration.setLoadOnStartup(-1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public LegacyStaticContent legacyStaticContent() {
        return new LegacyStaticContent();
    }

    @Bean
    public ServletRegistrationBean<LegacyStaticContent> legacyStaticContentRegistration(LegacyStaticContent servlet) {
        ServletRegistrationBean<LegacyStaticContent> registration = new ServletRegistrationBean<>(servlet, false, "", "/PingHtml.html", "/PingWebSocketBinary.html", "/PingWebSocketJson.html", "/PingWebSocketTextAsync.html", "/PingWebSocketTextSync.html", "/configure.html", "/contentHome.html", "/docs/benchmarking.html", "/docs/documentation.html", "/docs/glossary.html", "/docs/rtCharacterisitics.html", "/docs/tradeFAQ.html", "/docs/tradeversion.html", "/favicon.ico", "/footer.html", "/header.html", "/images/DayTraderHead_blue.gif", "/images/DayTraderHead_red.gif", "/images/about.gif", "/images/account.gif", "/images/arrowdown.gif", "/images/arrowup.gif", "/images/bottomRedBar.gif", "/images/configuration.gif", "/images/dayTraderLogo.gif", "/images/daytrader_simple_arch.gif", "/images/faq.gif", "/images/graph.gif", "/images/home.gif", "/images/homeBanner.gif", "/images/line.gif", "/images/logout.gif", "/images/lower_banner.gif", "/images/menuHome.gif", "/images/nav_bg.png", "/images/portfolio.gif", "/images/primitives.gif", "/images/quotes.gif", "/images/reports.gif", "/images/spacer.gif", "/images/ticker-anim.gif", "/images/topRedBar.gif", "/images/topline.jpg", "/images/tradeOverview.png", "/images/tradingAndPortfolios.gif", "/index.html", "/leftMenu.html", "/marketSummary.html", "/style-jsf.css", "/style.css", "/web_prmtv.html");
        registration.setName("provenant.legacy-static");
        registration.setLoadOnStartup(-1);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.OrdersAlertFilter filter_com_ibm_websphere_samples_daytrader_web_OrdersAlertFilter() {
        return new com.ibm.websphere.samples.daytrader.web.OrdersAlertFilter();
    }

    @Bean
    public FilterRegistrationBean<com.ibm.websphere.samples.daytrader.web.OrdersAlertFilter> filterRegistration_com_ibm_websphere_samples_daytrader_web_OrdersAlertFilter(com.ibm.websphere.samples.daytrader.web.OrdersAlertFilter filter) {
        FilterRegistrationBean<com.ibm.websphere.samples.daytrader.web.OrdersAlertFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setName("OrdersAlertFilter");
        registration.setServletNames(List.of("TradeAppServlet"));
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST));
        registration.setOrder(1);
        registration.setAsyncSupported(false);
        return registration;
    }

    @Bean(destroyMethod = "")
    public com.ibm.websphere.samples.daytrader.web.TradeWebContextListener listener_com_ibm_websphere_samples_daytrader_web_TradeWebContextListener() {
        return new com.ibm.websphere.samples.daytrader.web.TradeWebContextListener();
    }

    @Bean
    public ServletListenerRegistrationBean<com.ibm.websphere.samples.daytrader.web.TradeWebContextListener> listenerRegistration_com_ibm_websphere_samples_daytrader_web_TradeWebContextListener(com.ibm.websphere.samples.daytrader.web.TradeWebContextListener listener) {
        return new ServletListenerRegistrationBean<>(listener);
    }

    private static final String[] WEB_RESOURCES = { "/dbscripts/db2/Table.ddl", "/dbscripts/db2i/Table.ddl", "/dbscripts/derby/Table.ddl", "/dbscripts/oracle/Table.ddl", "/properties/daytrader.properties" };

    private static final String[] WEB_PAGES = { "/PingJsp.jsp", "/PingServlet2Jsp.jsp", "/account.jsp", "/accountImg.jsp", "/config.jsp", "/displayQuote.jsp", "/marketSummary.jsp", "/order.jsp", "/orderImg.jsp", "/portfolio.jsp", "/portfolioImg.jsp", "/quote.jsp", "/quoteDataPrimitive.jsp", "/quoteImg.jsp", "/register.jsp", "/registerImg.jsp", "/runStats.jsp", "/tradehome.jsp", "/tradehomeImg.jsp", "/welcome.jsp", "/welcomeImg.jsp" };

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> legacyWebResources() {
        return factory -> factory.addContextCustomizers(context -> {
            try {
                Path root = Files.createTempDirectory("legacy-web-resources");
                root.toFile().deleteOnExit();
                extract(root, "META-INF/resources", WEB_RESOURCES);
                extract(root, "provenant/web-pages", WEB_PAGES);
                context.getResources().createWebResourceSet(
                        WebResourceRoot.ResourceSetType.POST, "/", root.toString(), null, "/");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    private static void extract(Path root, String prefix, String[] files) throws IOException {
        for (String file : files) {
            Path target = root.resolve(file.substring(1));
            Files.createDirectories(target.getParent());
            for (Path dir = target.getParent(); !dir.equals(root); dir = dir.getParent()) {
                dir.toFile().deleteOnExit();
            }
            try (InputStream in = ServletRegistrations.class.getClassLoader()
                    .getResourceAsStream(prefix + file)) {
                if (in == null) {
                    throw new IllegalStateException("missing packaged web resource " + file);
                }
                Files.copy(in, target);
            }
            target.toFile().deleteOnExit();
        }
    }

    @Bean
    public ServletContextInitializer servletContextParameters() {
        return context -> {
            context.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
            context.setInitParameter("javax.faces.PROJECT_STAGE", "Production");
            context.setInitParameter("javax.faces.STATE_SAVING_METHOD", "server");
        };
    }
}
