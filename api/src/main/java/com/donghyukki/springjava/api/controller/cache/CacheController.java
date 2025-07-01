package com.donghyukki.springjava.api.controller.cache;

import com.donghyukki.springjava.domain.service.cache.CacheCheckService;
import com.donghyukki.springjava.support.common.cache.CacheableInfo;
import com.donghyukki.springjava.support.database.entity.Company;
import com.donghyukki.springjava.support.security.annotations.AllowedUserOrAdmin;
import com.donghyukki.springjava.support.security.annotations.CurrentUserId;
import com.donghyukki.springjava.support.security.model.AuthUser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CacheController {

    private final CacheCheckService cacheCheckService;
    private final ConcurrentMapCacheManager inMemoryCacheManager;
    private final CacheManager redisCacheManager;

    public CacheController(
            CacheCheckService cacheCheckService,
            @Qualifier("inMemoryCacheManager") CacheManager inMemoryCacheManager,
            @Qualifier("redisCacheManager") CacheManager redisCacheManager
    ) {
        this.cacheCheckService = cacheCheckService;
        this.inMemoryCacheManager = (ConcurrentMapCacheManager) inMemoryCacheManager;
        this.redisCacheManager = redisCacheManager;
    }

    @AllowedUserOrAdmin
    @GetMapping("/api/cache/in-memory")
    public ResponseEntity<AuthUser> cacheInMemory(@CurrentUserId String userId) {
        var authUser = Optional.ofNullable(inMemoryCacheManager.getCache(CacheableInfo.CacheableSpec.USER_INFO.name()).get(userId, AuthUser.class));
        if (authUser.isPresent()) {
            System.out.println("cached user::" + userId);
        }
        return ResponseEntity.ok(cacheCheckService.getUser(userId));
    }

    @AllowedUserOrAdmin
    @GetMapping("/api/cache/redis")
    public ResponseEntity<String> cacheRedis(@CurrentUserId String userId) {
        var cache = Optional.ofNullable(redisCacheManager.getCache(CacheableInfo.CacheableSpec.API_CALL.name()).get(userId, String.class));
        if (cache.isPresent()) {
            System.out.println("cache.get() = " + cache.get());
        }
        return ResponseEntity.ok(cacheCheckService.callApi(userId));
    }

    @AllowedUserOrAdmin
    @GetMapping("/api/cache/template")
    public ResponseEntity<Company> cacheTemplate(@CurrentUserId String userId) {
        return ResponseEntity.ok(cacheCheckService.getCompany("TEST-COMPANY-1"));
    }
}
