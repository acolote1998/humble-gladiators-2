package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunwareServiceTest {

    @Mock
    private GeminiService geminiService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RunwareService runwareService;

    private Campaign campaign;
    private static final String API_KEY = "test-api-key";
    private static final String IMG_GEN_URL = "http://test-url.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(runwareService, "API_KEY", API_KEY);
        ReflectionTestUtils.setField(runwareService, "IMG_GEN_URL", IMG_GEN_URL);
        
        campaign = new Campaign();
        campaign.setId(1L);
    }

    @Test
    void getRequestBody_ShouldCreateRequestBodyWithCorrectStructure() throws Exception {
        // Arrange
        String prompt = "test prompt";
        String negativePrompt = "test negative";
        Integer width = 768;
        Integer height = 576;

        // Act - Use reflection to call private method for testing
        java.lang.reflect.Method method = RunwareService.class.getDeclaredMethod(
                "getRequestBody", String.class, String.class, Integer.class, Integer.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.List<java.util.HashMap<String, Object>> result = 
                (java.util.List<java.util.HashMap<String, Object>>) method.invoke(runwareService, prompt, negativePrompt, width, height);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        java.util.HashMap<String, Object> body = result.get(0);
        assertEquals("imageInference", body.get("taskType"));
        assertEquals(prompt, body.get("positivePrompt"));
        assertEquals(negativePrompt, body.get("negativePrompt"));
        assertEquals(width, body.get("width"));
        assertEquals(height, body.get("height"));
    }
}



