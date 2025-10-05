package com.github.acolote1998.humble_gladiators_2.booster.controller;

import com.github.acolote1998.humble_gladiators_2.booster.dto.ItemBoosterResponseDto;
import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import com.github.acolote1998.humble_gladiators_2.booster.service.ItemsBoosterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/campaign")
public class BoosterController {

    ItemsBoosterService itemsBoosterService;

    public BoosterController(ItemsBoosterService itemsBoosterService) {
        this.itemsBoosterService = itemsBoosterService;
    }

    @PostMapping("/{campaignId}/items-booster")
    ResponseEntity<ItemBoosterResponseDto> openNewItemBooster(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        ItemsBooster modelBooster = itemsBoosterService.getNewItemsBooster(campaignId, userId);
        ItemBoosterResponseDto responseDto = ItemBoosterResponseDto.fromModelToDto(modelBooster);
        return ResponseEntity
                .created(URI.create("/api/campaign/" + campaignId + "/items-booster/" + modelBooster.getId()))
                .body(responseDto);
    }

}