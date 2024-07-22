package com.zerobase.springjava.support.security.repository;

import com.zerobase.springjava.support.security.model.AuthUser;

public interface AuthUserRepository {
    AuthUser findByUserId(String userId);
}
