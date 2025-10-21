package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.characters.exception.DailyEnemyNotFound;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.dto.TurnRequestDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import com.github.acolote1998.humble_gladiators_2.core.enums.NPCActions;
import com.github.acolote1998.humble_gladiators_2.core.enums.StateType;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidBattle;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidTurn;
import com.github.acolote1998.humble_gladiators_2.core.model.Action;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.model.Turn;
import com.github.acolote1998.humble_gladiators_2.core.repository.BattleRepository;
import com.github.acolote1998.humble_gladiators_2.item.instances.ConsumableInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.SpellInstance;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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
        log.info("Battle '{}' created successfully for campaign '{}'", newBattle.getId(), campaignId);
        return newBattle;
    }

    public boolean isBattleNotNull(Battle battleToCheck) {
        return battleToCheck != null;
    }

    public boolean doesCharacterBelongToBattle(Battle battleToCheck, CharacterInstance charToCheck) {
        return (battleToCheck.getTeamOne().contains(charToCheck) ||
                battleToCheck.getTeamTwo().contains(charToCheck));
    }

    public boolean canProcessTurnValidly(
            TurnRequestDto turnRequest,
            CharacterInstance performingCharacter,
            CharacterInstance targetCharacter,
            Battle battleToCheck) {
        if (!characterService.isCharacterNotNull(performingCharacter)) {
            log.error("INVALID '{}' -  Performing character is invalid", turnRequest.action().name());
            return false;
        }
        if (!characterService.isCharacterNotNull(targetCharacter)) {
            log.error("INVALID '{}' -  Target character is invalid", turnRequest.action().name());
            return false;
        }
        if (!isBattleNotNull(battleToCheck)) {
            log.error("INVALID '{}' -  Battle is invalid", turnRequest.action().name());
            return false;
        }
        if (!performingCharacter.isAlive()) {
            log.error("INVALID '{}' - Performing character '{} - {}' is not alive",
                    turnRequest.action().name(),
                    performingCharacter.getId(),
                    performingCharacter.getName());
            return false;
        }
        if (!targetCharacter.isAlive()) {
            log.error("INVALID '{}' - Target character '{} - {}' is not alive",
                    turnRequest.action().name(),
                    targetCharacter.getId(),
                    targetCharacter.getName());
            return false;
        }
        if (!doesCharacterBelongToBattle(battleToCheck, performingCharacter)) {
            log.error("INVALID '{}' -  Character '{} - {}' does not belong to battle '{}'",
                    turnRequest.action().name(),
                    performingCharacter.getId(),
                    performingCharacter.getName(),
                    battleToCheck.getId());
            return false;
        }
        if (!doesCharacterBelongToBattle(battleToCheck, targetCharacter)) {
            log.error("INVALID '{}' -  Character '{} - {}' does not belong to battle '{}'",
                    turnRequest.action().name(),
                    targetCharacter.getId(),
                    targetCharacter.getName(),
                    battleToCheck.getId());
            return false;
        }
        if (!Objects.equals(whosTurnsIsIt(battleToCheck).getId(), performingCharacter.getId())) {
            log.error("INVALID '{}' -  It is not the character's '{} - {}' turn in battle '{}'",
                    turnRequest.action().name(),
                    performingCharacter.getId(),
                    performingCharacter.getName(),
                    battleToCheck.getId());
            return false;
        }
        if (turnRequest.action().name().equals(ActionType.CONSUMABLE.name())) {
            //If it is trying to use a consumable, we have to make sure it is a valid request
            if (turnRequest.cardToUseId() == null) {
                log.error("INVALID '{}' -  The intended consumable to use is null", turnRequest.action().name());
                return false;
            }
            if (!characterService.canTheCharacterUseConsumable(performingCharacter, turnRequest.cardToUseId())) {
                log.error("INVALID '{}' -  Character '{} - {}' cannot use the consumable '{}'",
                        turnRequest.action().name(),
                        performingCharacter.getId(),
                        performingCharacter.getName(),
                        turnRequest.cardToUseId());
                return false;
            }
        }
        if (turnRequest.action().name().equals(ActionType.SPELL.name())) {
            //If it is trying to use a spell, we have to make sure it is a valid request
            if (turnRequest.cardToUseId() == null) {
                log.error("INVALID '{}' -  The intended spell to use is null", turnRequest.action().name());
                return false;
            }
            if (!characterService.canTheCharacterUseSpell(performingCharacter, turnRequest.cardToUseId())) {
                log.error("INVALID '{}' -  Character '{} - {}' cannot use the spell '{}'",
                        turnRequest.action().name(),
                        performingCharacter.getId(),
                        performingCharacter.getName(),
                        turnRequest.cardToUseId());
                return false;
            }
        }
        return true;
    }

    @Transactional
    public Turn performPhysicalAttack(Long campaignId, String userId, Long battleId, TurnRequestDto turnRequest) {
        CharacterInstance performerCharacter = characterService.getCharacterByIdAndCampaignIdAndUserId(
                turnRequest.performingCharacterId(),
                campaignId,
                userId);
        CharacterInstance targetCharacter = characterService.getCharacterByIdAndCampaignIdAndUserId(
                turnRequest.targetCharacterId(),
                campaignId,
                userId);
        Battle battle = getBattleByIdAndCampaignIdAndUserId(battleId, campaignId, userId);
        if (!canProcessTurnValidly(
                turnRequest,
                performerCharacter,
                targetCharacter,
                battle)) {
            throw new InvalidTurn("Cannot process turn. Invalid");
        }
        Action action = performerCharacter.usePhysicalAttack(targetCharacter);
        characterService.saveCharacter(performerCharacter);
        characterService.saveCharacter(targetCharacter);
        Turn newTurn = new Turn();
        newTurn.setBattle(battle);
        newTurn.setCampaign(battle.getCampaign());
        newTurn.setPerformingCharacter(performerCharacter);
        newTurn.setTargetCharacter(targetCharacter);
        newTurn.setAction(action);
        battle.getTurns().add(newTurn);
        battleRepository.save(battle);
        return newTurn;
    }

    @Transactional
    public Turn useConsumable(Long campaignId, String userId, Long battleId, TurnRequestDto turnRequest) {
        CharacterInstance performerCharacter = characterService.getCharacterByIdAndCampaignIdAndUserId(
                turnRequest.performingCharacterId(),
                campaignId,
                userId);
        CharacterInstance targetCharacter = characterService.getCharacterByIdAndCampaignIdAndUserId(
                turnRequest.targetCharacterId(),
                campaignId,
                userId);
        Battle battle = getBattleByIdAndCampaignIdAndUserId(battleId, campaignId, userId);
        if (!canProcessTurnValidly(
                turnRequest,
                performerCharacter,
                targetCharacter,
                battle)) {
            throw new InvalidTurn("Cannot process turn. Invalid");
        }
        Action action = performerCharacter.useConsumable(turnRequest.cardToUseId(), targetCharacter);
        characterService.saveCharacter(performerCharacter);
        characterService.saveCharacter(targetCharacter);
        Turn newTurn = new Turn();
        newTurn.setBattle(battle);
        newTurn.setCampaign(battle.getCampaign());
        newTurn.setPerformingCharacter(performerCharacter);
        newTurn.setTargetCharacter(targetCharacter);
        newTurn.setAction(action);
        battle.getTurns().add(newTurn);
        battleRepository.save(battle);
        return newTurn;
    }

    @Transactional
    public Turn castSpell(Long campaignId, String userId, Long battleId, TurnRequestDto turnRequest) {
        CharacterInstance performerCharacter = characterService.getCharacterByIdAndCampaignIdAndUserId(
                turnRequest.performingCharacterId(),
                campaignId,
                userId);
        CharacterInstance targetCharacter = characterService.getCharacterByIdAndCampaignIdAndUserId(
                turnRequest.targetCharacterId(),
                campaignId,
                userId);
        Battle battle = getBattleByIdAndCampaignIdAndUserId(battleId, campaignId, userId);
        if (!canProcessTurnValidly(
                turnRequest,
                performerCharacter,
                targetCharacter,
                battle)) {
            throw new InvalidTurn("Cannot process turn. Invalid");
        }
        Action action = performerCharacter.castSpell(turnRequest.cardToUseId(), targetCharacter);
        characterService.saveCharacter(performerCharacter);
        characterService.saveCharacter(targetCharacter);
        Turn newTurn = new Turn();
        newTurn.setBattle(battle);
        newTurn.setCampaign(battle.getCampaign());
        newTurn.setPerformingCharacter(performerCharacter);
        newTurn.setTargetCharacter(targetCharacter);
        newTurn.setAction(action);
        battle.getTurns().add(newTurn);
        battleRepository.save(battle);
        return newTurn;
    }


    public Battle getBattleByIdAndCampaignIdAndUserId(Long battleId, Long campaignId, String userId) {
        return battleRepository.findByIdAndCampaign_IdAndUserId(battleId, campaignId, userId);
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
        try {
            getBattleForTodayByCampaignAndUserId(campaignId, userId);
            return false;
        } catch (InvalidBattle ex) {

        }
        return true;
    }

    public Battle getBattleForTodayByCampaignAndUserId(Long campaignId, String userId) {
        LocalDate today = LocalDate.now();
        Battle todaysBattle = battleRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(campaignId, userId, today);
        if (todaysBattle == null) {
            log.error("Campaign '{}' - Battle for today not found", campaignId);
            throw new InvalidBattle("Battle for today not found");
        }
        CharacterInstance updatedCharacterToPlay = whosTurnsIsIt(todaysBattle);
        if (!todaysBattle.getCurrentCharacterToPlay().getId().equals(updatedCharacterToPlay.getId())) {
            todaysBattle.setCurrentCharacterToPlay(updatedCharacterToPlay);
            battleRepository.save(todaysBattle);
        }

        return todaysBattle;
    }

    Turn playNPCTurn(Battle battleToPlayAt, CharacterInstance characterToPlay, CharacterInstance characterToTarget) {
        boolean couldRecoverHp = characterToPlay.getStats().getCurrentHp() < characterToPlay.getStats().getMaxHp();
        boolean couldRecoverMp = characterToPlay.getStats().getCurrentMp() < characterToPlay.getStats().getMaxMp();
        boolean couldAttackPhysically = characterToPlay.getPhysicalDamage() > 0;
        boolean couldCastDamageSpell = false;
        if (!characterToPlay.getInventory().getSpells().isEmpty()) {
            for (SpellInstance spell : characterToPlay.getInventory().getSpells()) {
                if ((spell.getTemplate().getMagicalDamage() > 0
                        || spell.getTemplate().getPhysicalDamage() > 0)
                        && spell.getTemplate().getMpCost() <= characterToPlay.getStats().getCurrentMp()) {
                    couldCastDamageSpell = true;
                    break;
                }
            }
        }
        boolean couldCastHealSpell = false;
        if (!characterToPlay.getInventory().getSpells().isEmpty()) {
            for (SpellInstance spell : characterToPlay.getInventory().getSpells()) {
                if ((spell.getTemplate().getRestoreHp() > 0)
                        && spell.getTemplate().getMpCost() <= characterToPlay.getStats().getCurrentMp()) {
                    couldCastHealSpell = true;
                    break;
                }
            }
        }
        boolean couldRestoreHpWithConsumable = false;
        if (!characterToPlay.getInventory().getConsumables().isEmpty()) {
            for (ConsumableInstance consumable : characterToPlay.getInventory().getConsumables()) {
                if (consumable.getTemplate().getRestoreHp() > 0) {
                    couldRestoreHpWithConsumable = true;
                    break;
                }
            }
        }
        boolean couldRestoreMpWithConsumable = false;
        if (!characterToPlay.getInventory().getConsumables().isEmpty()) {
            for (ConsumableInstance consumable : characterToPlay.getInventory().getConsumables()) {
                if (consumable.getTemplate().getRestoreMp() > 0) {
                    couldRestoreMpWithConsumable = true;
                    break;
                }
            }
        }
        List<NPCActions> possibleActions = new ArrayList<>();
        if (couldRecoverHp && couldCastHealSpell) {
            possibleActions.add(NPCActions.SPELL_HEAL);
        }
        if (couldRecoverHp && couldRestoreHpWithConsumable) {
            possibleActions.add(NPCActions.CONSUMABLE_RECOVER_HP);
        }
        if (couldRecoverMp && couldRestoreMpWithConsumable) {
            possibleActions.add(NPCActions.CONSUMABLE_RECOVER_MP);
        }
        if (couldAttackPhysically) {
            possibleActions.add(NPCActions.PHYSICAL_ATTACK);
        }
        if (couldCastDamageSpell) {
            possibleActions.add(NPCActions.SPELL_ATTACK);
        }
        if (possibleActions.isEmpty()) {
            possibleActions.add(NPCActions.PHYSICAL_ATTACK);
        }
        Collections.shuffle(possibleActions);
        NPCActions decisionToPerform = possibleActions.getFirst();
        Turn npcsTurn = new Turn();
        npcsTurn.setBattle(battleToPlayAt);
        npcsTurn.setCampaign(characterToPlay.getCampaign());
        npcsTurn.setPerformingCharacter(characterToPlay);
        switch (decisionToPerform) {
            case SPELL_HEAL -> {
                List<SpellInstance> possibleSpells = new ArrayList<>();
                for (SpellInstance spell : characterToPlay.getInventory().getSpells()) {
                    if ((spell.getTemplate().getMpCost() <= characterToPlay.getStats().getCurrentMp())
                            && spell.getTemplate().getRestoreHp() > 0) {
                        possibleSpells.add(spell);
                    }
                }
                Collections.shuffle(possibleSpells);
                SpellInstance spellToCast = possibleSpells.getFirst();
                Action spellAction = characterToPlay.castSpell(spellToCast.getId(), characterToPlay);
                npcsTurn.setTargetCharacter(characterToPlay);
                npcsTurn.setAction(spellAction);
                log.info("'ID {} {} used 'ID {} {}' to heal themselves'",
                        characterToPlay.getId(),
                        characterToPlay.getName(),
                        spellToCast.getId(),
                        spellToCast.getName()
                );
            }
            case SPELL_ATTACK -> {
                List<SpellInstance> possibleSpells = new ArrayList<>();
                for (SpellInstance spell : characterToPlay.getInventory().getSpells()) {
                    if ((spell.getTemplate().getMagicalDamage() > 0
                            || spell.getTemplate().getPhysicalDamage() > 0)
                            && spell.getTemplate().getMpCost() <= characterToPlay.getStats().getCurrentMp()) {
                        possibleSpells.add(spell);
                    }
                }
                Collections.shuffle(possibleSpells);
                SpellInstance spellToCast = possibleSpells.getFirst();
                Action spellAction = characterToPlay.castSpell(spellToCast.getId(), characterToTarget);
                npcsTurn.setTargetCharacter(characterToTarget);
                npcsTurn.setAction(spellAction);
                log.info("'ID {} {} used 'ID {} {}' to attack'",
                        characterToPlay.getId(),
                        characterToPlay.getName(),
                        spellToCast.getId(),
                        spellToCast.getName()
                );
            }
            case PHYSICAL_ATTACK -> {
                Action physicalAttackAction = characterToPlay.usePhysicalAttack(characterToTarget);
                npcsTurn.setTargetCharacter(characterToTarget);
                npcsTurn.setAction(physicalAttackAction);
                log.info("'ID {} {} performed a physical attack'",
                        characterToPlay.getId(),
                        characterToPlay.getName()
                );
            }
            case CONSUMABLE_RECOVER_HP -> {
                List<ConsumableInstance> possibleConsumables = new ArrayList<>();
                for (ConsumableInstance consumable : characterToPlay.getInventory().getConsumables()) {
                    if (consumable.getTemplate().getRestoreHp() > 0) {
                        possibleConsumables.add(consumable);
                    }
                }
                Collections.shuffle(possibleConsumables);
                ConsumableInstance consumableToUse = possibleConsumables.getFirst();
                Action consumableAction = characterToPlay.useConsumable(consumableToUse.getId(), characterToPlay);
                npcsTurn.setTargetCharacter(characterToPlay);
                npcsTurn.setAction(consumableAction);
                log.info("'ID {} {} used consumable 'ID {} {}' to heal themselves'",
                        characterToPlay.getId(),
                        characterToPlay.getName(),
                        consumableToUse.getId(),
                        consumableToUse.getName()
                );
            }
            case CONSUMABLE_RECOVER_MP -> {
                List<ConsumableInstance> possibleConsumables = new ArrayList<>();
                for (ConsumableInstance consumable : characterToPlay.getInventory().getConsumables()) {
                    if (consumable.getTemplate().getRestoreMp() > 0) {
                        possibleConsumables.add(consumable);
                    }
                }
                Collections.shuffle(possibleConsumables);
                ConsumableInstance consumableToUse = possibleConsumables.getFirst();
                Action consumableAction = characterToPlay.useConsumable(consumableToUse.getId(), characterToPlay);
                npcsTurn.setTargetCharacter(characterToPlay);
                npcsTurn.setAction(consumableAction);
                log.info("'ID {} {} used consumable 'ID {} {}' to recover mp'",
                        characterToPlay.getId(),
                        characterToPlay.getName(),
                        consumableToUse.getId(),
                        consumableToUse.getName()
                );
            }
            case NOTHING -> {
                npcsTurn.setTargetCharacter(characterToTarget);
                Action action = new Action();
                action.setDamageCaused(0);
                action.setHealingCaused(0);
                action.setMpRecoverCaused(0);
                action.setActionType(ActionType.NOTHING);
                action.setStateCaused(StateType.NONE);
                npcsTurn.setAction(action);
                log.warn("'ID {} {} did not do anything???'",
                        characterToPlay.getId(),
                        characterToPlay.getName()
                );
            }
        }
        return npcsTurn;
    }

    private boolean isBattleActive(Battle battleToCheck) {
        //Do checks here such as

        // battle is still on the same day it was created , if not -> add hero as loser, mark as onGoing false

        // winner list or loser list are still empty -> mark onGoing as false

        // both team one and team two are still alive

        // the "onGoing" is still true????
        return true;
    }

    @Transactional
    public Battle getUpdatedBattleForToday(Battle oldBattle) {
        Battle updatedBattle = oldBattle;
        if (!isBattleActive(updatedBattle)) {
            updatedBattle.setOngoing(false);
            return battleRepository.save(updatedBattle);
        }
        CharacterInstance whosTurnIsIt = whosTurnsIsIt(updatedBattle);
        if (updatedBattle.getTeamTwo().contains(whosTurnIsIt) && (whosTurnIsIt.getCharacterType() == CharacterType.NPC)) {
            // If it is the NPC's turn
            CharacterInstance enemyAsAttacker = whosTurnIsIt;
            CharacterInstance heroAsTarget = oldBattle.getTeamOne().stream().filter(characterInstance -> characterInstance.getCharacterType() == CharacterType.PLAYER).findFirst().orElse(null);
            if (heroAsTarget != null && enemyAsAttacker != null) {
                updatedBattle.getTurns().add(playNPCTurn(updatedBattle, enemyAsAttacker, heroAsTarget));
            }
        }
        updatedBattle.setCurrentCharacterToPlay(whosTurnsIsIt(updatedBattle));
        return battleRepository.save(updatedBattle);
    }
}
