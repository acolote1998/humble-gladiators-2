package com.github.acolote1998.humble_gladiators_2.characters.controller;

import com.github.acolote1998.humble_gladiators_2.characters.dto.EquipArmorRequestDto;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import com.github.acolote1998.humble_gladiators_2.item.dto.ArmorInstanceResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.instances.ArmorInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/campaign")
public class InventoryController {

    CharacterService characterService;
    CampaignService campaignService;

    @Autowired
    public InventoryController(CharacterService characterService, CampaignService campaignService) {
        this.characterService = characterService;
        this.campaignService = campaignService;
    }

    @PatchMapping("/{campaignId}/character-instances/hero/equip/armor/{itemId}")
    ResponseEntity<ArmorInstanceResponseDto> equipArmorToHero(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId, @RequestBody EquipArmorRequestDto dtoRequest) {
        ArmorInstance updatedArmor = characterService.equipArmor(dtoRequest.armorId());
        ArmorInstanceResponseDto dto = ArmorInstanceResponseDto.fromModel(updatedArmor);
        return ResponseEntity.ok(dto);
    }
}
