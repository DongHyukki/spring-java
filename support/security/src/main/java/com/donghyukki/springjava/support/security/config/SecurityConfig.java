package com.donghyukki.springjava.support.security.config;

import com.donghyukki.springjava.support.security.model.AdminAuthority;
import com.donghyukki.springjava.support.security.model.SuperAdminAuthority;
import com.donghyukki.springjava.support.security.model.UserAuthority;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            TokenAuthenticationFilter tokenAuthenticationFilter
    ) throws Exception {
        return http.
                csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requestMatcherCustomizer) ->
                        requestMatcherCustomizer
                                .requestMatchers("/login")
                                .permitAll()
                                .requestMatchers("/api/v1/logging")
                                .permitAll()
                                .requestMatchers("/api/**")
                                .hasAnyAuthority(UserAuthority.ROLE, AdminAuthority.ROLE, SuperAdminAuthority.ROLE)
                                .anyRequest().permitAll())
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        (sessionManagementCustomizer) ->
                                sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }
}
