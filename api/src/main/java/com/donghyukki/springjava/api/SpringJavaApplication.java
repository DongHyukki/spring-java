package com.donghyukki.springjava.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = "com.donghyukki.springjava"
)
public class SpringJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJavaApplication.class, args);
    }
}
