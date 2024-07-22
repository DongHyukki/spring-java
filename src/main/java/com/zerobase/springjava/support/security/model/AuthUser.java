package com.zerobase.springjava.support.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public final class AuthUser implements UserDetails {

    private final String id;
    private final String name;
    private final String password;
    private final Collection<String> authorities;

    public AuthUser(String id, String name, String password, Collection<String> authorities) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.authorities = authorities;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(CustomAuthorities::from).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }
}
