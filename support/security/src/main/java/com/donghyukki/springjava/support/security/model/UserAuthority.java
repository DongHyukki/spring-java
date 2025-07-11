package com.donghyukki.springjava.support.security.model;

import org.springframework.security.core.GrantedAuthority;

public class UserAuthority implements GrantedAuthority {
    public static final String ROLE = "ROLE_USER";

    @Override
    public String getAuthority() {
        return ROLE;
    }

}
