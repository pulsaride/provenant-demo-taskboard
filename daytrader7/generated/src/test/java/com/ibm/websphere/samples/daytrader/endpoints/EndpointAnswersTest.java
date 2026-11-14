package com.ibm.websphere.samples.daytrader.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import jakarta.servlet.ServletException;
import java.util.Set;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * RFC-025 G4 as a test (engine-authored, ADR-010 PR-6.W2): one request per endpoint of
 * the contract, authenticated with the roles the endpoint names, with G4's samples (a path
 * variable is 0, a declared parameter a sample of its type, a body method sends {}).
 * A smoke, not a scenario: it claims nothing about behaviour. Each test runs in a transaction
 * that is rolled back, so a write of one request never changes what the next one finds. Listed in
 * reserved_to_emitter.engine_files of modernization-contract.json; not patchable.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EndpointAnswersTest {

    private static final Set<String> BODY_METHODS = Set.of("POST", "PUT", "PATCH");
    private static final Set<String> SAFE_METHODS = Set.of("GET", "HEAD", "OPTIONS");

    @Autowired(required = false)
    private CsrfTokenRepository csrfRepository;

    @Autowired
    private MockMvc mockMvc;

    /** An authenticated session holding exactly {@code authorities} (stored where the emitted chain loads it). */
    private static MockHttpSession signedIn(String[] authorities) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("endpoint-answers", "n/a",
                Arrays.stream(authorities).map(SimpleGrantedAuthority::new).toList()));
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        return session;
    }

    /** The double-submit pair the emitted CsrfTokenRepository writes: its cookie and its header. */
    private void withCsrf(MockHttpServletRequestBuilder request) {
        if (csrfRepository == null) {
            return;
        }
        MockHttpServletRequest probe = new MockHttpServletRequest();
        MockHttpServletResponse probed = new MockHttpServletResponse();
        CsrfToken token = csrfRepository.generateToken(probe);
        csrfRepository.saveToken(token, probe, probed);
        for (Cookie cookie : probed.getCookies()) {
            request.cookie(new Cookie(cookie.getName(), cookie.getValue()));
        }
        request.header(token.getHeaderName(), token.getToken());
    }

    /** The status of the one request G4 sends; an exception no handler maps is the 500 a server would answer. */
    private int statusOf(String method, String target, String[] authorities) throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.request(HttpMethod.valueOf(method), target)
                .accept(MediaType.APPLICATION_JSON);
        if (BODY_METHODS.contains(method)) {
            request.contentType(MediaType.APPLICATION_JSON).content("{}");
        }
        request.session(signedIn(authorities));
        if (!SAFE_METHODS.contains(method)) {
            withCsrf(request);
        }
        try {
            return mockMvc.perform(request).andReturn().getResponse().getStatus();
        } catch (ServletException unmapped) {
            return 500;
        }
    }

    /** A Stubbed endpoint answers exactly 501 (the engine stub exception's mapping). */
    private void assertStubbedAnswers501(String method, String target, String[] authorities) throws Exception {
        assertEquals(501, statusOf(method, target, authorities), method + " " + target);
    }

    /** Served (not 404 or 405), not refused to its own roles (not 401 or 403), no 5xx but 501. */
    private void assertAnswers(String method, String target, String[] authorities) throws Exception {
        int status = statusOf(method, target, authorities);
        String what = method + " " + target + " answered " + status;
        assertFalse(status == 404 || status == 405, what + ": not served");
        assertFalse(status == 401 || status == 403, what + ": refused to its own roles");
        assertFalse(status >= 500 && status != 501, what + ": server error");
    }

    @Test
    @DisplayName("GET /api/accountdata/logoutCount (tier not claimed)")
    void endpoint_001_get_api_accountdata_logoutcount() throws Exception {
        // node_sig 033fdfbeda367640880153eff1220968c089af550937512f2915fe7431454cfb; tier not claimed
        assertAnswers("GET", "/api/accountdata/logoutCount", new String[] {});
    }

    @Test
    @DisplayName("GET /api/portfolio/totalGain (tier not claimed)")
    void endpoint_002_get_api_portfolio_totalgain() throws Exception {
        // node_sig 107c3b5030ed351743cddc5dcea3c805df2613d54f47d7d872805294db99515e; tier not claimed
        assertAnswers("GET", "/api/portfolio/totalGain", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/accountID (tier not claimed)")
    void endpoint_003_get_api_accountdata_accountid() throws Exception {
        // node_sig 21fd43371f9310d404ba254e267be3aaf8d993b71ba99a85c8cdb1585be4d8b9; tier not claimed
        assertAnswers("GET", "/api/accountdata/accountID", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/profileID (tier not claimed)")
    void endpoint_004_get_api_accountdata_profileid() throws Exception {
        // node_sig 23b9be1636150994116656bf26e739802a92ad88eb701f2566e28cc1c10c36b9; tier not claimed
        assertAnswers("GET", "/api/accountdata/profileID", new String[] {});
    }

    @Test
    @DisplayName("POST /api/quotedata/buy (tier not claimed)")
    void endpoint_005_post_api_quotedata_buy() throws Exception {
        // node_sig 31d49d360118349214cfba591e458c41944abedad106534a52ad0ac45db71b0f; tier not claimed
        assertAnswers("POST", "/api/quotedata/buy", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/gainHTML (tier not claimed)")
    void endpoint_006_get_api_accountdata_gainhtml() throws Exception {
        // node_sig 3aed8256da5a7966e0ff8a6acf8bb8d69dc6f363441bcbd55be6c8569747d04b; tier not claimed
        assertAnswers("GET", "/api/accountdata/gainHTML", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/loginCount (tier not claimed)")
    void endpoint_007_get_api_accountdata_logincount() throws Exception {
        // node_sig 459a78fed4c743b9312da510d8b7cc3f107d2c4c6e02618ed1a9a46d957a8768; tier not claimed
        assertAnswers("GET", "/api/accountdata/loginCount", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/numberHoldings (tier not claimed)")
    void endpoint_008_get_api_accountdata_numberholdings() throws Exception {
        // node_sig 48449bff7d97fd17496950247900349bb4d58bfdfd40d6a646d9f94f9799b81e; tier not claimed
        assertAnswers("GET", "/api/accountdata/numberHoldings", new String[] {});
    }

    @Test
    @DisplayName("GET /api/pingCDIJSFBean/hitCount (tier not claimed)")
    void endpoint_009_get_api_pingcdijsfbean_hitcount() throws Exception {
        // node_sig 4ab5951d4d46400bf920d2bc43792ee1559f3c10a776648d9ef58bfe5d745015; tier not claimed
        assertAnswers("GET", "/api/pingCDIJSFBean/hitCount", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/creationDate (tier not claimed)")
    void endpoint_010_get_api_accountdata_creationdate() throws Exception {
        // node_sig 4bcefc0c3933ae4ddd3754787d06d07de00483d1b542c193c0d6d046106a2719; tier not claimed
        assertAnswers("GET", "/api/accountdata/creationDate", new String[] {});
    }

    @Test
    @DisplayName("POST /api/tradeapp/updateProfile (tier not claimed)")
    void endpoint_011_post_api_tradeapp_updateprofile() throws Exception {
        // node_sig 4cc22d68009fb91e57399e2212f3abb58e7facf0451d5988e270be2a37f65a62; tier not claimed
        assertAnswers("POST", "/api/tradeapp/updateProfile", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/currentTime (tier not claimed)")
    void endpoint_012_get_api_accountdata_currenttime() throws Exception {
        // node_sig 5f0fd8241e11f63f479bc27a22de627a12490813765f5e7a34bbd08bbe49ba46; tier not claimed
        assertAnswers("GET", "/api/accountdata/currentTime", new String[] {});
    }

    @Test
    @DisplayName("POST /api/tradeapp/logout (tier not claimed)")
    void endpoint_013_post_api_tradeapp_logout() throws Exception {
        // node_sig 5fc3fcb4a9339821313dbb42e025eb5352af5321cb3a6934f519638cdd8722a5; tier not claimed
        assertAnswers("POST", "/api/tradeapp/logout", new String[] {});
    }

    @Test
    @DisplayName("POST /api/portfolio/sell (tier not claimed)")
    void endpoint_014_post_api_portfolio_sell() throws Exception {
        // node_sig 6b4861870268a04d0a3a77bad272c998cbdea19fcdfff442956af403997453c5; tier not claimed
        assertAnswers("POST", "/api/portfolio/sell", new String[] {});
    }

    @Test
    @DisplayName("GET /api/portfolio/numberHoldings (tier not claimed)")
    void endpoint_015_get_api_portfolio_numberholdings() throws Exception {
        // node_sig 730a6e2149a5113d332a25013b34854464965d89187dc985a9b702d3a44a66a9; tier not claimed
        assertAnswers("GET", "/api/portfolio/numberHoldings", new String[] {});
    }

    @Test
    @DisplayName("GET /api/portfolio/totalBasis (tier not claimed)")
    void endpoint_016_get_api_portfolio_totalbasis() throws Exception {
        // node_sig 739e06c5cbfb0346f704c8cd603aef0e91c9c26f1254b8bd0f355e02533b236b; tier not claimed
        assertAnswers("GET", "/api/portfolio/totalBasis", new String[] {});
    }

    @Test
    @DisplayName("GET /api/tradeapp/results (tier not claimed)")
    void endpoint_017_get_api_tradeapp_results() throws Exception {
        // node_sig 76c5bc982844abc4ab9155456e43e890b22d8521c7a7f412f60b7f19cd65eafc; tier not claimed
        assertAnswers("GET", "/api/tradeapp/results", new String[] {});
    }

    @Test
    @DisplayName("POST /api/accountdata/toggleShowAllRows (tier not claimed)")
    void endpoint_018_post_api_accountdata_toggleshowallrows() throws Exception {
        // node_sig 854064ccb72d2a8cd5fcef9e0eb6c69e54987320cc646e1aaa3d0c41ae4aaa1c; tier not claimed
        assertAnswers("POST", "/api/accountdata/toggleShowAllRows", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/holdingsTotal (tier not claimed)")
    void endpoint_019_get_api_accountdata_holdingstotal() throws Exception {
        // node_sig 891ac3b1f062a7c2dfbe102a40f592549f34e0e33b9291f7f6b700ce9caef714; tier not claimed
        assertAnswers("GET", "/api/accountdata/holdingsTotal", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/gainPercentHTML (tier not claimed)")
    void endpoint_020_get_api_accountdata_gainpercenthtml() throws Exception {
        // node_sig 8a4b3dd4bf59de7dbd7a7c2c55a05d2c37130388e9fea0b9401065810ae17654; tier not claimed
        assertAnswers("GET", "/api/accountdata/gainPercentHTML", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/sessionCreationDate (tier not claimed)")
    void endpoint_021_get_api_accountdata_sessioncreationdate() throws Exception {
        // node_sig 8c6c1474b8fdf11cbabd15a886fa23c18f2cc2c442576dece18ea9dd9824bcba; tier not claimed
        assertAnswers("GET", "/api/accountdata/sessionCreationDate", new String[] {});
    }

    @Test
    @DisplayName("POST /api/quotedata/getQuotesBySymbols (tier not claimed)")
    void endpoint_022_post_api_quotedata_getquotesbysymbols() throws Exception {
        // node_sig 947ea1e25804e56c2e7c92b8b4c01e15c875d3015953dc0eaa5eb295cff75dd6; tier not claimed
        assertAnswers("POST", "/api/quotedata/getQuotesBySymbols", new String[] {});
    }

    @Test
    @DisplayName("POST /api/tradeapp/login (tier not claimed)")
    void endpoint_023_post_api_tradeapp_login() throws Exception {
        // node_sig 95e0fa265568bdd54de3307e6e083938d1b82adea3eabc244aece8698414176a; tier not claimed
        assertAnswers("POST", "/api/tradeapp/login", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/balance (tier not claimed)")
    void endpoint_024_get_api_accountdata_balance() throws Exception {
        // node_sig 96f0c6ef84ba8af81352b8fb03edfcd4155e268630ef5efe4eb44a88506dafbb; tier not claimed
        assertAnswers("GET", "/api/accountdata/balance", new String[] {});
    }

    @Test
    @DisplayName("GET /api/portfolio/totalValue (tier not claimed)")
    void endpoint_025_get_api_portfolio_totalvalue() throws Exception {
        // node_sig d1f3bd55af149a398e5e00530e351db835d21309e525a99fdb2f95c60ba0b018; tier not claimed
        assertAnswers("GET", "/api/portfolio/totalValue", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/lastLogin (tier not claimed)")
    void endpoint_026_get_api_accountdata_lastlogin() throws Exception {
        // node_sig d26d9669358368416992291685436c5247f37b57cf75b165a86ca2fd09c83bb1; tier not claimed
        assertAnswers("GET", "/api/accountdata/lastLogin", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/openBalance (tier not claimed)")
    void endpoint_027_get_api_accountdata_openbalance() throws Exception {
        // node_sig edf67f9c0648d5788c42a105f2293b45cee1e0cccb30a8df92fa82c5ddb3fa97; tier not claimed
        assertAnswers("GET", "/api/accountdata/openBalance", new String[] {});
    }

    @Test
    @DisplayName("GET /api/accountdata/sumOfCashHoldings (tier not claimed)")
    void endpoint_028_get_api_accountdata_sumofcashholdings() throws Exception {
        // node_sig f834790fdbf35620628e7be71c7e50f407141d7f3c3f679739e5baa2c9aa03c0; tier not claimed
        assertAnswers("GET", "/api/accountdata/sumOfCashHoldings", new String[] {});
    }

    @Test
    @DisplayName("GET /api/portfolio/totalGainPercentHTML (tier not claimed)")
    void endpoint_029_get_api_portfolio_totalgainpercenthtml() throws Exception {
        // node_sig fe334570ebecee28a2ef21c9cefab6bc6600fc56e351473810e1280fa2cdc87f; tier not claimed
        assertAnswers("GET", "/api/portfolio/totalGainPercentHTML", new String[] {});
    }
}
