package com.github.hantsy.ee8sample.endpoints;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
    @DisplayName("GET /api/viewTaskAction/task (tier not claimed)")
    void endpoint_001_get_api_viewtaskaction_task() throws Exception {
        // node_sig 1fc1492c3cbf792219d9529771db07295995b3dd5b3be279921344318a6bb59a; tier not claimed
        assertAnswers("GET", "/api/viewTaskAction/task?id=1", new String[] {});
    }

    @Test
    @DisplayName("GET /api/taskHome/donetasks (tier not claimed)")
    void endpoint_002_get_api_taskhome_donetasks() throws Exception {
        // node_sig 231667a94db77a823b898d4ea47ce855061f3926f4a6ae4c1ac3a6b47960a6bd; tier not claimed
        assertAnswers("GET", "/api/taskHome/donetasks", new String[] {});
    }

    @Test
    @DisplayName("GET /api/taskHome/doingtasks (tier not claimed)")
    void endpoint_003_get_api_taskhome_doingtasks() throws Exception {
        // node_sig 2c2949141b7cdfe39d5762aeafa212e15247f23ce759af1351d516f8a74a9f1e; tier not claimed
        assertAnswers("GET", "/api/taskHome/doingtasks", new String[] {});
    }

    @Test
    @DisplayName("POST /api/taskHome/markTaskDoing (tier not claimed)")
    void endpoint_004_post_api_taskhome_marktaskdoing() throws Exception {
        // node_sig 2f89b7a591e42dd32287d4085d4815985b63d4a251669de047d81e4bcb2a14ae; tier not claimed
        assertAnswers("POST", "/api/taskHome/markTaskDoing?id=1", new String[] {});
    }

    @Test
    @DisplayName("POST /api/taskHome/markTaskDone (tier not claimed)")
    void endpoint_005_post_api_taskhome_marktaskdone() throws Exception {
        // node_sig 4fa2d663c10205c36b59e0296a2fa521fbf97c0deb6a596368362e011903db4d; tier not claimed
        assertAnswers("POST", "/api/taskHome/markTaskDone?id=1", new String[] {});
    }

    @Test
    @DisplayName("GET /api/taskHome/todotasks (tier not claimed)")
    void endpoint_006_get_api_taskhome_todotasks() throws Exception {
        // node_sig 8144f2807f295cc1aabc1a3dbdbd7924a2572a27e3978f07741f97337c1397ee; tier not claimed
        assertAnswers("GET", "/api/taskHome/todotasks", new String[] {});
    }

    @Test
    @DisplayName("POST /api/taskHome/deleteTask (tier not claimed)")
    void endpoint_007_post_api_taskhome_deletetask() throws Exception {
        // node_sig c518fa09531d9d4a7b149646e619bb3706871cd7c8d35aee062b295389ef1918; tier not claimed
        assertAnswers("POST", "/api/taskHome/deleteTask?id=1", new String[] {});
    }

    @Test
    @DisplayName("GET /api/editTaskAction/task (tier not claimed)")
    void endpoint_008_get_api_edittaskaction_task() throws Exception {
        // node_sig c5303f432ea0b0018cfc68b8a3a24f23c36d0cf5319ca87bd5b5a69d7dcc1b9d; tier not claimed
        assertAnswers("GET", "/api/editTaskAction/task?id=1", new String[] {"ROLE_ADMIN", "ROLE_USER"});
    }

    @Test
    @DisplayName("POST /api/editTaskAction/save (tier not claimed)")
    void endpoint_009_post_api_edittaskaction_save() throws Exception {
        // node_sig e2c79e9d482fd4373489ac7064e1309699eb82cbc553565214c4f76cb4775d72; tier not claimed
        assertAnswers("POST", "/api/editTaskAction/save?id=1", new String[] {"ROLE_ADMIN", "ROLE_USER"});
    }
}
