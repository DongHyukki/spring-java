package com.donghyukki.springjava.support.security.config;

import com.donghyukki.springjava.support.common.jwt.JsonWebToken;
import com.donghyukki.springjava.support.security.model.JwtAuthenticationToken;
import com.donghyukki.springjava.support.security.model.TokenAuthenticatedPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String API_PATH = "/api/**";
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final RequestMatcher requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(API_PATH);
    private final AuthenticationManager authenticationManager;

    public TokenAuthenticationFilter(@Qualifier("tokenAuthenticationManager") AuthenticationManager authenticationManager) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    public static String getBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        var token = getBearerToken(request);
        var principal = new TokenAuthenticatedPrincipal(JsonWebToken.of(token));

        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(principal);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
