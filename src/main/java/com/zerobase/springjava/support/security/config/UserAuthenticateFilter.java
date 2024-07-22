package com.zerobase.springjava.support.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class UserAuthenticateFilter extends UsernamePasswordAuthenticationFilter {
    private static final String LOGIN_PATH = "/login";
    private static final String LOGIN_HTTP_METHOD = "POST";
    private final AuthenticationManager authenticationManager;

    public UserAuthenticateFilter(@Qualifier("userAuthenticationManager") AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void afterPropertiesSet() {
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LOGIN_PATH, LOGIN_HTTP_METHOD));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Map<String, String> credentials = null;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String userId = credentials.get("username");
        String password = credentials.get("password");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, password);

        return authenticationManager.authenticate(authenticationToken);
    }
}
