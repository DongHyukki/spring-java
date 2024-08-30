package com.zerobase.springjava.support.security.support.jwt;

public record JsonWebToken(String value) {
    public static JsonWebToken of(String value) {
        return new JsonWebToken(value);
    }
}
