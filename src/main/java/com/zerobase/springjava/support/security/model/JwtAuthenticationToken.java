package com.zerobase.springjava.support.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final TokenAuthenticatedPrincipal principal;

    public JwtAuthenticationToken(TokenAuthenticatedPrincipal principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(TokenAuthenticatedPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public static JwtAuthenticationToken unauthenticated(TokenAuthenticatedPrincipal principal) {
        return new JwtAuthenticationToken(principal);
    }

    public static JwtAuthenticationToken authenticated(String userId, Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(TokenAuthenticatedPrincipal.fromUserId(userId), authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
