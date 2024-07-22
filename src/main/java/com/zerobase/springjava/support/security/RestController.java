package com.zerobase.springjava.support.security;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/api/test")
    public String api() {
        System.out.println("kk");
        return "SUCCESS";
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
