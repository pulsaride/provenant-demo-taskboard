package com.github.hantsy.ee8sample.security;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import jakarta.inject.Qualifier;

@Qualifier
@Retention(value = RUNTIME)
@Target(value = {METHOD, FIELD, PARAMETER, TYPE})
public @interface Authenticated {
}
