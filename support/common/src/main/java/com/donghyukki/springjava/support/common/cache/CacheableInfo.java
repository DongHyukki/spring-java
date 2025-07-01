package com.donghyukki.springjava.support.common.cache;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CacheableInfo {

    static final Map<CacheType, List<CacheableSpec>> CACHE_NAMES_BY_TYPE =
            Arrays.stream(CacheableSpec.values())
                    .collect(Collectors.groupingBy(CacheableSpec::getType));

    public enum CacheableSpec {
        USER_INFO(CacheType.IN_MEMORY, null),
        API_CALL(CacheType.REDIS, Optional.of(Duration.ofMinutes(10L))),
        ;

        CacheableSpec(CacheType type, Optional<Duration> ttl) {
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
