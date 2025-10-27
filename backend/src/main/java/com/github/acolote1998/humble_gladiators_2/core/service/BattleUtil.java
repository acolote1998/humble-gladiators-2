package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidBattle;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.repository.BattleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class BattleUtil {

    BattleRepository battleRepository;

    public BattleUtil(BattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

    public Battle getOnGoingBattleForTodayByCampaignAndUserId(Long campaignId, String userId) {
        LocalDate today = LocalDate.now();
        Battle todaysBattle = battleRepository.findOnGoingByCampaignIdAndUserIdAndUpdatedAtDate(campaignId, userId, today);
        if (todaysBattle == null) {
            log.error("Campaign '{}' - Battle for today not found", campaignId);
            throw new InvalidBattle("Battle for today not found");
        }
        return todaysBattle;
    }

    public Battle getFinishedBattleForTodayByCampaignAndUserId(Long campaignId, String userId) {
        LocalDate today = LocalDate.now();
        Battle todaysBattle = battleRepository.findFinishedByCampaignIdAndUserIdAndUpdatedAtDate(campaignId, userId, today);
        if (todaysBattle == null) {
            log.error("Campaign '{}' - Battle finished for today not found", campaignId);
            throw new InvalidBattle("Battle finished for today not found");
        }
        return todaysBattle;
    }

    public boolean isThereOngoingBattleForToday(Long campaignId, String userId) {
        Battle battleToCheck = null;
        try {
            battleToCheck = getOnGoingBattleForTodayByCampaignAndUserId(campaignId, userId);
        } catch (InvalidBattle e) {
            log.info("There is no battle ongoing for today in campaign '{}'", campaignId);
        }
        return battleToCheck != null;
    }
}
