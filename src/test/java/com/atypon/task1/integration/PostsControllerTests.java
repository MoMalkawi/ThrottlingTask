package com.atypon.task1.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostsControllerTests {

    @Autowired
    private MockMvc mock;

    @Test
    void testPostsController() throws Exception {
        mock.perform(get("/api/posts").header("Host", "localhost:8080"))
                .andExpect(status().isOk())
                .andExpect(content().string("http://localhost:8080/api/posts"));
    }

}

