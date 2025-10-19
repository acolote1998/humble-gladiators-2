package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.repository.BattleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class BattleService {
    CharacterService characterService;
    BattleRepository battleRepository;

    @Autowired
    public BattleService(
            CharacterService characterService,
            BattleRepository battleRepository) {
        this.characterService = characterService;
        this.battleRepository = battleRepository;
    }

    public boolean isBattleAvailableForToday() {
    }

    public Battle getBattleForTodayByCampaignAndUserId(Long campaignId, String userId) {
        LocalDate today = LocalDate.now();
        return battleRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(campaignId, userId, today);
    }
}
