package com.atypon.task1.integration;

import com.atypon.task1.utils.RequestUtils;
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
class HistoryControllerTests {

    @Autowired
    private MockMvc mock;

    @Test
    void serviceUnavailableTest() throws Exception {
        MockHttpServletRequest request = RequestUtils.generateMockAtyponRequest("/api/history");
        for(int i = 0; i <= 60; i++) {
            performAsyncRequest(request);
        }
        MvcResult result = performAsyncRequest(request);
        mock.perform(asyncDispatch(result)).andExpect(status().isServiceUnavailable());
    }

    private MvcResult performAsyncRequest(MockHttpServletRequest request) throws Exception {
        return mock.perform(get("/api/history")
                        .header("Host", "atypon.com")
                        .requestAttr("jakarta.servlet.http.HttpServletRequest", request)
                        .with(postProcessor -> {
                            postProcessor.setRemoteAddr("127.0.0.2");
                            return postProcessor;
                        }))
                .andExpect(request().asyncStarted())
                .andReturn();
    }

}
