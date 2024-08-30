package com.zerobase.springjava.support.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/api/test")
    public String api() {
        return "SUCCESS";
    }

    @PostMapping("/login")
    public String login() {
        return "LOGIN_SUCCESS";
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
