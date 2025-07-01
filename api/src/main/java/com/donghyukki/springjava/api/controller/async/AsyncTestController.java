package com.donghyukki.springjava.api.controller.async;

import com.donghyukki.springjava.support.common.async.AsyncTemplate;
import com.donghyukki.springjava.support.common.jwt.JsonWebTokenManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

@RestController
public class AsyncTestController {
    private final RestTemplate restTemplate;
    private final AsyncTemplate asyncTemplate;
    private final JsonWebTokenManager jsonWebTokenManager;

    public AsyncTestController(RestTemplate restTemplate, AsyncTemplate asyncTemplate, JsonWebTokenManager jsonWebTokenManager) {
        this.restTemplate = restTemplate;
        this.asyncTemplate = asyncTemplate;
        this.jsonWebTokenManager = jsonWebTokenManager;
    }

    @GetMapping("api/async/await-all")
    public ResponseEntity<String> asyncAwaitAll() {
        long start = System.currentTimeMillis();
        var token = jsonWebTokenManager.createToken("test-user-1", Instant.now().plusMillis(1000 * 60 * 60));

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
