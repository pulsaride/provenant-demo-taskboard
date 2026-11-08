package com.github.hantsy.ee8sample.domain.support;

import com.github.hantsy.ee8sample.security.Authenticated;
import com.github.hantsy.ee8sample.security.UserInfo;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuditEntityListener {

    private static final Logger LOG = Logger.getLogger(AuditEntityListener.class.getName());

    // TODO(body: retention_calls_stubbed_member:currentUser)
    // TODO(annotation: PrePersist, body_not_extracted)
    public void beforePersist(Object entity) {
        throw new provenant.engine.EngineStubException("body not extracted for beforePersist — see lower::lir's module docs");
    }

    // TODO(body: retention_calls_stubbed_member:currentUser)
    // TODO(annotation: PreUpdate, body_not_extracted)
    public void beforeUpdate(Object entity) {
        throw new provenant.engine.EngineStubException("body not extracted for beforeUpdate — see lower::lir's module docs");
    }

    // TODO(body: retention_unprovided_library_type)
    private String currentUser() {
        throw new provenant.engine.EngineStubException("body not extracted for currentUser — see lower::lir's module docs");
    }
}
