package com.ibm.websphere.samples.daytrader;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Serves the legacy static files byte for byte at their own exact paths (engine-authored;
 * listed in reserved_to_emitter.engine_files; not patchable). Registered by
 * ServletRegistrations on exactly the paths of the table.
 */
public class LegacyStaticContent extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /** Path to {resource, content type}. */
    private static final Map<String, String[]> TABLE = Map.ofEntries(
            Map.entry("", new String[] { "provenant/legacy-web/index.html", "text/html" }),
            Map.entry("/PingHtml.html", new String[] { "provenant/legacy-web/PingHtml.html", "text/html" }),
            Map.entry("/PingWebSocketBinary.html", new String[] { "provenant/legacy-web/PingWebSocketBinary.html", "text/html" }),
            Map.entry("/PingWebSocketJson.html", new String[] { "provenant/legacy-web/PingWebSocketJson.html", "text/html" }),
            Map.entry("/PingWebSocketTextAsync.html", new String[] { "provenant/legacy-web/PingWebSocketTextAsync.html", "text/html" }),
            Map.entry("/PingWebSocketTextSync.html", new String[] { "provenant/legacy-web/PingWebSocketTextSync.html", "text/html" }),
            Map.entry("/configure.html", new String[] { "provenant/legacy-web/configure.html", "text/html" }),
            Map.entry("/contentHome.html", new String[] { "provenant/legacy-web/contentHome.html", "text/html" }),
            Map.entry("/docs/benchmarking.html", new String[] { "provenant/legacy-web/docs/benchmarking.html", "text/html" }),
            Map.entry("/docs/documentation.html", new String[] { "provenant/legacy-web/docs/documentation.html", "text/html" }),
            Map.entry("/docs/glossary.html", new String[] { "provenant/legacy-web/docs/glossary.html", "text/html" }),
            Map.entry("/docs/rtCharacterisitics.html", new String[] { "provenant/legacy-web/docs/rtCharacterisitics.html", "text/html" }),
            Map.entry("/docs/tradeFAQ.html", new String[] { "provenant/legacy-web/docs/tradeFAQ.html", "text/html" }),
            Map.entry("/docs/tradeversion.html", new String[] { "provenant/legacy-web/docs/tradeversion.html", "text/html" }),
            Map.entry("/favicon.ico", new String[] { "provenant/legacy-web/favicon.ico", "image/x-icon" }),
            Map.entry("/footer.html", new String[] { "provenant/legacy-web/footer.html", "text/html" }),
            Map.entry("/header.html", new String[] { "provenant/legacy-web/header.html", "text/html" }),
            Map.entry("/images/DayTraderHead_blue.gif", new String[] { "provenant/legacy-web/images/DayTraderHead_blue.gif", "image/gif" }),
            Map.entry("/images/DayTraderHead_red.gif", new String[] { "provenant/legacy-web/images/DayTraderHead_red.gif", "image/gif" }),
            Map.entry("/images/about.gif", new String[] { "provenant/legacy-web/images/about.gif", "image/gif" }),
            Map.entry("/images/account.gif", new String[] { "provenant/legacy-web/images/account.gif", "image/gif" }),
            Map.entry("/images/arrowdown.gif", new String[] { "provenant/legacy-web/images/arrowdown.gif", "image/gif" }),
            Map.entry("/images/arrowup.gif", new String[] { "provenant/legacy-web/images/arrowup.gif", "image/gif" }),
            Map.entry("/images/bottomRedBar.gif", new String[] { "provenant/legacy-web/images/bottomRedBar.gif", "image/gif" }),
            Map.entry("/images/configuration.gif", new String[] { "provenant/legacy-web/images/configuration.gif", "image/gif" }),
            Map.entry("/images/dayTraderLogo.gif", new String[] { "provenant/legacy-web/images/dayTraderLogo.gif", "image/gif" }),
            Map.entry("/images/daytrader_simple_arch.gif", new String[] { "provenant/legacy-web/images/daytrader_simple_arch.gif", "image/gif" }),
            Map.entry("/images/faq.gif", new String[] { "provenant/legacy-web/images/faq.gif", "image/gif" }),
            Map.entry("/images/graph.gif", new String[] { "provenant/legacy-web/images/graph.gif", "image/gif" }),
            Map.entry("/images/home.gif", new String[] { "provenant/legacy-web/images/home.gif", "image/gif" }),
            Map.entry("/images/homeBanner.gif", new String[] { "provenant/legacy-web/images/homeBanner.gif", "image/gif" }),
            Map.entry("/images/line.gif", new String[] { "provenant/legacy-web/images/line.gif", "image/gif" }),
            Map.entry("/images/logout.gif", new String[] { "provenant/legacy-web/images/logout.gif", "image/gif" }),
            Map.entry("/images/lower_banner.gif", new String[] { "provenant/legacy-web/images/lower_banner.gif", "image/gif" }),
            Map.entry("/images/menuHome.gif", new String[] { "provenant/legacy-web/images/menuHome.gif", "image/gif" }),
            Map.entry("/images/nav_bg.png", new String[] { "provenant/legacy-web/images/nav_bg.png", "image/png" }),
            Map.entry("/images/portfolio.gif", new String[] { "provenant/legacy-web/images/portfolio.gif", "image/gif" }),
            Map.entry("/images/primitives.gif", new String[] { "provenant/legacy-web/images/primitives.gif", "image/gif" }),
            Map.entry("/images/quotes.gif", new String[] { "provenant/legacy-web/images/quotes.gif", "image/gif" }),
            Map.entry("/images/reports.gif", new String[] { "provenant/legacy-web/images/reports.gif", "image/gif" }),
            Map.entry("/images/spacer.gif", new String[] { "provenant/legacy-web/images/spacer.gif", "image/gif" }),
            Map.entry("/images/ticker-anim.gif", new String[] { "provenant/legacy-web/images/ticker-anim.gif", "image/gif" }),
            Map.entry("/images/topRedBar.gif", new String[] { "provenant/legacy-web/images/topRedBar.gif", "image/gif" }),
            Map.entry("/images/topline.jpg", new String[] { "provenant/legacy-web/images/topline.jpg", "image/jpeg" }),
            Map.entry("/images/tradeOverview.png", new String[] { "provenant/legacy-web/images/tradeOverview.png", "image/png" }),
            Map.entry("/images/tradingAndPortfolios.gif", new String[] { "provenant/legacy-web/images/tradingAndPortfolios.gif", "image/gif" }),
            Map.entry("/index.html", new String[] { "provenant/legacy-web/index.html", "text/html" }),
            Map.entry("/leftMenu.html", new String[] { "provenant/legacy-web/leftMenu.html", "text/html" }),
            Map.entry("/marketSummary.html", new String[] { "provenant/legacy-web/marketSummary.html", "text/html" }),
            Map.entry("/style-jsf.css", new String[] { "provenant/legacy-web/style-jsf.css", "text/css" }),
            Map.entry("/style.css", new String[] { "provenant/legacy-web/style.css", "text/css" }),
            Map.entry("/web_prmtv.html", new String[] { "provenant/legacy-web/web_prmtv.html", "text/html" }));

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String[] entry = TABLE.get(request.getServletPath());
        if (entry == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        byte[] bytes;
        try (InputStream in = LegacyStaticContent.class.getClassLoader().getResourceAsStream(entry[0])) {
            if (in == null) {
                throw new IllegalStateException("missing packaged legacy file " + entry[0]);
            }
            bytes = in.readAllBytes();
        }
        response.setContentType(entry[1]);
        response.setContentLengthLong(bytes.length);
        response.getOutputStream().write(bytes);
    }
}
