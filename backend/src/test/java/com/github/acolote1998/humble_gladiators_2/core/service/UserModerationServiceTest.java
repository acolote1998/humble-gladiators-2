package com.github.acolote1998.humble_gladiators_2.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserModerationServiceTest {

    @Mock
    GeminiService geminiService;

    @InjectMocks
    UserModerationService userModerationService;

    @Test
    void validPromptReturnsTrue() {
        String validPrompt = "I am a valid prompt";

        when(geminiService.verifyPromptValidity(anyString())).thenReturn(true);

        Boolean result = userModerationService.verifyPromptValidity(validPrompt);

        assertTrue(result);
    }

}