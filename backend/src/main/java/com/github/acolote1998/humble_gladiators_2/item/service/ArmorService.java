package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory;
import com.github.acolote1998.humble_gladiators_2.item.repository.ArmorTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ArmorService {
    private GeminiService geminiService;
    private ArmorTemplateRepository armorTemplateRepository;

    public ArmorService(GeminiService geminiService, ArmorTemplateRepository armorTemplateRepository) {
        this.geminiService = geminiService;
        this.armorTemplateRepository = armorTemplateRepository;
    }

    public Map<String, Object> getShortAIGeneratedReport() {
        List<ArmorTemplate> allItems = armorTemplateRepository.findAll();
        // Sort by Tier (highest first) then Rarity (highest first)
        allItems.sort((a1, a2) -> {
            int tierComparison = Integer.compare(a2.getTier(), a1.getTier());
            if (tierComparison != 0) {
                return tierComparison;
            }
            return Integer.compare(a2.getRarity(), a1.getRarity());
        });

        Map<String, Object> itemValues = new HashMap<>();
        Map<String, String> namesAndDescriptions = new HashMap<>();
        allItems.forEach(armorTemplate -> {
            String name = armorTemplate.getName();
            String description = "Tier: " + armorTemplate.getTier() + ", Rarity: " + armorTemplate.getRarity() + ", Category: " + armorTemplate.getCategory();
            namesAndDescriptions.put(name, description);
        });
        itemValues.put("ArmorTemplates", namesAndDescriptions);
        return itemValues;
    }

    public List<ArmorTemplate> createTwentyFiveNewArmorTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveArmors(campaign);
        List<ArmorTemplate> savedArmorTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            ArmorTemplate armorTemplate = new ArmorTemplate();
            armorTemplate.setName(dto.name());
            armorTemplate.setDescription(dto.description());
            armorTemplate.setUserId(campaign.getUserId());
            armorTemplate.setRarity(dto.rarity());
            armorTemplate.setTier(dto.tier());
            armorTemplate.setValue(dto.value());
            armorTemplate.setDiscovered(dto.discovered());
            armorTemplate.setQuantity(0); // templates always start at 0 quantity
            armorTemplate.setEquipped(dto.equipped());
            armorTemplate.setCampaign(campaign);
            armorTemplate.setCategory(ArmorCategory.valueOf(dto.category()));
            armorTemplate.setPhysicalDefense((int) Math.round((dto.tier() * 1 * dto.rarity() * 1.5)));
            armorTemplate.setMagicalDefense((int) Math.round((dto.tier() * 0.2 * dto.rarity() * 0.5)));
            armorTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedArmorTemplates.add(armorTemplate);
        });

        armorTemplateRepository.saveAll(savedArmorTemplates);

        log.info(savedArmorTemplates.size() + " armors successfully created an persisted");

        return savedArmorTemplates;
    }

    public List<ArmorTemplate> getAllArmorTemplatesForACampaignAndUser(String userId, Long campaignId) {
        return armorTemplateRepository.findAllByUserIdAndCampaign_Id(userId, campaignId);
    }

    public ArmorTemplate getRandomArmorTemplate(Long campaignId, String userId) {
        return armorTemplateRepository.findRandomByCampaignIdAndUserId(campaignId, userId);
    }

}
