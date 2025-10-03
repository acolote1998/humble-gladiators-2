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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CharacterService {
    GeminiService geminiService;
    CharacterInstanceRepository characterInstanceRepository;

    public CharacterService(GeminiService geminiService, CharacterInstanceRepository characterInstanceRepository) {
        this.geminiService = geminiService;
        this.characterInstanceRepository = characterInstanceRepository;
    }

    public Map<String, Object> getShortAIGeneratedReport() {
        List<CharacterInstance> allCharacters = characterInstanceRepository.findAll();
        // Sort by Tier (highest first) then Rarity (highest first)
        allCharacters.sort((c1, c2) -> {
            int tierComparison = Integer.compare(c2.getTier(), c1.getTier());
            if (tierComparison != 0) {
                return tierComparison;
            }
            return Integer.compare(c2.getRarity(), c1.getRarity());
        });

        Map<String, Object> characterValues = new HashMap<>();
        Map<String, String> namesAndDescriptions = new HashMap<>();
        allCharacters.forEach(characterInstance -> {
            String name = characterInstance.getName();
            String description = "Tier: " + characterInstance.getTier() + ", Rarity: " + characterInstance.getRarity() + ", Category: " + characterInstance.getCategory();
            ;
            namesAndDescriptions.put(name, description);
        });
        characterValues.put("CharacterInstances", namesAndDescriptions);
        return characterValues;
    }

    public List<CharacterInstance> createTenNPCsOfDesiredTier(Campaign campaign, Integer tier) {
        List<CharacterInstance> existingCharactersForContext = characterInstanceRepository.findAll();
        List<CharacterFromGeminiDto> generatedDtos = geminiService.generateTenNpcsOfDesiredTier(campaign, existingCharactersForContext, tier);
        List<CharacterInstance> savedCharacterInstances = new ArrayList<>();

        generatedDtos.forEach(characterFromGeminiDto -> {
            CharacterInstance characterInstance = new CharacterInstance();
            characterInstance.setUserId(campaign.getUserId());
            characterInstance.setStats(Stats.mapStatsFromCharacterFromGeminiDto(characterFromGeminiDto));
            characterInstance.setCharacterType(characterFromGeminiDto.characterType());
            characterInstance.setCategory(characterFromGeminiDto.category());
            characterInstance.setName(characterFromGeminiDto.name());
            characterInstance.setDescription(characterFromGeminiDto.description());
            characterInstance.setDiscovered(characterFromGeminiDto.discovered());
            characterInstance.setCampaign(campaign);
            characterInstance.setRarity(characterFromGeminiDto.rarity());
            characterInstance.setTier(characterFromGeminiDto.tier());
            characterInstance.setGoldReward(characterFromGeminiDto.stats().level() * 10 * characterFromGeminiDto.rarity() * characterFromGeminiDto.tier());
            characterInstance.setExpReward(characterFromGeminiDto.stats().level() * 20 * characterFromGeminiDto.rarity() * characterFromGeminiDto.tier());
            Inventory inventory = InventoryService.createBlankInventory();
            characterInstance.setInventory(inventory);
            savedCharacterInstances.add(characterInstance);
        });
        characterInstanceRepository.saveAll(savedCharacterInstances);
        log.info(savedCharacterInstances.size() + " characters tier " + tier + " successfully created and persisted");
        return savedCharacterInstances;
    }

    public List<CharacterInstance> getAllCharacterInstancesForACampaignAndUser(String userId, Long campaignId) {
        return characterInstanceRepository.findAllByUserIdAndCampaign_Id(userId, campaignId);
    }
}
