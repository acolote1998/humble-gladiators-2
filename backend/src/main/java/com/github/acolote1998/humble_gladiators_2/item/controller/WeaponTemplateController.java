package com.github.acolote1998.humble_gladiators_2.item.controller;

import com.github.acolote1998.humble_gladiators_2.item.dto.WeaponTemplateResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.service.WeaponService;
import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/campaign")
public class WeaponTemplateController {

    WeaponService weaponService;

    @Autowired
    public WeaponTemplateController(WeaponService weaponService) {
        this.weaponService = weaponService;
    }

    @GetMapping("/{campaignId}/weapon-templates")
    ResponseEntity<List<WeaponTemplateResponseDto>> getAllWeaponTemplatesForACampaign(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        List<WeaponTemplate> weaponTemplatesFromDb = weaponService.getAllWeaponTemplatesForACampaignAndUser(userId, campaignId);
        List<WeaponTemplateResponseDto> dtos = WeaponTemplateResponseDto.fromListOfWeaponTemplateToListOfDto(weaponTemplatesFromDb);
        return ResponseEntity.ok(dtos);
    }
}