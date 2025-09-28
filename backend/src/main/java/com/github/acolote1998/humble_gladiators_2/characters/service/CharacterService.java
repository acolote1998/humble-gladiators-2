package com.github.acolote1998.humble_gladiators_2.characters.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.characters.repository.CharacterInstanceRepository;
import com.github.acolote1998.humble_gladiators_2.core.dto.CharacterFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CharacterService {
    GeminiService geminiService;
    CharacterInstanceRepository characterInstanceRepository;

    public CharacterService(GeminiService geminiService, CharacterInstanceRepository characterInstanceRepository) {
        this.geminiService = geminiService;
        this.characterInstanceRepository = characterInstanceRepository;
    }

    public List<CharacterInstance> createTenNPCsOfDesiredTier(Campaign campaign, Integer tier) {
        List<CharacterInstance> existingCharactersForContext = characterInstanceRepository.findAll();
        List<CharacterFromGeminiDto> generatedDtos = geminiService.generateTenNpcsOfDesiredTier(campaign, existingCharactersForContext, tier);
        List<CharacterInstance> savedCharacterInstances = new ArrayList<>();

        generatedDtos.forEach(characterFromGeminiDto -> {
            CharacterInstance characterInstance = new CharacterInstance();
            characterInstance.setStats(Stats.mapStatsFromCharacterFromGeminiDto(characterFromGeminiDto));
            characterInstance.setCharacterType(characterFromGeminiDto.characterType());
            characterInstance.setName(characterFromGeminiDto.name());
            characterInstance.setDiscovered(characterFromGeminiDto.discovered());
            characterInstance.setCampaign(campaign);
            characterInstance.setRarity(characterFromGeminiDto.rarity());
            characterInstance.setTier(characterFromGeminiDto.tier());
            characterInstance.setGoldReward(characterFromGeminiDto.goldReward());
            characterInstance.setExpReward(characterFromGeminiDto.expReward());
            Inventory inventory = InventoryService.createBlankInventory();
            characterInstance.setInventory(inventory);
            savedCharacterInstances.add(characterInstance);
        });
        characterInstanceRepository.saveAll(savedCharacterInstances);
        log.info(savedCharacterInstances.size() + " characters successfully created and persisted");
        return savedCharacterInstances;
    }
}
