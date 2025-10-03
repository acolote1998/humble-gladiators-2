package com.github.acolote1998.humble_gladiators_2.item.controller;

import com.github.acolote1998.humble_gladiators_2.item.dto.ShieldTemplateResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.service.ShieldService;
import com.github.acolote1998.humble_gladiators_2.item.templates.ShieldTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/campaign")
public class ShieldTemplateController {

    ShieldService shieldService;

    @Autowired
    public ShieldTemplateController(ShieldService shieldService) {
        this.shieldService = shieldService;
    }

    @GetMapping("/{campaignId}/shield-templates")
    ResponseEntity<List<ShieldTemplateResponseDto>> getAllShieldTemplatesForACampaign(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        List<ShieldTemplate> shieldTemplatesFromDb = shieldService.getAllShieldTemplatesForACampaignAndUser(userId, campaignId);
        List<ShieldTemplateResponseDto> dtos = ShieldTemplateResponseDto.fromListOfShieldTemplateToListOfDto(shieldTemplatesFromDb);
        return ResponseEntity.ok(dtos);
    }
}