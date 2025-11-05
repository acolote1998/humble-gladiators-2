package com.github.acolote1998.humble_gladiators_2.core.controller.integration;

import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class GeminiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeminiService geminiService;

    @Test
    void status_verifiesServiceIntegration() throws Exception {
        String expectedResponse = "Gemini API is working";
        when(geminiService.sendTestPrompt()).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/public/gemini/status"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void status_isPublicEndpoint_noAuthenticationRequired() throws Exception {
        String expectedResponse = "Test response";
        when(geminiService.sendTestPrompt()).thenReturn(expectedResponse);

        // No JWT token required - public endpoint
        mockMvc.perform(get("/api/public/gemini/status"))
                .andExpect(status().isOk());
    }
}

