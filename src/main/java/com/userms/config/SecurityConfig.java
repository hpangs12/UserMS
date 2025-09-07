package com.userms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.userms.filters.LoginCheckFilter;


/**
 * Spring Security Configuration Class
 * <p>
 * This class provides the custom security configuration needed for our project to function properly.
 * </p>
 *
 * <b>Publicly accessible endpoints: </b><p>"/users/login", "/users/login/activate", "/users/register", "/users/auth/validate"</p>
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true) // Enables @PreAuthorize, @PostAuthorize
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LoginCheckFilter loginCheckFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/users/login", "/users/login/activate", "/users/register", "/users/auth/validate")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(loginCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                	    .authenticationEntryPoint((req, res, authEx) -> {
                	        req.setAttribute("authEx", authEx);
                	        req.getRequestDispatcher("/error/auth").forward(req, res);
                	    })
                	    .accessDeniedHandler((req, res, accessEx) -> {
                	        req.setAttribute("accessEx", accessEx);
                	        req.getRequestDispatcher("/error/denied").forward(req, res);
                	    })
                	)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}
