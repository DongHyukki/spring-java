package com.donghyukki.springjava.support.security.model;

import org.springframework.security.core.GrantedAuthority;

public class SuperAdminAuthority implements GrantedAuthority {
    public static final String ROLE = "ROLE_SUPER_ADMIN";

    @Override
    public String getAuthority() {
        return ROLE;
    }
}
