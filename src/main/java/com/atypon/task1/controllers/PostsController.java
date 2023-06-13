package com.atypon.task1.controllers;

import com.atypon.task1.services.LocatorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/posts")
@RestController
@RequiredArgsConstructor
public class PostsController {

    private @NonNull LocatorService locatorService;

    @GetMapping
    public ResponseEntity<String> getAddress(HttpServletRequest request) {
        return ResponseEntity.ok(locatorService.getUniformResourceLocator(request));
    }

}
