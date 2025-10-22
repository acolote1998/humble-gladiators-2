package com.github.acolote1998.humble_gladiators_2.booster.controller;

import com.github.acolote1998.humble_gladiators_2.booster.dto.CharacterBoosterResponseDto;
import com.github.acolote1998.humble_gladiators_2.booster.dto.ItemBoosterResponseDto;
import com.github.acolote1998.humble_gladiators_2.booster.exception.DailyBoosterAlreadyOpened;
import com.github.acolote1998.humble_gladiators_2.booster.exception.InvalidBooster;
import com.github.acolote1998.humble_gladiators_2.booster.model.CharacterBooster;
import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import com.github.acolote1998.humble_gladiators_2.booster.service.BoosterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@CrossOrigin
@RequestMapping("/api/campaign")
public class BoosterController {

    BoosterService boosterService;

    public BoosterController(BoosterService boosterService) {
        this.boosterService = boosterService;
    }

    @PostMapping("/{campaignId}/items-booster")
    ResponseEntity<ItemBoosterResponseDto> openNewItemBooster(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        ItemsBooster modelBooster = boosterService.getNewItemsBooster(campaignId, userId);
        ItemBoosterResponseDto responseDto = ItemBoosterResponseDto.fromModelToDto(modelBooster);
        boosterService.discoverContentOfItemBooster(modelBooster);
        return ResponseEntity
                .created(URI.create("/api/campaign/" + campaignId + "/items-booster/" + modelBooster.getId()))
                .body(responseDto);
    }

    @PostMapping("/{campaignId}/character-booster")
    ResponseEntity<CharacterBoosterResponseDto> openNewCharacterBooster(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        CharacterBooster modelBooster = boosterService.getNewCharacterBooster(campaignId, userId);
        CharacterBoosterResponseDto responseDto = CharacterBoosterResponseDto.fromModelToDto(modelBooster);
        boosterService.discoverContentOfCharacterBooster(modelBooster);
        return ResponseEntity
                .created(URI.create("/api/campaign/" + campaignId + "/character-booster/" + modelBooster.getId()))
                .body(responseDto);
    }

    @GetMapping("/{campaignId}/character-booster/check-if-available")
    ResponseEntity<Boolean> canPlayerOpenCharacterBooster(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(boosterService.canOpenAValidCharacterBooster(campaignId, userId));
    }

    @GetMapping("/{campaignId}/items-booster/check-if-available")
    ResponseEntity<Boolean> canPlayerOpenItemsBooster(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(boosterService.canOpenAValidItemBooster(campaignId, userId));
    }

    @ExceptionHandler(DailyBoosterAlreadyOpened.class)
    public ResponseEntity<String> handleDailyBoosterAlreadyOpened(DailyBoosterAlreadyOpened ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidBooster.class)
    public ResponseEntity<String> handleInvalidBooster(InvalidBooster ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(ex.getMessage());
    }

}