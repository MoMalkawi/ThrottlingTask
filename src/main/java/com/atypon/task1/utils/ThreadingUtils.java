package com.atypon.task1.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ThreadingUtils {

    private ThreadingUtils() {}

    public static <T> CompletableFuture<T> createAsyncFuture(int delay) {
        if(delay <= 0)
            return createAsyncPresent();
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeAsync(() -> null,
                CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS));
        return future;
    }

    public static <T> CompletableFuture<T> createAsyncPresent() {
        CompletableFuture<T> present = new CompletableFuture<>();
        present.complete(null);
        return present;
    }

}
