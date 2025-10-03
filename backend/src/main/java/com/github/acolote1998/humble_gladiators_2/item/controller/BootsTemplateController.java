package com.github.acolote1998.humble_gladiators_2.item.controller;

import com.github.acolote1998.humble_gladiators_2.item.dto.BootsTemplateResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.service.BootsService;
import com.github.acolote1998.humble_gladiators_2.item.templates.BootsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/campaign")
public class BootsTemplateController {

    BootsService bootsService;

    @Autowired
    public BootsTemplateController(BootsService bootsService) {
        this.bootsService = bootsService;
    }

    @GetMapping("/{campaignId}/boots-templates")
    ResponseEntity<List<BootsTemplateResponseDto>> getAllBootsTemplatesForACampaign(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        List<BootsTemplate> bootsTemplatesFromDb = bootsService.getAllBootsTemplatesForACampaignAndUser(userId, campaignId);
        List<BootsTemplateResponseDto> dtos = BootsTemplateResponseDto.fromListOfBootsTemplateToListOfDto(bootsTemplatesFromDb);
        return ResponseEntity.ok(dtos);
    }
}