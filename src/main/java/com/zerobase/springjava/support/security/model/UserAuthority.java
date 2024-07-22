package com.zerobase.springjava.support.security.model;

import org.springframework.security.core.GrantedAuthority;

public class UserAuthority implements GrantedAuthority {
    private static final String ROLE = "ROLE_USER";

    @Override
    public String getAuthority() {
        return ROLE;
    }

}
