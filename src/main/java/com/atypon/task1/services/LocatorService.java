package com.atypon.task1.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class LocatorService {

    public String getUniformResourceLocator(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getHeader("Host") + request.getRequestURI();
    }

}
