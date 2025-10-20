package com.github.acolote1998.humble_gladiators_2.core.controller;

import com.github.acolote1998.humble_gladiators_2.core.dto.BattleResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.dto.TurnRequestDto;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidBattle;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidTurn;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
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
import org.springframework.validation.annotation.Validated;
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

    @GetMapping("/{campaignId}/battle")
    public ResponseEntity<BattleResponseDto> getBattleForToday(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        Battle todaysBattle = battleService.getBattleForTodayByCampaignAndUserId(campaignId, userId);
        BattleResponseDto dto = BattleResponseDto.fromModel(todaysBattle);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{campaignId}/battle/new")
    public ResponseEntity<BattleResponseDto> createNewDailyBattle(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        Battle newBattle = battleService.createNewBattle(campaignId, userId);
        BattleResponseDto dto = BattleResponseDto.fromModel(newBattle);
        return ResponseEntity.created(URI.create("/api/campaign/" + campaignId + "/battle/" + newBattle.getId())).body(dto);
    }

    @PostMapping("/{campaignId}/battle/{battleId}/action/attack")
    public ResponseEntity<BattleResponseDto.TurnResponseDto> heroAttacks(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long campaignId,
            @PathVariable Long battleId,
            @RequestBody @Valid TurnRequestDto turnRequest) {
        String userId = jwt.getSubject();
        Turn newTurn = battleService.performAttack(campaignId, userId, battleId, turnRequest);
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
