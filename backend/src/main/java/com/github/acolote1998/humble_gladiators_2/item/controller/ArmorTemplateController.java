package com.github.acolote1998.humble_gladiators_2.item.controller;

import com.github.acolote1998.humble_gladiators_2.item.dto.ArmorTemplateResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.service.ArmorService;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/campaign")
public class ArmorTemplateController {

    ArmorService armorService;

    @Autowired
    public ArmorTemplateController(ArmorService armorService) {
        this.armorService = armorService;
    }

    @GetMapping("/{campaignId}/armor-templates")
    ResponseEntity<List<ArmorTemplateResponseDto>> getAllArmorTemplatesForACampaign(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        List<ArmorTemplate> armorTemplatesFromDb = armorService.getAllArmorTemplatesForACampaignAndUser(userId, campaignId);
        List<ArmorTemplateResponseDto> dtos = ArmorTemplateResponseDto.fromListOfArmorTemplateToListOfDto(armorTemplatesFromDb);
        return ResponseEntity.ok(dtos);
    }
}