package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.GeminiPromptValidationResponse;
import com.github.acolote1998.humble_gladiators_2.core.exception.BannedUser;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.UserModeration;
import com.github.acolote1998.humble_gladiators_2.core.repository.UserModerationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserModerationServiceTest {

    @Mock
    GeminiService geminiService;

    @Mock
    UserModerationRepository userModerationRepository;

    @InjectMocks
    UserModerationService userModerationService;


    @Test
    void validPromptReturnsTrue() {
        String validPrompt = "I am a valid prompt";
        Campaign validCampaign = new Campaign();
        validCampaign.setUserId("validUser");

        GeminiPromptValidationResponse validValidationResponse = new GeminiPromptValidationResponse(true);

        when(geminiService.verifyPromptValidity(anyString())).thenReturn(validValidationResponse);
        when(userModerationRepository.findAllByUserIdAndBanned(anyString(), anyBoolean())).thenReturn(new ArrayList<>());

        Boolean result = userModerationService.verifyPromptValidity(validPrompt, validCampaign);

        assertTrue(result);
    }

    @Test
    void InvalidUserThrowsBannedUserException() {
        String validPrompt = "I am a valid prompt";
        Campaign validCampaign = new Campaign();
        validCampaign.setUserId("bannedUser");
        UserModeration userModeration = new UserModeration();
        userModeration.setAmountOfInvalidRequests(0);
        userModeration.setBanned(false);
        userModeration.setUserId(validCampaign.getUserId());
        userModeration.setLastInvalidRequest(null);
        validCampaign.setUserModeration(userModeration);

        when(userModerationRepository.findAllByUserIdAndBanned(anyString(), anyBoolean())).thenReturn(List.of(new UserModeration()));

        assertThrows(BannedUser.class, () -> {
            userModerationService.verifyPromptValidity(validPrompt, validCampaign);
        });
    }

    @Test
    void InvalidPromptBansTheUser() {
        String invalidPrompt = "I am a prompt with bad intentions";
        Campaign validCampaign = new Campaign();
        validCampaign.setUserId("soonBannedUser");
        UserModeration userModeration = new UserModeration();
        userModeration.setAmountOfInvalidRequests(0);
        userModeration.setBanned(false);
        userModeration.setUserId(validCampaign.getUserId());
        userModeration.setLastInvalidRequest(null);
        validCampaign.setUserModeration(userModeration);
        GeminiPromptValidationResponse invalidValidationResponse = new GeminiPromptValidationResponse(false);

        when(userModerationRepository.findAllByUserIdAndBanned(anyString(), anyBoolean())).thenReturn(new ArrayList<>());
        when(geminiService.verifyPromptValidity(anyString())).thenReturn(invalidValidationResponse);

        assertTrue(userModerationService.isValidUser(validCampaign));
        assertFalse(userModerationService.verifyPromptValidity(invalidPrompt, validCampaign));
        when(userModerationRepository.findAllByUserIdAndBanned(anyString(), anyBoolean())).thenReturn(List.of(new UserModeration()));
        assertFalse(userModerationService.isValidUser(validCampaign));
        assertThrows(BannedUser.class, () -> {
            userModerationService.verifyPromptValidity(invalidPrompt, validCampaign);
        });
    }

}