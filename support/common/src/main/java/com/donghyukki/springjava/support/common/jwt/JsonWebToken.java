package com.donghyukki.springjava.support.common.jwt;

public record JsonWebToken(String value) {
    public static JsonWebToken of(String value) {
        return new JsonWebToken(value);
    }
}
