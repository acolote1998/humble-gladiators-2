package com.github.acolote1998.humble_gladiators_2.characters.controller;

import com.github.acolote1998.humble_gladiators_2.characters.dto.FullCharacterResponseDto;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/campaign")
public class CharacterController {

    CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/{campaignId}/character-instances")
    ResponseEntity<List<FullCharacterResponseDto>> getAllFullCharactersForACampaign(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        List<CharacterInstance> charactersFromDb = characterService.getAllCharacterInstancesForACampaignAndUser(userId, campaignId);
        List<FullCharacterResponseDto> dtos = FullCharacterResponseDto.fromListOfCharInstToListOfCharDto(charactersFromDb);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{campaignId}/character-instances")
    ResponseEntity<FullCharacterResponseDto> createCharacterForACampaign(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
    }
}