package com.donghyukki.springjava.domain.support.cache;

import com.donghyukki.springjava.support.common.cache.RedisCacheSpec;
import com.donghyukki.springjava.support.database.entity.Company;

import java.time.Duration;

public enum DomainCacheSpec {
    GET_COMPANY(new RedisCacheSpec<>("GET_COMPANY", Company.class, Duration.ofMinutes(10L)));

    DomainCacheSpec(RedisCacheSpec<?> cacheSpec) {
        this.cacheSpec = cacheSpec;
    }

    private final RedisCacheSpec<?> cacheSpec;

    public RedisCacheSpec<?> getCacheSpec() {
        return cacheSpec;
    }
}
