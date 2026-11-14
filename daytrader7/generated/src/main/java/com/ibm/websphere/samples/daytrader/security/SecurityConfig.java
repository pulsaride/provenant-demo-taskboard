package com.ibm.websphere.samples.daytrader.security;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Fail-closed access control (RFC-025 section 2.3): every request needs an
 * authenticated user unless it is listed below, and the JSR-250 annotations on
 * the controller methods decide per endpoint. The listed requests are the
 * endpoints the legacy application left open.
 */
// TODO(authentication_not_mapped: auth_method_unsupported: BASIC)
// TODO(csrf_token_not_session_bound: double-submit XSRF-TOKEN cookie without __Host- prefix (no secure transport): plantable over HTTP or from a sibling subdomain; forgery still needs the X-XSRF-TOKEN header, which a cross-site page cannot send without a CORS preflight)
@Configuration
@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CsrfTokenRepository csrfRepository) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/PingHtml.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/PingJsp.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/PingServlet2Jsp.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/PingWebSocketBinary.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/PingWebSocketJson.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/PingWebSocketTextAsync.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/PingWebSocketTextSync.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/TestServlet")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/account.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/accountImg.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/app")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/config")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/config.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/configure.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/contentHome.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/displayQuote.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/docs/benchmarking.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/docs/documentation.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/docs/glossary.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/docs/rtCharacterisitics.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/docs/tradeFAQ.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/docs/tradeversion.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/ejb3/PingServlet2SessionLocal")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/favicon.ico")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/footer.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/header.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/DayTraderHead_blue.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/DayTraderHead_red.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/about.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/account.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/arrowdown.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/arrowup.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/bottomRedBar.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/configuration.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/dayTraderLogo.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/daytrader_simple_arch.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/faq.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/graph.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/home.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/homeBanner.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/line.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/logout.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/lower_banner.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/menuHome.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/nav_bg.png")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/portfolio.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/primitives.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/quotes.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/reports.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/spacer.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/ticker-anim.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/topRedBar.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/topline.jpg")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/tradeOverview.png")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/images/tradingAndPortfolios.gif")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/index.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/leftMenu.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/marketSummary.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/marketSummary.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/marketsummary")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/order.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/orderImg.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/pingBinary")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/pingTextAsync")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/pingTextSync")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/pingWebSocketJson")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/portfolio.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/portfolioImg.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/quote.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/quoteDataPrimitive.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/quoteImg.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/register.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/registerImg.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/runStats.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingJDBCRead")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingJDBCRead2JSP")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingJDBCWrite")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingJSONP")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingManagedExecutor")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingManagedThread")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingReentryServlet")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2DB")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2Include")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2IncludeRcv")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2JNDI")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2Jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2PDF")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet30Async")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet31Async")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet31AsyncRead")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServletLargeContentLength")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServletSetContentLength")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServletWriter")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingSession1")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingSession2")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingSession3")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingUpgradeServlet")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/style-jsf.css")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/style.css")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/tradehome.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/tradehomeImg.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/web_prmtv.html")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/welcome.jsp")).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/welcomeImg.jsp")).permitAll()
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfRepository)
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/TestServlet"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/app"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/config"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/ejb3/PingServlet2SessionLocal"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingJDBCRead"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingJDBCRead2JSP"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingJDBCWrite"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingJSONP"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingManagedExecutor"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingManagedThread"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingReentryServlet"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2DB"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2Include"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2IncludeRcv"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2JNDI"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2Jsp"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet2PDF"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet30Async"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet31Async"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServlet31AsyncRead"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServletLargeContentLength"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServletSetContentLength"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingServletWriter"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingSession1"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingSession2"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingSession3"))
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/servlet/PingUpgradeServlet"))
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
                .logout(AbstractHttpConfigurer::disable)
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.build();
    }

    /** The legacy role names are used as written (no ROLE_ prefix is added). */
    @Bean
    static CsrfTokenRepository csrfTokenRepository() {
        // SameSite=Lax: the SPA is same-site; a cross-site request carries no CSRF cookie.
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setHeaderName("X-XSRF-TOKEN");
        repository.setCookieCustomizer(cookie -> cookie.sameSite("Lax"));
        return repository;
    }

    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    private static final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
        private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
            this.xor.handle(request, response, csrfToken);
            csrfToken.get();
        }

        @Override
        public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
            // Header only: no _csrf request-parameter fallback.
            return request.getHeader(csrfToken.getHeaderName());
        }
    }

    private static final class CsrfCookieFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            if (csrfToken != null) {
                csrfToken.getToken();
            }
            filterChain.doFilter(request, response);
        }
    }
}
