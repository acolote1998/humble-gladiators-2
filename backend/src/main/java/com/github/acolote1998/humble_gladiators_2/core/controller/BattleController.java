package com.github.acolote1998.humble_gladiators_2.core.controller;

import com.github.acolote1998.humble_gladiators_2.core.dto.BattleResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.dto.BattleRewardsResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.dto.TurnRequestDto;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidBattle;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidTurn;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.model.BattleReward;
import com.github.acolote1998.humble_gladiators_2.core.model.Turn;
import com.github.acolote1998.humble_gladiators_2.core.service.BattleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/api/campaign")
public class BattleController {

    BattleService battleService;

    @Autowired
    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @GetMapping("/{campaignId}/battle/check-availability")
    public ResponseEntity<Boolean> checkIfPossibleToStartABattleToday(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(battleService.isBattleAvailableForToday(campaignId, userId));
    }

    @GetMapping("/{campaignId}/battle/check-ongoing")
    public ResponseEntity<Boolean> checkIfThereIsAnOngoingBattleForToday(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(battleService.getBattleUtil().isThereOngoingBattleForToday(campaignId, userId));
    }


    @GetMapping("/{campaignId}/battle/ongoing")
    public ResponseEntity<BattleResponseDto> getOnGoingBattleForToday(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        Battle todaysOngoingBattle = battleService.getUpdatedBattle(
                battleService.getBattleUtil().
                        getOnGoingBattleForTodayByCampaignAndUserId(campaignId, userId));
        BattleResponseDto dto = BattleResponseDto.fromModel(todaysOngoingBattle);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{campaignId}/battle/get-rewards")
    public ResponseEntity<BattleRewardsResponseDto> getRewardsForTodaysFinishedBattle(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        Battle todaysFinishedBattle = battleService.getUpdatedBattle(
                battleService.getBattleUtil().
                        getFinishedBattleForTodayByCampaignAndUserId(campaignId, userId));
        BattleReward rewards = battleService.getRewardForBattle(todaysFinishedBattle);
        battleService.fullyRecoverBothTeams(todaysFinishedBattle);
        BattleRewardsResponseDto dto = BattleRewardsResponseDto.fromModel(rewards);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{campaignId}/battle/recover-characters")
    public ResponseEntity<Void> recoverCharactersForTodaysFinishedBattle(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        Battle todaysFinishedBattle = battleService.getUpdatedBattle(
                battleService.getBattleUtil().
                        getFinishedBattleForTodayByCampaignAndUserId(campaignId, userId));
        battleService.fullyRecoverBothTeams(todaysFinishedBattle);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{campaignId}/battle/finished")
    public ResponseEntity<BattleResponseDto> getFinishedBattleForToday(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        Battle todaysFinishedBattle = battleService.getUpdatedBattle(
                battleService.getBattleUtil().
                        getFinishedBattleForTodayByCampaignAndUserId(campaignId, userId));
        BattleResponseDto dto = BattleResponseDto.fromModel(todaysFinishedBattle);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{campaignId}/battle/action/trigger-npc-turn")
    public ResponseEntity<Void> triggerNpcTurnInBattleOfToday(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        Battle battleToTrigger = battleService.getUpdatedBattle(
                battleService.getBattleUtil().
                        getOnGoingBattleForTodayByCampaignAndUserId(campaignId, userId));
        battleService.triggerNpcTurn(battleToTrigger);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{campaignId}/battle/new")
    public ResponseEntity<BattleResponseDto> createNewDailyBattle(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        Battle newBattle = battleService.createNewBattle(campaignId, userId);
        BattleResponseDto dto = BattleResponseDto.fromModel(newBattle);
        return ResponseEntity.created(URI.create("/api/campaign/" + campaignId + "/battle/" + newBattle.getId())).body(dto);
    }

    @PostMapping("/{campaignId}/battle/{battleId}/action/attack")
    public ResponseEntity<BattleResponseDto.TurnResponseDto> characterAttacks(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long campaignId,
            @PathVariable Long battleId,
            @RequestBody @Valid TurnRequestDto turnRequest) {
        String userId = jwt.getSubject();
        Turn newTurn = battleService.performPhysicalAttack(campaignId, userId, battleId, turnRequest);
        BattleResponseDto.TurnResponseDto dtoResponse = BattleResponseDto.TurnResponseDto.fromModel(newTurn);
        return ResponseEntity.ok(dtoResponse);
    }

    @PostMapping("/{campaignId}/battle/{battleId}/action/consumable")
    public ResponseEntity<BattleResponseDto.TurnResponseDto> characterUsesConsumable(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long campaignId,
            @PathVariable Long battleId,
            @RequestBody @Valid TurnRequestDto turnRequest) {
        String userId = jwt.getSubject();
        Turn newTurn = battleService.useConsumable(campaignId, userId, battleId, turnRequest);
        BattleResponseDto.TurnResponseDto dtoResponse = BattleResponseDto.TurnResponseDto.fromModel(newTurn);
        return ResponseEntity.ok(dtoResponse);
    }

    @PostMapping("/{campaignId}/battle/{battleId}/action/spell")
    public ResponseEntity<BattleResponseDto.TurnResponseDto> characterCastsSpell(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long campaignId,
            @PathVariable Long battleId,
            @RequestBody @Valid TurnRequestDto turnRequest) {
        String userId = jwt.getSubject();
        Turn newTurn = battleService.castSpell(campaignId, userId, battleId, turnRequest);
        BattleResponseDto.TurnResponseDto dtoResponse = BattleResponseDto.TurnResponseDto.fromModel(newTurn);
        return ResponseEntity.ok(dtoResponse);
    }


    @ExceptionHandler(InvalidBattle.class)
    public ResponseEntity<String> handleBattleAlreadyStarted(InvalidBattle ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTurn.class)
    public ResponseEntity<String> handleInvalidTurn(InvalidTurn ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(ex.getMessage());
    }
}
