package com.zerobase.springjava.support.security.model;

import org.springframework.security.core.GrantedAuthority;

public class AdminAuthority implements GrantedAuthority {
    private static final String ROLE = "ROLE_ADMIN";

    @Override
    public String getAuthority() {
        return ROLE;
    }

}
