package com.atypon.task1.integration;

import com.atypon.task1.utils.RequestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTests {

    @Autowired
    private MockMvc mock;

    @Test
    void preThrottlingTest() throws Exception {
        MockHttpServletRequest request = RequestUtils.generateMockAtyponRequest("/api/profile");
        MvcResult result = performAsyncRequest(request, "127.0.0.2");
        mock.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().string("http://atypon.com/api/profile"));
    }

    @Test
    void throttlingTest() throws Exception {
        MockHttpServletRequest request = RequestUtils.generateMockAtyponRequest("/api/profile");
        long time;
        for(int i = 0; i < 10; i++) {
            time = System.currentTimeMillis();
            MvcResult result = performAsyncRequest(request, "127.0.0.3");
            fetchAndValidateAsyncResponse(result);
            Assertions.assertTrue((System.currentTimeMillis() - time) < 3000);
        }
        time = System.currentTimeMillis();
        MvcResult result = performAsyncRequest(request, "127.0.0.3");
        fetchAndValidateAsyncResponse(result);
        Assertions.assertTrue((System.currentTimeMillis() - time) >= 3000);
    }

    @Test
    void ipThrottlingIsolationTest() throws Exception {
        MockHttpServletRequest request = RequestUtils.generateMockAtyponRequest("/api/profile");
        long time;
        for(int i = 0; i <= 10; i++) {
            time = System.currentTimeMillis();
            MvcResult result = performAsyncRequest(request, i == 10 ? "127.0.0.5" : "127.0.0.4");
            fetchAndValidateAsyncResponse(result);
            Assertions.assertTrue((System.currentTimeMillis() - time) < 3000);
        }
    }

    private MvcResult performAsyncRequest(MockHttpServletRequest request, String ipAddress) throws Exception {
        return mock.perform(get("/api/profile")
                        .header("Host", "atypon.com")
                        .requestAttr("jakarta.servlet.http.HttpServletRequest", request)
                        .with(postProcessor -> {
                            postProcessor.setRemoteAddr(ipAddress);
                            return postProcessor;
                        }))
                .andExpect(request().asyncStarted())
                .andReturn();
    }

    private void fetchAndValidateAsyncResponse(MvcResult originRequest) throws Exception {
        mock.perform(asyncDispatch(originRequest)).andExpect(status().isOk())
                .andExpect(content().string("http://atypon.com/api/profile"));
    }

}
