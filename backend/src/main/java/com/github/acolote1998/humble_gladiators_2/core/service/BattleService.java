package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.characters.exception.DailyEnemyNotFound;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterSnapshot;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.characters.service.DamageCalculationService;
import com.github.acolote1998.humble_gladiators_2.core.dto.TurnRequestDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import com.github.acolote1998.humble_gladiators_2.core.enums.BattleResultEnum;
import com.github.acolote1998.humble_gladiators_2.core.enums.NPCActions;
import com.github.acolote1998.humble_gladiators_2.core.enums.StateType;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidBattle;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidTurn;
import com.github.acolote1998.humble_gladiators_2.core.model.*;
import com.github.acolote1998.humble_gladiators_2.core.repository.BattleRepository;
import com.github.acolote1998.humble_gladiators_2.item.instances.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class BattleService {
    private CharacterService characterService;

    private BattleRepository battleRepository;

    @Getter
    private BattleUtil battleUtil;
    
    private DamageCalculationService damageCalculationService;

    @Value("${UNLIMITED_BATTLES_ALLOWED}")
    private boolean UNLIMITED_BATTLES_ALLOWED;

    @Value("${NPC_ITEM_DROP_CHANCE}")
    private Integer NPC_ITEM_DROP_CHANCE;

    @Autowired
    public BattleService(
            CharacterService characterService,
            BattleRepository battleRepository,
            BattleUtil battleUtil,
            DamageCalculationService damageCalculationService) {
        this.characterService = characterService;
        this.battleRepository = battleRepository;
        this.battleUtil = battleUtil;
        this.damageCalculationService = damageCalculationService;
    }

    public Boolean doesCharacterDropThisItem() {
        Random randomChance = new Random();
        return randomChance.nextInt(1, 101) <= NPC_ITEM_DROP_CHANCE;
    }

    public Battle createNewBattle(Long campaignId, String userId) {
        if (!isBattleAvailableForToday(campaignId, userId)) {
            throw new InvalidBattle("It is not possible to start a new battle");
        }
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        CharacterInstance enemy = characterService.getDailyEnemy(campaignId, userId);
        hero.setDamageCalculationService(damageCalculationService);
        enemy.setDamageCalculationService(damageCalculationService);
        List<Turn> emptyTurnList = new ArrayList<>();
        List<CharacterInstance> teamOne = new ArrayList<>();
        teamOne.add(hero);
        List<CharacterInstance> teamTwo = new ArrayList<>();
        teamTwo.add(enemy);
        List<CharacterSnapshot> startingTeamOne = new ArrayList<>();
        startingTeamOne.add(CharacterSnapshot.fromCharacterInstance(hero));
        List<CharacterSnapshot> startingTeamTwo = new ArrayList<>();
        startingTeamTwo.add(CharacterSnapshot.fromCharacterInstance(enemy));
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
        newBattle.setStartingTeamOne(startingTeamOne);
        newBattle.setStartingTeamTwo(startingTeamTwo);
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
        // Compare by ID instead of object reference, since entities loaded from DB are different instances
        Long charId = charToCheck.getId();
        boolean inTeamOne = battleToCheck.getTeamOne().stream()
                .anyMatch(character -> character.getId().equals(charId));
        boolean inTeamTwo = battleToCheck.getTeamTwo().stream()
                .anyMatch(character -> character.getId().equals(charId));
        return inTeamOne || inTeamTwo;
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
        if (!battleToCheck.isOngoing()) {
            log.error("INVALID '{}' -  Battle is not ongoing", turnRequest.action().name());
            return false;
        }
        if (!battleToCheck.getWinningTeam().isEmpty() || !battleToCheck.getLosingTeam().isEmpty()) {
            log.error("INVALID '{}' -  Battle already has winners or losers", turnRequest.action().name());
            return false;
        }
        if ((battleToCheck.getTeamOne() == null || battleToCheck.getTeamOne().isEmpty()) ||
                (battleToCheck.getTeamTwo() == null || battleToCheck.getTeamTwo().isEmpty())) {
            log.error("INVALID '{}' -  Battle is missing members for either team one or two", turnRequest.action().name());
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
        performerCharacter.setDamageCalculationService(damageCalculationService);
        targetCharacter.setDamageCalculationService(damageCalculationService);
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
        battleRepository.save(getUpdatedBattle(battle));
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
        performerCharacter.setDamageCalculationService(damageCalculationService);
        targetCharacter.setDamageCalculationService(damageCalculationService);
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
        battleRepository.save(getUpdatedBattle(battle));
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
        performerCharacter.setDamageCalculationService(damageCalculationService);
        targetCharacter.setDamageCalculationService(damageCalculationService);
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
        battleRepository.save(getUpdatedBattle(battle));
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
            Long lastPerformerId = lastPerformer.getId();
            // Compare by ID instead of object reference to avoid persistence context issues
            boolean inTeamOne = battle.getTeamOne().stream()
                    .anyMatch(character -> character.getId().equals(lastPerformerId));
            if (!inTeamOne) {
                characterToPlay = battle.getTeamOne().getFirst();
            } else {
                // If in team one, next turn should be team two
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

        CharacterInstance fastest = allChars.getFirst();

        for (CharacterInstance character : allChars) {
            int speed = character.getStats().getSpeed();
            int currentSpeed = fastest.getStats().getSpeed();

            if (speed >= currentSpeed) {
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
        if (battleUtil.isThereOngoingBattleForToday(campaignId, userId)) {
            return false;
        }
        if (!UNLIMITED_BATTLES_ALLOWED) {
            try {
                getAnyBattleForTodayByCampaignAndUserId(campaignId, userId);
                return false;
            } catch (InvalidBattle ex) {

            }
        }
        return true;
    }


    public Battle getAnyBattleForTodayByCampaignAndUserId(Long campaignId, String userId) {
        LocalDate today = LocalDate.now();
        Battle todaysBattle = battleRepository.findAnyByCampaignIdAndUserIdAndUpdatedAtDate(campaignId, userId, today);
        if (todaysBattle == null) {
            log.error("Campaign '{}' - Battle for today not found", campaignId);
            throw new InvalidBattle("Battle for today not found");
        }
        return todaysBattle;
    }

    Turn playNPCTurn(Battle battleToPlayAt, CharacterInstance characterToPlay, CharacterInstance characterToTarget) {
        characterToPlay.setDamageCalculationService(damageCalculationService);
        characterToTarget.setDamageCalculationService(damageCalculationService);
        boolean couldRecoverHp = characterToPlay.getStats().getCurrentHp() < characterToPlay.getStats().getMaxHp();
        boolean couldRecoverMp = characterToPlay.getStats().getCurrentMp() < characterToPlay.getStats().getMaxMp();
        // Technically they should always be able to physically attack, because even if physical dmg is 0, the strength
        // will never be zero, and that can cause damage anyways
//        boolean couldAttackPhysically = characterToPlay.getPhysicalDamage() > 0;
        boolean couldAttackPhysically = true;
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
        if (battleToCheck.getCreatedAt().getDayOfYear() != LocalDate.now().getDayOfYear()) {
            log.warn("The battle '{}' is not a battle of today", battleToCheck.getId());
            return false;
        }
        if (!battleToCheck.getWinningTeam().isEmpty() || !battleToCheck.getLosingTeam().isEmpty()) {
            log.warn("The battle '{}' already has winners/losers, it should not be active", battleToCheck.getId());
            return false;
        }
        if (!battleToCheck.isOngoing()) {
            log.warn("The battle '{}' is not ongoing!", battleToCheck.getId());
            return false;
        }
        return true;
    }

    @Transactional
    public Battle getUpdatedBattle(Battle oldBattle) {
        Battle updatedBattle = oldBattle;
        if (!isBattleActive(updatedBattle)) {
            //If not a battle from today, and there are still no winners / losers, we default the victory for the enemy
            if ((updatedBattle.getCreatedAt().getDayOfYear() != LocalDate.now().getDayOfYear())
                    && (updatedBattle.getLosingTeam().isEmpty())) {
                updatedBattle.getLosingTeam().add(updatedBattle.getTeamOne().getFirst());
            }
            if ((updatedBattle.getCreatedAt().getDayOfYear() != LocalDate.now().getDayOfYear())
                    && updatedBattle.getWinningTeam().isEmpty()) {
                updatedBattle.getWinningTeam().add(updatedBattle.getTeamTwo().getFirst());
            }
            if ((updatedBattle.getCreatedAt().getDayOfYear() != LocalDate.now().getDayOfYear())
                    && updatedBattle.getCurrentCharacterToPlay() != null) {
                updatedBattle.setCurrentCharacterToPlay(null);
            }
            updatedBattle.setOngoing(false);
        } else {
            CharacterInstance currentCharToPlay = whosTurnsIsIt(updatedBattle);
            updatedBattle.setCurrentCharacterToPlay(currentCharToPlay);
            updatedBattle = findWinnersOrContinueBattle(updatedBattle);
        }
        return battleRepository.save(updatedBattle);
    }

    @Transactional
    public Battle triggerNpcTurn(Battle battleToTrigger) {
        if (isBattleActive(battleToTrigger)) {
            CharacterInstance whosTurnIsIt = whosTurnsIsIt(battleToTrigger);
            if (battleToTrigger.getTeamTwo().contains(whosTurnIsIt) && (whosTurnIsIt.getCharacterType() == CharacterType.NPC)) {
                // If it is the NPC's turn
                CharacterInstance enemyAsAttacker = whosTurnIsIt;
                CharacterInstance heroAsTarget = battleToTrigger.getTeamOne().stream().filter(characterInstance -> characterInstance.getCharacterType() == CharacterType.PLAYER).findFirst().orElse(null);
                if (heroAsTarget != null && enemyAsAttacker != null) {
                    battleToTrigger.getTurns().add(playNPCTurn(battleToTrigger, enemyAsAttacker, heroAsTarget));
                }
            }
        }
        return battleRepository.save(getUpdatedBattle(battleToTrigger));
    }

    @Transactional
    public void fullyRecoverBothTeams(Battle battleToCheck) {
        battleToCheck.getWinningTeam().getFirst().recoverMp(50000);
        battleToCheck.getWinningTeam().getFirst().heal(50000);
        battleToCheck.getLosingTeam().getFirst().recoverMp(50000);
        battleToCheck.getLosingTeam().getFirst().heal(50000);
        characterService.saveCharacter(battleToCheck.getWinningTeam().getFirst());
        characterService.saveCharacter(battleToCheck.getLosingTeam().getFirst());
    }

    public Battle findWinnersOrContinueBattle(Battle battleToCheck) {
        if (battleToCheck.getWinningTeam().isEmpty()
                && battleToCheck.getLosingTeam().isEmpty()
                && (battleToCheck.getTeamOne().getFirst().getStats().getCurrentHp() <= 0 ||
                battleToCheck.getTeamTwo().getFirst().getStats().getCurrentHp() <= 0)) {
            battleToCheck.setCurrentCharacterToPlay(null);
            battleToCheck.setOngoing(false);
            if (battleToCheck.getTeamOne().getFirst().isAlive() &&
                    !battleToCheck.getTeamTwo().getFirst().isAlive()) {
                battleToCheck.getWinningTeam().add(battleToCheck.getTeamOne().getFirst());
                battleToCheck.getLosingTeam().add(battleToCheck.getTeamTwo().getFirst());
                log.info("'{} {}' won the battle", battleToCheck.getTeamOne().getFirst().getId(), battleToCheck.getTeamOne().getFirst().getName());
            }
            if (!battleToCheck.getTeamOne().getFirst().isAlive() &&
                    battleToCheck.getTeamTwo().getFirst().isAlive()) {
                battleToCheck.getWinningTeam().add(battleToCheck.getTeamTwo().getFirst());
                battleToCheck.getLosingTeam().add(battleToCheck.getTeamOne().getFirst());
                log.info("'{} {}' won the battle", battleToCheck.getTeamTwo().getFirst().getId(), battleToCheck.getTeamTwo().getFirst().getName());
            }
            battleToCheck.getTeamOne().clear();
            battleToCheck.getTeamTwo().clear();
        } else {
            battleToCheck.setCurrentCharacterToPlay(whosTurnsIsIt(battleToCheck));
        }
        return battleToCheck;
    }

    public List<Battle> getAllWonBattlesForCampaignHero(Long campaignId, String userId) {
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        List<Battle> allWonBattles = battleRepository.findAllByWinningTeamContains(List.of(hero));
        return allWonBattles;
    }

    public List<Battle> getAllLostBattlesForACharacter(Long campaignId, String userId) {
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        List<Battle> allWonBattles = battleRepository.findAllByLosingTeamContains(List.of(hero));
        return allWonBattles;
    }

    public BattleReward createRewardForBattle(Battle battleToCheck) {
        if (isBattleActive(battleToCheck)) {
            log.warn("Cannot assign rewards to battle {}, still active!", battleToCheck.getId());
            throw new InvalidBattle("Cannot assign rewards - Battle is still active!");
        }
        log.info("Reward for battle '{}' not found - creating", battleToCheck.getId());
        CharacterInstance winner = battleToCheck.getWinningTeam().getFirst();
        CharacterInstance loser = battleToCheck.getLosingTeam().getFirst();
        int goldReward = loser.getGoldReward();
        int expReward = loser.getExpReward();
        List<ArmorInstance> possibleArmorDrop = new ArrayList<>();
        List<BootsInstance> possibleBootsDrop = new ArrayList<>();
        List<ConsumableInstance> possibleConsumablesDrop = new ArrayList<>();
        List<HelmetInstance> possibleHelmetDrop = new ArrayList<>();
        List<ShieldInstance> possibleShieldDrop = new ArrayList<>();
        List<SpellInstance> possibleSpellDrop = new ArrayList<>();
        List<WeaponInstance> possibleWeaponDrop = new ArrayList<>();
        BattleResultEnum teamWhoWon = BattleResultEnum.NONE;
        if (battleToCheck.getStartingTeamOne().getFirst().getName().equals(battleToCheck.getWinningTeam().getFirst().getName())) {
            teamWhoWon = BattleResultEnum.VICTORY_TEAM_ONE;
        } else if (battleToCheck.getStartingTeamTwo().getFirst().getName().equals(battleToCheck.getWinningTeam().getFirst().getName())) {
            teamWhoWon = BattleResultEnum.VICTORY_TEAM_TWO;
        }
        if (winner.getCharacterType().equals(CharacterType.PLAYER)) {
            if (doesCharacterDropThisItem()) {
                if (!loser.getInventory().getArmors().isEmpty()) {
                    Collections.shuffle(loser.getInventory().getArmors());
                    ArmorInstance dropped = loser.getInventory().getArmors().getFirst();
                    dropped.setEquipped(false);
                    dropped.setDiscovered(true);
                    dropped.getTemplate().setDiscovered(true);
                    possibleArmorDrop.add(dropped);
                    loser.getInventory().getArmors().remove(dropped);
                }
            }
            if (doesCharacterDropThisItem()) {
                if (!loser.getInventory().getBoots().isEmpty()) {
                    Collections.shuffle(loser.getInventory().getBoots());
                    BootsInstance dropped = loser.getInventory().getBoots().getFirst();
                    dropped.setEquipped(false);
                    dropped.setDiscovered(true);
                    dropped.getTemplate().setDiscovered(true);
                    possibleBootsDrop.add(dropped);
                    loser.getInventory().getBoots().remove(dropped);
                }
            }
            if (doesCharacterDropThisItem()) {
                if (!loser.getInventory().getConsumables().isEmpty()) {
                    Collections.shuffle(loser.getInventory().getConsumables());
                    ConsumableInstance dropped = loser.getInventory().getConsumables().getFirst();
                    dropped.setEquipped(false);
                    dropped.setDiscovered(true);
                    dropped.getTemplate().setDiscovered(true);
                    possibleConsumablesDrop.add(dropped);
                    loser.getInventory().getConsumables().remove(dropped);
                }
            }
            if (doesCharacterDropThisItem()) {
                if (!loser.getInventory().getHelmets().isEmpty()) {
                    Collections.shuffle(loser.getInventory().getHelmets());
                    HelmetInstance dropped = loser.getInventory().getHelmets().getFirst();
                    dropped.setEquipped(false);
                    dropped.setDiscovered(true);
                    dropped.getTemplate().setDiscovered(true);
                    possibleHelmetDrop.add(dropped);
                    loser.getInventory().getHelmets().remove(dropped);
                }
            }
            if (doesCharacterDropThisItem()) {
                if (!loser.getInventory().getShields().isEmpty()) {
                    Collections.shuffle(loser.getInventory().getShields());
                    ShieldInstance dropped = loser.getInventory().getShields().getFirst();
                    dropped.setEquipped(false);
                    dropped.setDiscovered(true);
                    dropped.getTemplate().setDiscovered(true);
                    possibleShieldDrop.add(dropped);
                    loser.getInventory().getShields().remove(dropped);
                }
            }
            if (doesCharacterDropThisItem()) {
                if (!loser.getInventory().getSpells().isEmpty()) {
                    Collections.shuffle(loser.getInventory().getSpells());
                    SpellInstance dropped = loser.getInventory().getSpells().getFirst();
                    dropped.setEquipped(false);
                    dropped.setDiscovered(true);
                    dropped.getTemplate().setDiscovered(true);
                    possibleSpellDrop.add(dropped);
                    loser.getInventory().getSpells().remove(dropped);
                }
            }
            if (doesCharacterDropThisItem()) {
                if (!loser.getInventory().getWeapons().isEmpty()) {
                    Collections.shuffle(loser.getInventory().getWeapons());
                    WeaponInstance dropped = loser.getInventory().getWeapons().getFirst();
                    dropped.setEquipped(false);
                    dropped.setDiscovered(true);
                    dropped.getTemplate().setDiscovered(true);
                    possibleWeaponDrop.add(dropped);
                    loser.getInventory().getWeapons().remove(dropped);
                }
            }
            winner.getStats().setCurrentExp(winner.getStats().getCurrentExp() + expReward);
            Inventory winnerInventory = winner.getInventory();
            winnerInventory.setGold(winnerInventory.getGold() + goldReward);
            winnerInventory.getArmors().addAll(possibleArmorDrop);
            winnerInventory.getBoots().addAll(possibleBootsDrop);
            winnerInventory.getConsumables().addAll(possibleConsumablesDrop);
            winnerInventory.getHelmets().addAll(possibleHelmetDrop);
            winnerInventory.getShields().addAll(possibleShieldDrop);
            winnerInventory.getSpells().addAll(possibleSpellDrop);
            winnerInventory.getWeapons().addAll(possibleWeaponDrop);
        }
        BattleReward reward = new BattleReward();
        reward.setGoldReward(goldReward);
        reward.setExpReward(expReward);
        reward.setBattleResult(teamWhoWon);
        reward.setArmorLoot(possibleArmorDrop);
        reward.setBootsLoot(possibleBootsDrop);
        reward.setConsumablesLoot(possibleConsumablesDrop);
        reward.setHelmetsLoot(possibleHelmetDrop);
        reward.setShieldsLoot(possibleShieldDrop);
        reward.setSpellsLoot(possibleSpellDrop);
        reward.setWeaponsLoot(possibleWeaponDrop);
        characterService.saveCharacter(winner);
        characterService.saveCharacter(loser);
        return reward;
    }

    @Transactional
    public BattleReward getRewardForBattle(Battle battleToProcess) {
        if (isBattleActive(battleToProcess)) {
            log.warn("Battle '{}' is still active, cannot retrieve its reward!", battleToProcess.getId());
            throw new InvalidBattle("Battle '" + battleToProcess.getId() + "' is active, cannot retrieve its reward");
        }
        BattleReward reward;
        if (battleToProcess.getReward() == null) {
            reward = createRewardForBattle(battleToProcess);
            battleToProcess.setReward(reward);
            battleRepository.save(battleToProcess);
        } else {
            reward = battleToProcess.getReward();
        }
        return reward;
    }
}