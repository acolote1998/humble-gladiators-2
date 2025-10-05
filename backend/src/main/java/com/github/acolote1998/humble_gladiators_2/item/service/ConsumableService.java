package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.enums.ConsumablesCategory;
import com.github.acolote1998.humble_gladiators_2.item.repository.ConsumableTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.ConsumableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ConsumableService {
    private ConsumableTemplateRepository consumableTemplateRepository;
    private GeminiService geminiService;

    public ConsumableService(ConsumableTemplateRepository consumableTemplateRepository, GeminiService geminiService) {
        this.consumableTemplateRepository = consumableTemplateRepository;
        this.geminiService = geminiService;
    }

    public Map<String, Object> getShortAIGeneratedReport() {
        List<ConsumableTemplate> allItems = consumableTemplateRepository.findAll();
        // Sort by Tier (highest first) then Rarity (highest first)
        allItems.sort((c1, c2) -> {
            int tierComparison = Integer.compare(c2.getTier(), c1.getTier());
            if (tierComparison != 0) {
                return tierComparison;
            }
            return Integer.compare(c2.getRarity(), c1.getRarity());
        });

        Map<String, Object> itemValues = new HashMap<>();
        Map<String, String> namesAndDescriptions = new HashMap<>();
        allItems.forEach(consumableTemplate -> {
            String name = consumableTemplate.getName();
            String description = "Tier: " + consumableTemplate.getTier() + ", Rarity: " + consumableTemplate.getRarity() + ", Category: " + consumableTemplate.getCategory();
            namesAndDescriptions.put(name, description);
        });
        itemValues.put("ConsumableTemplates", namesAndDescriptions);
        return itemValues;
    }

    public List<ConsumableTemplate> createTwentyFiveNewConsumableTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveConsumables(campaign);
        List<ConsumableTemplate> savedConsumableTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            ConsumableTemplate consumableTemplate = new ConsumableTemplate();
            consumableTemplate.setName(dto.name());
            consumableTemplate.setDescription(dto.description());
            consumableTemplate.setUserId(campaign.getUserId());
            consumableTemplate.setRarity(dto.rarity());
            consumableTemplate.setTier(dto.tier());
            consumableTemplate.setValue(dto.value());
            consumableTemplate.setDiscovered(dto.discovered());
            consumableTemplate.setQuantity(0); // templates always start at 0 quantity
            consumableTemplate.setEquipped(dto.equipped());
            consumableTemplate.setCampaign(campaign);
            consumableTemplate.setCategory(ConsumablesCategory.valueOf(dto.category()));
            consumableTemplate.setRestoreHp((int) Math.round((dto.tier() * 1.5 * dto.rarity() * 1.5)));
            consumableTemplate.setRestoreMp((int) Math.round((dto.tier() * 2 * dto.rarity() * 4)));
            consumableTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedConsumableTemplates.add(consumableTemplate);
        });

        consumableTemplateRepository.saveAll(savedConsumableTemplates);

        log.info(savedConsumableTemplates.size() + " consumables successfully created an persisted");

        return savedConsumableTemplates;
    }

    public List<ConsumableTemplate> getAllConsumableTemplatesForACampaignAndUser(String userId, Long campaignId) {
        return consumableTemplateRepository.findAllByUserIdAndCampaign_Id(userId, campaignId);
    }

    public ConsumableTemplate getRandomConsumableTemplate(Long campaignId, String userId) {
        return consumableTemplateRepository.findRandomByCampaignIdAndUserId(campaignId, userId);
    }

    public ConsumableTemplate saveConsumable(ConsumableTemplate consumable) {
        return consumableTemplateRepository.save(consumable);
    }
}
