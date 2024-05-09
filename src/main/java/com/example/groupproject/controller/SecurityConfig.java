package com.example.groupproject.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.config.Customizer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults()) // Disable CSRF
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(createSecurityMatcher()).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/login") // This should match the form action in your HTML
                                .failureUrl("/login-error.html")
                                .failureUrl("/login?error=true")
                                .defaultSuccessUrl("/index", true) // Redirect to /index on successful login
                );
        return http.build();
    }

    private RequestMatcher createSecurityMatcher() {
        return new OrRequestMatcher(Arrays.asList(
                new AntPathRequestMatcher("/index"),
                new AntPathRequestMatcher("/login")
        ));
    }

}
