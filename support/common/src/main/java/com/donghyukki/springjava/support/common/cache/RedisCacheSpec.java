package com.donghyukki.springjava.support.common.cache;

import java.time.Duration;

public record RedisCacheSpec<T>(
        String cacheName,
        Class<T> clazz,
        Duration ttl
) {
}
