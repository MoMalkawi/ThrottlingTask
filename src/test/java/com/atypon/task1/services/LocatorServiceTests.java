package com.atypon.task1.services;

import com.atypon.task1.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LocatorServiceTests {

    @Autowired
    private LocatorService locatorService;

    @Test
    void getAddressTest() {
        HttpServletRequest postsRequest = RequestUtils.generateMockAtyponRequest("/api/posts");
        Assertions.assertEquals("http://atypon.com/api/posts",
                locatorService.getUniformResourceLocator(postsRequest));
    }

}
