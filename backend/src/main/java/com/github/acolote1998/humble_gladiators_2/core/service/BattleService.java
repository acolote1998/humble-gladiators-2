package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.exception.DailyEnemyNotFound;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidBattle;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.model.Turn;
import com.github.acolote1998.humble_gladiators_2.core.repository.BattleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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

    public Battle createNewBattle(Long campaignId, String userId) {
        if (battleRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(campaignId, userId, LocalDate.now()) != null) {
            throw new InvalidBattle("You already have a battle today. Come back tomorrow");
        }
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        CharacterInstance enemy = characterService.getDailyEnemy(campaignId, userId);
        List<Turn> emptyTurnList = new ArrayList<>();
        List<CharacterInstance> teamOne = new ArrayList<>();
        teamOne.add(hero);
        List<CharacterInstance> teamTwo = new ArrayList<>();
        teamTwo.add(enemy);
        List<CharacterInstance> winningTeam = new ArrayList<>();
        List<CharacterInstance> losingTeam = new ArrayList<>();
        Battle newBattle = new Battle();
        newBattle.setCampaign(hero.getCampaign());
        newBattle.setUserId(userId);
        newBattle.setTurns(emptyTurnList);
        newBattle.setWinningTeam(winningTeam);
        newBattle.setLosingTeam(losingTeam);
        newBattle.setTeamOne(teamOne);
        newBattle.setTeamTwo(teamTwo);
        CharacterInstance startingCharacter = whosTurnsIsIt(newBattle);
        newBattle.setCurrentCharacterToPlay(startingCharacter);
        newBattle.setOngoing(true);
        newBattle = battleRepository.save(newBattle);
        return newBattle;
    }

    private CharacterInstance whosTurnsIsIt(Battle battle) {
        CharacterInstance characterToPlay = null;
        if (battle.getTurns() == null || battle.getTurns().isEmpty()) {
            return whoStartsTheBattle(battle.getTeamOne(), battle.getTeamTwo());
        }
        if (battle.getTurns().size() == 1) {
            CharacterInstance lastPerformer = battle.getTurns().getFirst().getPerformingCharacter();
            if (!battle.getTeamOne().contains(lastPerformer)) {
                characterToPlay = battle.getTeamOne().getFirst();
            } else if (!battle.getTeamTwo().contains(lastPerformer)) {
                characterToPlay = battle.getTeamTwo().getFirst();
            }
        }
        if (battle.getTurns().size() > 1) {
            Turn previousTurn = battle.getTurns().get(battle.getTurns().size() - 2);
            characterToPlay = previousTurn.getPerformingCharacter();
        }
        return characterToPlay;
    }

    private CharacterInstance whoStartsTheBattle(List<CharacterInstance> teamOne, List<CharacterInstance> teamTwo) {
        List<CharacterInstance> allChars = new ArrayList<>();
        allChars.addAll(teamOne);
        allChars.addAll(teamTwo);

        Random random = new Random();
        CharacterInstance fastest = allChars.getFirst();

        for (CharacterInstance character : allChars) {
            int speed = character.getStats().getSpeed();
            int currentSpeed = fastest.getStats().getSpeed();

            if (speed > currentSpeed) {
                fastest = character;
            } else if (speed == currentSpeed && random.nextInt(1, 3) == 1) {
                fastest = character;
            }
        }

        return fastest;
    }


    public boolean isBattleAvailableForToday(Long campaignId, String userId) {
        if (!characterService.doesHeroExistForACampaign(campaignId, userId)) {
            return false;
        }
        try {
            characterService.getDailyEnemy(campaignId, userId);
        } catch (DailyEnemyNotFound e) {
            return false;
        }
        if (getBattleForTodayByCampaignAndUserId(campaignId, userId) != null) {
            return false;
        }
        return true;
    }

    public Battle getBattleForTodayByCampaignAndUserId(Long campaignId, String userId) {
        LocalDate today = LocalDate.now();
        Battle todaysBattle = battleRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(campaignId, userId, today);
        if (todaysBattle == null) {
            throw new InvalidBattle("Battle for today not found");
        }
        CharacterInstance updatedCharacterToPlay = whosTurnsIsIt(todaysBattle);
        if (!todaysBattle.getCurrentCharacterToPlay().getId().equals(updatedCharacterToPlay.getId())) {
            todaysBattle.setCurrentCharacterToPlay(updatedCharacterToPlay);
            battleRepository.save(todaysBattle);
        }

        return todaysBattle;
    }
}
