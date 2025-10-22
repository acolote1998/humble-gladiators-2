package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.repository.BattleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class BattleCleanupService {
    BattleRepository battleRepository;
    BattleService battleService;

    @Autowired
    public BattleCleanupService(BattleRepository battleRepository,
                                BattleService battleService) {
        this.battleRepository = battleRepository;
        this.battleService = battleService;
    }

    @Transactional
    @Scheduled(cron = "0 1 0 * * ?") // Every day at midnight
    public void consolidateOldBattles() {
        List<Battle> oldBattles = battleRepository.findBattlesBeforeDate(LocalDate.now());
        for (Battle battle : oldBattles) {
            log.info("Consolidated battle {} as defeat for user {}", battle.getId(), battle.getUserId());
            consolidateBattle(battle);
        }
        battleRepository.saveAll(oldBattles);
    }

    public void consolidateBattle(Battle battleToConsolidate) {
        battleToConsolidate.setOngoing(false);
        if (battleToConsolidate.getCurrentCharacterToPlay() != null)
            battleToConsolidate.setCurrentCharacterToPlay(null);
        if (battleToConsolidate.getWinningTeam().isEmpty())
            battleToConsolidate.getWinningTeam().add(battleToConsolidate.getTeamTwo().getFirst());
        if (battleToConsolidate.getLosingTeam().isEmpty())
            battleToConsolidate.getLosingTeam().add(battleToConsolidate.getTeamOne().getFirst());
        battleService.fullyRecoverBothTeams(battleToConsolidate);
        battleToConsolidate.getTeamOne().clear();
        battleToConsolidate.getTeamTwo().clear();
    }
}
