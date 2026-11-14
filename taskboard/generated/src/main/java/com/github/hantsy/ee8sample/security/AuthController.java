package com.github.hantsy.ee8sample.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The form login of the legacy application as a JSON endpoint: the credentials
 * are checked by the AuthenticationManager (retained user lookup, legacy hash
 * format) and the result is kept in the HTTP session, whose cookie the browser
 * sends back. Every failure answers 401 (no oracle); the cause is logged.
 */
@RestController
public class AuthController {

    /** Credentials of one login attempt. */
    public record LoginRequest(@NotNull @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters") String username, @NotNull @Size(min = 5, max = 50, message = "Password must be between 5 and 50 characters") String password) {}

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final CsrfAuthenticationStrategy csrfStrategy;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    public AuthController(AuthenticationManager authenticationManager, CsrfTokenRepository csrfRepository) {
        this.authenticationManager = authenticationManager;
        this.csrfStrategy = new CsrfAuthenticationStrategy(csrfRepository);
    }

    /**
     * Issues the CSRF token before the login, nothing else: the CSRF filter writes
     * the CSRF cookie on this response (no body, no session). The login POST must
     * echo it in the X-XSRF-TOKEN header.
     */
    @GetMapping("/api/auth/login")
    public ResponseEntity<Void> loginToken() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest login, HttpServletRequest request, HttpServletResponse response) {
        if (login == null || login.username() == null || login.password() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(login.username(), login.password()));
        if (request.getSession(false) != null) {
            request.changeSessionId();
        }
        SecurityContext context = contextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        contextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        // A new CSRF token for the new authentication.
        csrfStrategy.onAuthentication(authentication, request, response);
        return ResponseEntity.noContent().build();
    }

    /** The answer of {@link #session}: whether the request has an authenticated principal, nothing else. */
    public record SessionState(boolean authenticated) {}

    /**
     * Whether the current request has an authenticated principal, the state
     * {@code request.userPrincipal != null} showed in the legacy pages. Open to
     * anonymous callers (it answers false for them); it reads the existing
     * session only, never creates one, and returns no identity, role or id.
     */
    @GetMapping("/api/auth/session")
    public SessionState session(HttpServletRequest request) {
        return new SessionState(request.getUserPrincipal() != null);
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<Void> authenticationFailed(AuthenticationException failure) {
        // Every failure answers 401, whatever its cause: the response must not tell a
        // wrong password from an unknown user or a failing lookup. The cause is logged.
        if (failure instanceof AuthenticationServiceException) {
            LOG.error("login failed in the user lookup", failure);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Void> credentialsRejected(MethodArgumentNotValidException rejected) {
        // The legacy login form validated these constraints before authenticating:
        // rejected credentials never reach the authentication, and answer 401 like
        // every other login failure.
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
