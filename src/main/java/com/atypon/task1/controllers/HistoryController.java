package com.atypon.task1.controllers;

import com.atypon.task1.services.LocatorService;
import com.atypon.task1.services.ResponseService;
import com.atypon.task1.services.ThrottlingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@RequestMapping("/api/history")
@Controller
@RequiredArgsConstructor
public class HistoryController {

    private @NonNull ThrottlingService throttlingService;

    private @NonNull ResponseService responseService;

    private @NonNull LocatorService locatorService;

    @GetMapping
    public CompletableFuture<ResponseEntity<String>> getAddress(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        if(throttlingService.exceedsThrottlingLimit(ipAddress, 30))
            return responseService.serviceUnavailable();
        Supplier<String> locationSupplier = () -> locatorService.getUniformResourceLocator(request);
        if(throttlingService.throttledRequest(ipAddress, 30, 60000))
            return responseService.createThrottledResponse(locationSupplier, 3000, ipAddress);
        return responseService.createResponse(locationSupplier.get(), HttpStatus.OK);
    }

}
