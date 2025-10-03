package com.github.acolote1998.humble_gladiators_2.item.controller;

import com.github.acolote1998.humble_gladiators_2.item.dto.SpellTemplateResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.service.SpellService;
import com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/campaign")
public class SpellTemplateController {

    SpellService spellService;

    @Autowired
    public SpellTemplateController(SpellService spellService) {
        this.spellService = spellService;
    }

    @GetMapping("/{campaignId}/spell-templates")
    ResponseEntity<List<SpellTemplateResponseDto>> getAllSpellTemplatesForACampaign(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        List<SpellTemplate> spellTemplatesFromDb = spellService.getAllSpellTemplatesForACampaignAndUser(userId, campaignId);
        List<SpellTemplateResponseDto> dtos = SpellTemplateResponseDto.fromListOfSpellTemplateToListOfDto(spellTemplatesFromDb);
        return ResponseEntity.ok(dtos);
    }
}