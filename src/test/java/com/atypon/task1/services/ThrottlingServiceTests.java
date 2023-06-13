package com.atypon.task1.services;

import com.atypon.task1.services.ThrottlingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThrottlingServiceTests {

    @Autowired
    private ThrottlingService throttlingService;

    @Test
    void throttlingRequestsTest() {
        String ipAddress = "127.0.0.1";
        int maxRequestsBeforeThrottling = 10;
        int intervalTime = 60000;
        for(int i = 0; i < 10; i++)
            Assertions.assertFalse(throttlingService.throttledRequest(ipAddress,
                    maxRequestsBeforeThrottling, intervalTime));
        Assertions.assertTrue(throttlingService.throttledRequest(ipAddress,
                maxRequestsBeforeThrottling, intervalTime));
    }

    @Test
    void ipThrottlingIsolationTest() {
        String ipAddress = "127.0.0.2";
        int maxRequestsBeforeThrottling = 10;
        int intervalTime = 60000;
        for(int i = 0; i < 10; i++)
            Assertions.assertFalse(throttlingService.throttledRequest(ipAddress,
                    maxRequestsBeforeThrottling, intervalTime));
        ipAddress = "127.0.0.3";
        Assertions.assertFalse(throttlingService.throttledRequest(ipAddress,
                maxRequestsBeforeThrottling, intervalTime));
    }

    @Test
    void overQueuedServiceUnavailableTest() {
        String ipAddress = "127.0.0.5";
        int maxRequestsBeforeThrottling = 10;
        int intervalTime = 60000;
        for(int i = 0; i < 10; i++)
            Assertions.assertFalse(throttlingService.throttledRequest(ipAddress,
                    maxRequestsBeforeThrottling, intervalTime));
        for(int i = 0; i <= 10; i++)
            Assertions.assertTrue(throttlingService.throttledRequest(ipAddress,
                    maxRequestsBeforeThrottling, intervalTime));
        Assertions.assertTrue(throttlingService.exceedsThrottlingLimit(ipAddress, 10));
    }

}
