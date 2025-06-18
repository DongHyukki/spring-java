package com.donghyukki.springjava.support.security.repository;

import com.donghyukki.springjava.support.security.model.AuthUser;

public interface AuthUserRepository {
    AuthUser findByUserId(String userId);
}
