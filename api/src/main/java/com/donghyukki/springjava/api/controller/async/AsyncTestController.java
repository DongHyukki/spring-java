package com.donghyukki.springjava.api.controller.async;

import com.donghyukki.springjava.support.common.async.AsyncTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Supplier;

@RestController
public class AsyncTestController {
    private final RestTemplate restTemplate;
    private final AsyncTemplate asyncTemplate;

    public AsyncTestController(RestTemplate restTemplate, AsyncTemplate asyncTemplate) {
        this.restTemplate = restTemplate;
        this.asyncTemplate = asyncTemplate;
    }

    @GetMapping("api/async/await-all")
    public ResponseEntity<String> asyncAwaitAll() {
        long start = System.currentTimeMillis();
        var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0LXVzZXItMSIsImV4cCI6MTc1MTMzNjUxNCwiaXNzIjoiemVyby1iYXNlIn0.7WBf-GjBVziLxbUiK7t_HRfYB85j4YfhRzGzhLvcIhc";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Supplier<String> call1 = () -> restTemplate.exchange("http://localhost:8080/api/async/task", HttpMethod.GET, entity, String.class).getBody();
        Supplier<String> call2 = () -> restTemplate.exchange("http://localhost:8080/api/async/task", HttpMethod.GET, entity, String.class).getBody();
        Supplier<String> call3 = () -> restTemplate.exchange("http://localhost:8080/api/async/task", HttpMethod.GET, entity, String.class).getBody();

        var results = asyncTemplate.asyncAwaitAll(List.of(call1, call2, call3));
        long end = System.currentTimeMillis();
        return ResponseEntity.ok("Time: " + (end - start) + "ms");
    }

    @GetMapping("api/async/task")
    public ResponseEntity<String> task() throws InterruptedException {
        Thread.sleep(1000L);
        return ResponseEntity.ok(Thread.currentThread().getName());
    }
}
