package com.zerobase.springjava.support.security.repository;

import com.zerobase.springjava.support.security.model.AuthUser;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
class InMemoryAuthUserRepository implements AuthUserRepository {
    private final Map<String, AuthUser> storage = new HashMap<>();

    @PostConstruct
    void initDummyUser() {
        storage.put("test-user-1",
                new AuthUser(
                        "test-user-1",
                        "test-user-name-1",
                        new BCryptPasswordEncoder().encode("test-user-password-1"),
                        List.of("admin")));
    }

    @Override
    public AuthUser findByUserId(String userId) {
        return storage.getOrDefault(
                userId,
                new AuthUser(
                        "test-user-id",
                        "test-name",
                        "test-password",
                        List.of("user")));
    }
}

