package com.donghyukki.springjava.support.common.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Cacheable(cacheManager = "redisCacheManager")
public @interface RedisCacheable {

    @AliasFor(annotation = Cacheable.class, attribute = "cacheNames")
    String[] cacheNames();

    @AliasFor(annotation = Cacheable.class, attribute = "key")
    String key() default "";
}

