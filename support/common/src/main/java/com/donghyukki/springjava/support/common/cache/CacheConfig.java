package com.donghyukki.springjava.support.common.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.stream.Collectors;

import static com.donghyukki.springjava.support.common.cache.CacheInfo.CACHE_NAMES_BY_TYPE;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final Duration REDIS_DEFAULT_TTL = Duration.ofMinutes(10);

    @Bean("inMemoryCacheManager")
    public CacheManager inMemoryCacheManager() {
        var cacheNames = CACHE_NAMES_BY_TYPE.get(CacheInfo.CacheType.IN_MEMORY)
                .stream()
                .map((Enum::name))
                .toArray(String[]::new);

        return new ConcurrentMapCacheManager(cacheNames);
    }

    @Primary
    @Bean("redisCacheManager")
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        var cacheNames = CACHE_NAMES_BY_TYPE.get(CacheInfo.CacheType.REDIS);

        var configMap = cacheNames.stream()
                .collect(Collectors.toMap(
                        Enum::name,
                        cacheName -> RedisCacheConfiguration
                                .defaultCacheConfig()
                                .entryTtl(cacheName.getTtl().orElseGet(() -> REDIS_DEFAULT_TTL))
                ));

        return RedisCacheManager.builder(redisConnectionFactory)
                .withInitialCacheConfigurations(configMap)
                .build();
    }
}
