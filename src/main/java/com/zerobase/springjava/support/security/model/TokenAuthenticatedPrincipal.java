package com.zerobase.springjava.support.security.model;

import org.springframework.security.core.AuthenticatedPrincipal;

import java.util.Optional;

public class TokenAuthenticatedPrincipal implements AuthenticatedPrincipal {
    private final Optional<String> userId;
    private final String token;

    public TokenAuthenticatedPrincipal(String token) {
        this.token = token;
        this.userId = Optional.empty();
    }

    public TokenAuthenticatedPrincipal(String userId, String token) {
        this.userId = Optional.of(userId);
        this.token = token;
    }

    public static TokenAuthenticatedPrincipal fromUserId(String userId) {
        return new TokenAuthenticatedPrincipal(
                userId,
                "erased-token"
        );
    }

    @Override
    public String getName() {
        return userId.orElseThrow(() -> new IllegalCallerException("인증되지 않은 Principal 의 getName 을 호출 할 수 없습니다."));
    }

    public String getUserId() {
        return userId.orElseThrow(() -> new IllegalCallerException("인증되지 않은 Principal 의 getUserId 를 호출 할 수 없습니다."));
    }

    public String getToken() {
        return token;
    }

}
