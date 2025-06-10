package com.openclassrooms.mddapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    private static final String ANGULAR_CLIENT_URL = "http://localhost:4200";
    private static final String WILDCARD_PATH = "/**";
    
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        configureCrossOriginRequests(corsRegistry);
    }
    
    private void configureCrossOriginRequests(CorsRegistry registry) {
        registry.addMapping(WILDCARD_PATH)
                .allowedOrigins(ANGULAR_CLIENT_URL)
                .allowedMethods(getPermittedHttpMethods())
                .allowedHeaders(getAllowedRequestHeaders())
                .allowCredentials(true);
    }
    
    private String[] getPermittedHttpMethods() {
        return new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    }
    
    private String getAllowedRequestHeaders() {
        return "*";
    }
}