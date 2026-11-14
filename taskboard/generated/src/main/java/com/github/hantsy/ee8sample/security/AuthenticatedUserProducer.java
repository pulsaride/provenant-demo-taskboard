package com.github.hantsy.ee8sample.security;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

// TODO(cdi: provably_null_normal_scoped_product)
@Component
@Scope("prototype")
public class AuthenticatedUserProducer {

    // @SessionScoped not reproduced: javax.faces has no equivalent in a Spring Boot REST backend.
    @Authenticated
    private UserInfo currentUser;

    // TODO(param_annotation: authenticatedUser: Observes, cdi_event_never_fired)
    // TODO(param_annotation: authenticatedUser: Authenticated, not_mapped)
    public void handleAuthenticationEvent(UserInfo authenticatedUser) {
        this.currentUser = authenticatedUser;
    }
}
