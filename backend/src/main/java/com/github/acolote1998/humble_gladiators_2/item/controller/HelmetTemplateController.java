package com.github.acolote1998.humble_gladiators_2.item.controller;

import com.github.acolote1998.humble_gladiators_2.item.dto.HelmetTemplateResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.service.HelmetService;
import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/campaign")
public class HelmetTemplateController {

    HelmetService helmetService;

    @Autowired
    public HelmetTemplateController(HelmetService helmetService) {
        this.helmetService = helmetService;
    }

    @GetMapping("/{campaignId}/helmet-templates")
    ResponseEntity<List<HelmetTemplateResponseDto>> getAllHelmetTemplatesForACampaign(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        List<HelmetTemplate> helmetTemplatesFromDb = helmetService.getAllHelmetTemplatesForACampaignAndUser(userId, campaignId);
        List<HelmetTemplateResponseDto> dtos = HelmetTemplateResponseDto.fromListOfHelmetTemplateToListOfDto(helmetTemplatesFromDb);
        return ResponseEntity.ok(dtos);
    }
}