package com.donghyukki.springjava.domain.support.cache;

import java.util.function.Supplier;

public interface DomainCacheService {
    <T> T cache(DomainCacheSpec domainCacheSpec, String key, Supplier<T> supplier);
}
