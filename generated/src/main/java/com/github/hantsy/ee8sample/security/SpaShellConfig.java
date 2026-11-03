package com.github.hantsy.ee8sample.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import java.util.function.Predicate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * Serves the packaged Angular build; a client route answers index.html. Inert
 * unless static/index.html is on the classpath (the -Pfrontend jar).
 */
@Configuration
@ConditionalOnResource(resources = "classpath:/static/index.html")
public class SpaShellConfig implements WebMvcConfigurer {

    private static final String INDEX = "static/index.html";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Hashed bundle files never change under their name: cacheable.
        registry.addResourceHandler("/*-*.js", "/*-*.css")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)).cachePublic().immutable())
                .resourceChain(false);
        // index.html and the rest: never cached; a client route answers index.html.
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.noStore())
                .resourceChain(false)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource found = super.getResource(resourcePath, location);
                        if (found != null) {
                            return found;
                        }
                        return Paths.isRoute("/" + resourcePath) ? new ClassPathResource(INDEX) : null;
                    }
                });
    }

    /** The shell requests only (`Paths`); everything else stays with SecurityConfig. */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain spaShellFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(request -> Paths.isShellRequest(
                        request.getMethod(),
                        request.getRequestURI(),
                        request.getContextPath(),
                        name -> {
                            Resource file = new ClassPathResource("static/" + name);
                            return file.exists() && file.isReadable();
                        }))
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    // --- Paths: begin
    /** Which requests are the packaged Angular shell: an allow-list, never the servlet path. */
    static final class Paths {

        private static final Set<String> ROUTES = Set.of("details", "form", "login", "tasks");

        private Paths() {
        }

        /** A client route of the Angular app: the root or one of its top-level routes. */
        static boolean isRoute(String path) {
            if (path.equals("/")) {
                return true;
            }
            String name = path.startsWith("/") ? path.substring(1) : path;
            if (name.endsWith("/")) {
                name = name.substring(0, name.length() - 1);
            }
            return ROUTES.contains(name);
        }

        /** GET/HEAD of the root, a client route or a file of the bundle (`bundleFile` gets the path under static/). */
        static boolean isShellRequest(String method, String requestUri, String contextPath, Predicate<String> bundleFile) {
            if (!"GET".equals(method) && !"HEAD".equals(method)) {
                return false;
            }
            if (requestUri == null || contextPath == null || !requestUri.startsWith(contextPath)) {
                return false;
            }
            String path = requestUri.substring(contextPath.length());
            if (!path.startsWith("/") || path.contains("..") || path.contains("//") || path.contains("\\") || path.contains(";")) {
                return false;
            }
            if (path.equals("/api") || path.startsWith("/api/") || path.equals("/error")) {
                return false;
            }
            return isRoute(path) || (path.length() > 1 && !path.endsWith("/") && bundleFile.test(path.substring(1)));
        }
}
    // --- Paths: end
}
