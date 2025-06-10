package com.openclassrooms.mddapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfiguration {

    private static final String AUTH_REGISTER_ENDPOINT = "/api/auth/register";
    private static final String AUTH_LOGIN_ENDPOINT = "/api/auth/login";
    private static final String API_DOCS_PATTERN = "/v3/api-docs/**";
    private static final String SWAGGER_UI_PATTERN = "/swagger-ui/**";

    @Bean
    public AuthenticationTokenFilter createJwtAuthenticationFilter() {
        return new AuthenticationTokenFilter();
    }

    @Bean
    public SecurityFilterChain configureSecurityFilterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvcBuilder) throws Exception {
        return buildSecurityConfiguration(httpSecurity, mvcBuilder);
    }

    private SecurityFilterChain buildSecurityConfiguration(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.csrf(this::disableCsrfProtection);
        http.sessionManagement(this::configureStatelessSession);
        http.authorizeHttpRequests(auth -> configureRequestAuthorization(auth, mvc));
        http.httpBasic(Customizer.withDefaults());
        http.addFilterBefore(createJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private void disableCsrfProtection(org.springframework.security.config.annotation.web.configurers.CsrfConfigurer<HttpSecurity> csrf) {
        csrf.disable();
    }

    private void configureStatelessSession(org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer<HttpSecurity> session) {
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private void configureRequestAuthorization(org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth, MvcRequestMatcher.Builder mvc) {
        auth.requestMatchers(mvc.pattern(AUTH_REGISTER_ENDPOINT)).permitAll();
        auth.requestMatchers(mvc.pattern(AUTH_LOGIN_ENDPOINT)).permitAll();
        auth.requestMatchers(mvc.pattern(API_DOCS_PATTERN)).permitAll();
        auth.requestMatchers(mvc.pattern(SWAGGER_UI_PATTERN)).permitAll();
        auth.anyRequest().authenticated();
    }

    @Bean
    public MvcRequestMatcher.Builder createMvcRequestMatcherBuilder(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public BCryptPasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}