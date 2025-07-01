package com.donghyukki.springjava.support.common.async;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Component
public class AsyncTemplate {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public List<?> asyncAwaitAll(List<Supplier<?>> tasks) {
        var mdcContext = MDC.getCopyOfContextMap();
        List<? extends CompletableFuture<?>> futures = tasks
                .stream()
                .map(task -> CompletableFuture.supplyAsync(
                        () -> {
                            try {
                                MDC.setContextMap(mdcContext);
                                return task.get();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            } finally {
                                MDC.clear();
                            }
                        }, executorService))
                .toList();

        return futures
                .stream()
                .map(CompletableFuture::join)
                .toList();
    }
}
