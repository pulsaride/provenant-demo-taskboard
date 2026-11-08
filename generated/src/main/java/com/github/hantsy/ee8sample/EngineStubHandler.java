package com.github.hantsy.ee8sample;

import provenant.engine.EngineStubException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Answers a request that reached a stubbed body with HTTP 501 and a fixed body.
 * The stub's message is logged here and never sent to the caller.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EngineStubHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EngineStubHandler.class);

    private static final String BODY = "{\"status\":501,\"error\":\"not_implemented\",\"message\":\"This operation is not carried by the modernized application.\"}";

    @ExceptionHandler(EngineStubException.class)
    public ResponseEntity<String> notImplemented(EngineStubException stub) {
        LOG.warn("stubbed operation reached: {}", stub.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BODY);
    }
}
