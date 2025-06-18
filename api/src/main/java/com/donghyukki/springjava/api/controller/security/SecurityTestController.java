package com.donghyukki.springjava.api.controller.security;

import com.donghyukki.springjava.support.common.jwt.JsonWebToken;
import com.donghyukki.springjava.support.common.jwt.JsonWebTokenManager;
import com.donghyukki.springjava.support.security.annotations.AllowedAdmin;
import com.donghyukki.springjava.support.security.annotations.AllowedSuperAdmin;
import com.donghyukki.springjava.support.security.annotations.AllowedUserOrAdmin;
import com.donghyukki.springjava.support.security.annotations.CurrentUserId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class SecurityTestController {
    private final JsonWebTokenManager jsonWebTokenManager;

    public SecurityTestController(JsonWebTokenManager jsonWebTokenManager) {
        this.jsonWebTokenManager = jsonWebTokenManager;
    }

    @GetMapping("/api/test")
    public String api() {
        return "SUCCESS";
    }

    @PostMapping("/login")
    public JsonWebToken login() {
        return jsonWebTokenManager.createToken("test-user-1", Instant.now().plusMillis(1000 * 60 * 60));
    }

    @GetMapping("/api/admin/test")
    @AllowedAdmin
    public String admin(@CurrentUserId String userId) {
        System.out.println(userId);
        return userId;
    }

    @AllowedUserOrAdmin
    @GetMapping("/api/user/test")
    public String user(@CurrentUserId String userId) {
        System.out.println(userId);
        return userId;
    }

    @AllowedSuperAdmin
    @GetMapping("/api/super-admin/test")
    public String superAdmin(@CurrentUserId String userId) {
        System.out.println(userId);
        return userId;
    }
}
