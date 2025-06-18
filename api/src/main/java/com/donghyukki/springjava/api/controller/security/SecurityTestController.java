package com.donghyukki.springjava.api.controller.security;

import com.donghyukki.springjava.support.security.support.common.jwt.JsonWebToken;
import com.donghyukki.springjava.support.security.support.common.jwt.JsonWebTokenManager;
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

    @GetMapping("/admin/test")
    public void admin() {
        System.out.println("ss");
    }

    @GetMapping("/test/test")
    public void test() {
        System.out.println("all");
    }
}
