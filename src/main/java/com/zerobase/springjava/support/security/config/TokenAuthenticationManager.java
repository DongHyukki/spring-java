package com.zerobase.springjava.support.security.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component("tokenAuthenticationManager")
public class TokenAuthenticationManager implements AuthenticationManager {

    private final AuthenticationProvider authenticationProvider;

    public TokenAuthenticationManager(@Qualifier("tokenAuthenticationProvider") AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticationProvider.authenticate(authentication);
    }
}
