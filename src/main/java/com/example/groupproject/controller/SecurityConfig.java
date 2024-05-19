package com.example.groupproject.controller;

import com.example.groupproject.security.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig{

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;
*/
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    public SecurityConfig(CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults()) // Disable CSRF
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(createSecurityMatcher()).permitAll()
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/seller/**").hasRole("SELLER")
                                .anyRequest().authenticated()


                )

                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/login") // This should match the form action in your HTML
                                .failureUrl("/login-error.html")
                                .failureUrl("/login?error=true")
                                .defaultSuccessUrl("/index", true) // Redirect to /index on successful login
                                .permitAll()
                )

                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .permitAll()
                );

        return http.build();
    }

    private RequestMatcher createSecurityMatcher() {
        return new OrRequestMatcher(Arrays.asList(
                new AntPathRequestMatcher("/index"),
                new AntPathRequestMatcher("/login"),
                new AntPathRequestMatcher("/logout")
        ));
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }
    @Bean
    public CustomAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

}





