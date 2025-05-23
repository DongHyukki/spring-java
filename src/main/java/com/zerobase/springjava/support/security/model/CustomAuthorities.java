package com.zerobase.springjava.support.security.model;

import org.springframework.security.core.GrantedAuthority;

public class CustomAuthorities {
    private static final GrantedAuthority ADMIN_AUTHORITY = new AdminAuthority();
    private static final GrantedAuthority USER_AUTHORITY = new UserAuthority();
    private static final String ADMIN_AUTHORITY_RAW_VALUE = "ADMIN";
    private static final String USER_AUTHORITY_RAW_VALUE = "USER";

    static GrantedAuthority from(String authority) {
        return switch (authority.toUpperCase()) {
            case ADMIN_AUTHORITY_RAW_VALUE -> ADMIN_AUTHORITY;
            case USER_AUTHORITY_RAW_VALUE -> USER_AUTHORITY;
            default -> throw new IllegalArgumentException("Unknown authority: " + authority);
        };
    }
}
