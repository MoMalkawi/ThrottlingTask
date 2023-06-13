package com.atypon.task1.services;

import com.atypon.task1.utils.ThreadingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private @NonNull ThrottlingService throttlingService;

    public <T> CompletableFuture<ResponseEntity<T>> createThrottledResponse(Supplier<T> responseSupplier, int delay, String ipAddress) {
        CompletableFuture<ResponseEntity<T>> futureTask = ThreadingUtils.createAsyncFuture(delay);
        return futureTask.thenApply(result -> {
            throttlingService.finalizeRequestThrottle(ipAddress);
            return ResponseEntity.ok(responseSupplier.get());
        });
    }

    public <T> CompletableFuture<ResponseEntity<T>> createResponse(T response, HttpStatus status) {
        CompletableFuture<ResponseEntity<T>> futureTask = ThreadingUtils.createAsyncFuture(0);
        return futureTask.thenApply(result -> ResponseEntity.status(status).body(response));
    }

    public CompletableFuture<ResponseEntity<String>> serviceUnavailable() {
        return createResponse("Too many requests, Service Unavailable.", HttpStatus.SERVICE_UNAVAILABLE);
    }

}
