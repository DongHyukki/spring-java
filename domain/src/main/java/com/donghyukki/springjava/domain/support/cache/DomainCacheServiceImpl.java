package com.donghyukki.springjava.domain.support.cache;

import com.donghyukki.springjava.support.common.cache.RedisCacheSpec;
import com.donghyukki.springjava.support.common.cache.RedisCacheTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class DomainCacheServiceImpl implements DomainCacheService {
    private final RedisCacheTemplate redisCacheTemplate;

    public DomainCacheServiceImpl(RedisCacheTemplate redisCacheTemplate) {
        this.redisCacheTemplate = redisCacheTemplate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T cache(DomainCacheSpec domainCacheSpec, String key, Supplier<T> supplier) {
        RedisCacheSpec<T> spec = (RedisCacheSpec<T>) domainCacheSpec.getCacheSpec();
        try {
            return redisCacheTemplate.cache(spec, key, supplier);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
