package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.UserModeration;
import com.github.acolote1998.humble_gladiators_2.core.repository.UserModerationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserModerationService {
    GeminiService geminiService;
    UserModerationRepository userModerationRepository;

    @Autowired
    public UserModerationService(GeminiService geminiService, UserModerationRepository userModerationRepository) {
        this.geminiService = geminiService;
        this.userModerationRepository = userModerationRepository;
    }

    public Boolean verifyPromptValidity(String promptToVerify, Campaign campaignToVerify) {
        if (!isValidUser(campaignToVerify)) {
        }
        return geminiService.verifyPromptValidity(promptToVerify).valid();
    }

    public Boolean isValidUser(Campaign campaignToVerify) {
        List<UserModeration> allBannedUserAppearances = userModerationRepository.findAllByUserIdAndBanned(campaignToVerify.getUserId(), true);
        return allBannedUserAppearances.isEmpty();
    }

}
