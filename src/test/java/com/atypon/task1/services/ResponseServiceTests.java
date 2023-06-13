package com.atypon.task1.services;

import com.atypon.task1.services.ResponseService;
import com.atypon.task1.services.ThrottlingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

@SpringBootTest
class ResponseServiceTests {

    @Autowired
    private ResponseService responseService;

    @MockBean
    private ThrottlingService throttlingService;

    @Test
    void throttledResponseTest() throws ExecutionException, InterruptedException {
        doNothing().when(throttlingService).finalizeRequestThrottle(any());
        long time = System.currentTimeMillis();
        CompletableFuture<ResponseEntity<String>> futureResponse = responseService
                .createThrottledResponse(() -> "http://atypon.com/api/posts", 1000, "127.0.0.1");
        futureResponse.join();
        Assertions.assertEquals("http://atypon.com/api/posts", futureResponse.get().getBody());
        Assertions.assertTrue((System.currentTimeMillis() - time) >= 1000);
    }

    @Test
    void normalResponseTest() throws ExecutionException, InterruptedException {
        CompletableFuture<ResponseEntity<String>> futureResponse = responseService.createResponse("test", HttpStatus.OK);
        ResponseEntity<String> response = futureResponse.get();
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("test", response.getBody());
    }

    @Test
    void serviceUnAvailableResponseTest() throws ExecutionException, InterruptedException {
        CompletableFuture<ResponseEntity<String>> serviceUnavailableResponse = responseService.serviceUnavailable();
        Assertions.assertEquals(503, serviceUnavailableResponse.get().getStatusCode().value());
    }

}
