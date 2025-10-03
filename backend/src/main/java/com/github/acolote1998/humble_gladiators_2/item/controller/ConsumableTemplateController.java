package com.github.acolote1998.humble_gladiators_2.item.controller;

import com.github.acolote1998.humble_gladiators_2.item.dto.ConsumableTemplateResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.service.ConsumableService;
import com.github.acolote1998.humble_gladiators_2.item.templates.ConsumableTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/campaign")
public class ConsumableTemplateController {

    ConsumableService consumableService;

    @Autowired
    public ConsumableTemplateController(ConsumableService consumableService) {
        this.consumableService = consumableService;
    }

    @GetMapping("/{campaignId}/consumable-templates")
    ResponseEntity<List<ConsumableTemplateResponseDto>> getAllConsumableTemplatesForACampaign(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        List<ConsumableTemplate> consumableTemplatesFromDb = consumableService.getAllConsumableTemplatesForACampaignAndUser(userId, campaignId);
        List<ConsumableTemplateResponseDto> dtos = ConsumableTemplateResponseDto.fromListOfConsumableTemplateToListOfDto(consumableTemplatesFromDb);
        return ResponseEntity.ok(dtos);
    }
}