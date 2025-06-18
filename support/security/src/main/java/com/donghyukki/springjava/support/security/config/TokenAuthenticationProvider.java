package com.donghyukki.springjava.support.security.config;

import com.donghyukki.springjava.support.security.model.AuthUser;
import com.donghyukki.springjava.support.security.model.JwtAuthenticationToken;
import com.donghyukki.springjava.support.security.model.TokenAuthenticatedPrincipal;
import com.donghyukki.springjava.support.security.support.common.jwt.JsonWebTokenManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component("tokenAuthenticationProvider")
public class TokenAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final JsonWebTokenManager jsonWebTokenManager;

    public TokenAuthenticationProvider(UserDetailsService userDetailsService, JsonWebTokenManager jsonWebTokenManager) {
        this.userDetailsService = userDetailsService;
        this.jsonWebTokenManager = jsonWebTokenManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        var principal = (TokenAuthenticatedPrincipal) jwtAuthenticationToken.getPrincipal();

        var token = principal.getToken();
        var subject = jsonWebTokenManager.getIssuerAndSubject(token).second();
        var userDetails = (AuthUser) userDetailsService.loadUserByUsername(subject);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }

        return JwtAuthenticationToken.authenticated(userDetails.getId(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
