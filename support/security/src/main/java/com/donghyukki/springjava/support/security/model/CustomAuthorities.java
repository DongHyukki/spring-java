package com.donghyukki.springjava.support.security.model;

import org.springframework.security.core.GrantedAuthority;

public class CustomAuthorities {
    private static final GrantedAuthority ADMIN_AUTHORITY = new AdminAuthority();
    private static final GrantedAuthority USER_AUTHORITY = new UserAuthority();
    private static final GrantedAuthority SUPER_ADMIN_AUTHORITY = new SuperAdminAuthority();
    private static final String ADMIN_AUTHORITY_RAW_VALUE = "ADMIN";
    private static final String USER_AUTHORITY_RAW_VALUE = "USER";
    private static final String SUPER_ADMIN_AUTHORITY_RAW_VALUE = "SUPER_ADMIN";

    static GrantedAuthority from(String authority) {
        return switch (authority.toUpperCase()) {
            case ADMIN_AUTHORITY_RAW_VALUE -> ADMIN_AUTHORITY;
            case USER_AUTHORITY_RAW_VALUE -> USER_AUTHORITY;
            case SUPER_ADMIN_AUTHORITY_RAW_VALUE -> SUPER_ADMIN_AUTHORITY;
            default -> throw new IllegalArgumentException("Unknown authority: " + authority);
        };
    }
}
