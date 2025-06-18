package com.donghyukki.springjava.support.security.model;

import org.springframework.security.core.GrantedAuthority;

public class AdminAuthority implements GrantedAuthority {
    public static final String ROLE = "ROLE_ADMIN";

    @Override
    public String getAuthority() {
        return ROLE;
    }

}
