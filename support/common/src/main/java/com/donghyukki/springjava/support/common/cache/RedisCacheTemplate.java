package com.donghyukki.springjava.support.common.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class RedisCacheTemplate {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisCacheTemplate(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> T cache(RedisCacheSpec<T> redisCacheSpec, String key, Supplier<T> supplier) throws JsonProcessingException {
        var redisKey = generateKey(redisCacheSpec.cacheName(), key);

        if (redisTemplate.hasKey(redisKey)) {
            var value = redisTemplate.opsForValue().get(redisKey);
            System.out.println("CACHE HIT");
            return objectMapper.readValue(value, redisCacheSpec.clazz());
        }

        if (redisCacheSpec.clazz() == Void.class) {
            throw new IllegalArgumentException("void type cannot be cached");
        }

        var originalResponse = supplier.get();
        var jsonStr = objectMapper.writeValueAsString(originalResponse);

        redisTemplate.opsForValue().set(redisKey, jsonStr, redisCacheSpec.ttl());

        return originalResponse;
    }

    private String generateKey(String name, String key) {
        return name + "::" + key;
    }
}
