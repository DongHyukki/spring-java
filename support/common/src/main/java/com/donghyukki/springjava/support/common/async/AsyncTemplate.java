package com.donghyukki.springjava.support.common.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Component
public class AsyncTemplate {
    private static final Logger log = LoggerFactory.getLogger(AsyncTemplate.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public <T> List<T> asyncAwaitAll(List<Supplier<T>> tasks) {
        List<CompletableFuture<T>> futures = tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(() -> {
                    try {
                        log.info("Awaiting task: {}", task.getClass().getName());
                        return task.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, executorService))
                .toList();

        return futures
                .stream()
                .map(CompletableFuture::join)
                .toList();
    }
}
