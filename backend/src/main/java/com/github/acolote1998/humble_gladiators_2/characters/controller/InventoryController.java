package com.github.acolote1998.humble_gladiators_2.characters.controller;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import com.github.acolote1998.humble_gladiators_2.item.dto.ArmorInstanceResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.dto.BootsInstanceResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.dto.HelmetInstanceResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.dto.ShieldInstanceResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.dto.WeaponInstanceResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.instances.ArmorInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.BootsInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.HelmetInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.ShieldInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.WeaponInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

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
    ResponseEntity<ArmorInstanceResponseDto> equipArmorToHero(@AuthenticationPrincipal Jwt jwt,
                                                              @PathVariable Long campaignId, @PathVariable Long itemId) {
        String userId = jwt.getSubject();
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        ArmorInstance updatedArmor = characterService.equipArmor(hero, itemId, userId);
        ArmorInstanceResponseDto dto = ArmorInstanceResponseDto.fromModel(updatedArmor);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{campaignId}/character-instances/hero/unequip/armor/{itemId}")
    ResponseEntity<Void> unequipArmorToHero(@AuthenticationPrincipal Jwt jwt,
                                            @PathVariable Long campaignId, @PathVariable Long itemId) {
        String userId = jwt.getSubject();
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        characterService.unequipArmors(hero, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{campaignId}/character-instances/hero/equip/boots/{itemId}")
    ResponseEntity<BootsInstanceResponseDto> equipBootsToHero(@AuthenticationPrincipal Jwt jwt,
                                                              @PathVariable Long campaignId, @PathVariable Long itemId) {
        String userId = jwt.getSubject();
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        BootsInstance updatedBoots = characterService.equipBoots(hero, itemId, userId);
        BootsInstanceResponseDto dto = BootsInstanceResponseDto.fromModel(updatedBoots);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{campaignId}/character-instances/hero/unequip/boots/{itemId}")
    ResponseEntity<BootsInstanceResponseDto> unequipBootsToHero(@AuthenticationPrincipal Jwt jwt,
                                                                @PathVariable Long campaignId, @PathVariable Long itemId) {
        String userId = jwt.getSubject();
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        BootsInstance updatedBoots = characterService.unequipBoots(hero, itemId, userId);
        BootsInstanceResponseDto dto = BootsInstanceResponseDto.fromModel(updatedBoots);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{campaignId}/character-instances/hero/equip/helmet/{itemId}")
    ResponseEntity<HelmetInstanceResponseDto> equipHelmetToHero(@AuthenticationPrincipal Jwt jwt,
                                                                @PathVariable Long campaignId, @PathVariable Long itemId) {
        String userId = jwt.getSubject();
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        HelmetInstance updatedHelmet = characterService.equipHelmet(hero, itemId, userId);
        HelmetInstanceResponseDto dto = HelmetInstanceResponseDto.fromModel(updatedHelmet);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{campaignId}/character-instances/hero/unequip/helmet/{itemId}")
    ResponseEntity<HelmetInstanceResponseDto> unequipHelmetToHero(@AuthenticationPrincipal Jwt jwt,
                                                                  @PathVariable Long campaignId, @PathVariable Long itemId) {
        String userId = jwt.getSubject();
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        HelmetInstance updatedHelmet = characterService.unequipHelmet(hero, itemId, userId);
        HelmetInstanceResponseDto dto = HelmetInstanceResponseDto.fromModel(updatedHelmet);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{campaignId}/character-instances/hero/unequip/shield/{itemId}")
    ResponseEntity<ShieldInstanceResponseDto> equipShieldToHero(@AuthenticationPrincipal Jwt jwt,
                                                                @PathVariable Long campaignId, @PathVariable Long itemId) {
        String userId = jwt.getSubject();
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        ShieldInstance updatedShield = characterService.equipShield(hero, itemId, userId);
        ShieldInstanceResponseDto dto = ShieldInstanceResponseDto.fromModel(updatedShield);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{campaignId}/character-instances/hero/unequip/shield/{itemId}")
    ResponseEntity<ShieldInstanceResponseDto> unequipShieldToHero(@AuthenticationPrincipal Jwt jwt,
                                                                  @PathVariable Long campaignId, @PathVariable Long itemId) {
        String userId = jwt.getSubject();
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        ShieldInstance updatedShield = characterService.unequipShield(hero, itemId, userId);
        ShieldInstanceResponseDto dto = ShieldInstanceResponseDto.fromModel(updatedShield);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{campaignId}/character-instances/hero/unequip/weapon/{itemId}")
    ResponseEntity<WeaponInstanceResponseDto> unequipWeaponToHero(@AuthenticationPrincipal Jwt jwt,
                                                                  @PathVariable Long campaignId, @PathVariable Long itemId) {
        String userId = jwt.getSubject();
        CharacterInstance hero = characterService.getHero(campaignId, userId);
        WeaponInstance updatedWeapon = characterService.unequipWeapon(hero, itemId, userId);
        WeaponInstanceResponseDto dto = WeaponInstanceResponseDto.fromModel(updatedWeapon);
        return ResponseEntity.ok(dto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleItemNotFound(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Could not equip item: item not found.");
    }
}
