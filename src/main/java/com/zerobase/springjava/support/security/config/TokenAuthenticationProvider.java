package com.zerobase.springjava.support.security.config;

import com.zerobase.springjava.support.security.model.AuthUser;
import com.zerobase.springjava.support.security.model.JwtAuthenticationToken;
import com.zerobase.springjava.support.security.model.TokenAuthenticatedPrincipal;
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

    public TokenAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        var principal = (TokenAuthenticatedPrincipal) jwtAuthenticationToken.getPrincipal();

        var token = principal.getToken();
        // validateToken

        var userId = "test-user-1";
        var userDetails = (AuthUser) userDetailsService.loadUserByUsername(userId);

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
