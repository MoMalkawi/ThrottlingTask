package com.atypon.task1.utils;

import org.springframework.mock.web.MockHttpServletRequest;

public class RequestUtils {

    private RequestUtils() {}

    public static MockHttpServletRequest generateMockAtyponRequest(String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("http");
        request.addHeader("Host", "atypon.com");
        request.setRequestURI(uri);
        return request;
    }

}
