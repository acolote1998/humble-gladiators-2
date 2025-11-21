package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.exception.BannedUser;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.UserModeration;
import com.github.acolote1998.humble_gladiators_2.core.repository.UserModerationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            throw new BannedUser("The user '" + campaignToVerify.getUserId() + "' from campaign '" + campaignToVerify.getId() + "' is banned - blocking request");
        }
        boolean isPromptValidFromGemini = geminiService.verifyPromptValidity(promptToVerify).valid();
        if (!isPromptValidFromGemini) {
            banUser(campaignToVerify);
        }
        return isPromptValidFromGemini;
    }

    public Boolean isValidUser(Campaign campaignToVerify) {
        List<UserModeration> allBannedUserAppearances = userModerationRepository.findAllByUserIdAndBanned(campaignToVerify.getUserId(), true);
        return allBannedUserAppearances.isEmpty();
    }

    public void banUser(Campaign userFromCampaignToBan) {
        UserModeration userToModerate = userFromCampaignToBan.getUserModeration();
        updateBanStatus(userToModerate);
        LocalDateTime banTime = calculateBanTime(userToModerate);
        userToModerate.setAmountOfInvalidRequests(userToModerate.getAmountOfInvalidRequests() + 1);
        userToModerate.setBannedUntil(banTime);
        userToModerate.setBanned(true);
        userModerationRepository.save(userToModerate);
    }

    public void updateBanStatus(UserModeration userToModerate) {
        if (userToModerate.getBanned()) {
            if (LocalDateTime.now().isAfter(userToModerate.getBannedUntil())) {
                userToModerate.setBanned(false);
            }
        }
    }

    public LocalDateTime calculateBanTime(UserModeration userModeration) {
        return switch (userModeration.getAmountOfInvalidRequests()) {
            case null -> LocalDateTime.now().plusMinutes(5);
            case 1 -> LocalDateTime.now().plusDays(1);
            case 2 -> LocalDateTime.now().plusDays(7);
            case 3 -> LocalDateTime.now().plusMonths(1);
            default -> LocalDateTime.now().plusMinutes(5);
        };
    }

}
