package com.donghyukki.springjava.support.common.cache;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CacheInfo {

    static final Map<CacheType, List<CacheName>> CACHE_NAMES_BY_TYPE =
            Arrays.stream(CacheName.values())
                    .collect(Collectors.groupingBy(CacheName::getType));

    public enum CacheName {
        USER_INFO(CacheType.IN_MEMORY, null),
        API_CALL(CacheType.REDIS, Optional.of(Duration.ofMinutes(10L))),
        ;

        CacheName(CacheType type, Optional<Duration> ttl) {
            this.type = type;
            this.ttl = ttl;
        }

        private final CacheType type;
        private final Optional<Duration> ttl;

        public CacheType getType() {
            return type;
        }

        public Optional<Duration> getTtl() {
            return ttl;
        }
    }

    public enum CacheType {
        IN_MEMORY,
        REDIS,
    }
}
