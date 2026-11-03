package com.github.hantsy.ee8sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.logging.Logger;
import org.springframework.beans.factory.InjectionPoint;

@Configuration
public class Resources {

    @Bean @Scope("prototype")
    public Logger getLogger(InjectionPoint p) {
        return Logger.getLogger(p.getMember().getDeclaringClass().getName());
    }
}
