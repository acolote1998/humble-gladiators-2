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
            throw new BannedUser("The user '" + campaignToVerify.getUserId() + "' from campaign '" + campaignToVerify.getId() + "' is banned until '" + campaignToVerify.getUserModeration().getBannedUntil() + "' - blocking request", campaignToVerify);
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

    public void banUser(Campaign campaignToCheck) {
        UserModeration userModerationToCheck = campaignToCheck.getUserModeration();
        updateBanStatus(userModerationToCheck);
        LocalDateTime banTime = calculateBanTime(userModerationToCheck);
        userModerationToCheck.setAmountOfInvalidRequests(userModerationToCheck.getAmountOfInvalidRequests() + 1);
        userModerationToCheck.setBannedUntil(banTime);
        userModerationToCheck.setBanned(true);
        log.warn(String.format("""
                        user '%s' got banned until '%s', current amount of invalid requests: '%s'
                        """,
                campaignToCheck.getUserId(),
                userModerationToCheck.getBannedUntil(),
                userModerationToCheck.getAmountOfInvalidRequests()
        ));
        userModerationRepository.save(userModerationToCheck);
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
