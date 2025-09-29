package com.github.acolote1998.humble_gladiators_2.core.controller;

import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("/api/game")
@CrossOrigin
@Slf4j
public class GameController {

    GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/campaign")
    public ResponseEntity<Void> createNewCampaign(@AuthenticationPrincipal Jwt jwt,
                                                  @RequestBody GameCreationDtoRequest gameCreationDtoRequest) {
        String userId = jwt.getSubject();
        Campaign createdCampaign = new Campaign();
        try {
            createdCampaign = gameService.startGame(gameCreationDtoRequest, userId);
            log.info("Campaign " + createdCampaign.getId() + " - '" + createdCampaign.getName() + "' created successfully");
        } catch (Exception e) {
            log.error("Error creating new campaign: " + e.getMessage());
        }

        return ResponseEntity.created(URI.create("/api/campaign/" + createdCampaign.getId())).build();
    }

    @GetMapping("/state")
    public ResponseEntity<CampaignCreationStateType> getGameCreationState(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        Campaign campaign = gameService.getCampaignService().getCampaignBeingCreatedByUserId(userId);
        if (campaign == null) {
            throw new RuntimeException("Campaign Not Found");
        }
        return ResponseEntity.ok(campaign.getCampaignCreationState());
    }
}
