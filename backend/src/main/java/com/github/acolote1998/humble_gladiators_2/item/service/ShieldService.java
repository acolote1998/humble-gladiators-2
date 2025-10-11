package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.enums.ShieldCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.ShieldInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.ShieldTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.ShieldTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ShieldService {
    GeminiService geminiService;
    ShieldTemplateRepository shieldTemplateRepository;

    public ShieldService(GeminiService geminiService, ShieldTemplateRepository shieldTemplateRepository) {
        this.geminiService = geminiService;
        this.shieldTemplateRepository = shieldTemplateRepository;
    }

    public Map<String, Object> getShortAIGeneratedReport() {
        List<ShieldTemplate> allItems = shieldTemplateRepository.findAll();
        // Sort by Tier (highest first) then Rarity (highest first)
        allItems.sort((s1, s2) -> {
            int tierComparison = Integer.compare(s2.getTier(), s1.getTier());
            if (tierComparison != 0) {
                return tierComparison;
            }
            return Integer.compare(s2.getRarity(), s1.getRarity());
        });

        Map<String, Object> itemValues = new HashMap<>();
        Map<String, String> namesAndDescriptions = new HashMap<>();
        allItems.forEach(shieldTemplate -> {
            String name = shieldTemplate.getName();
            String description = "Tier: " + shieldTemplate.getTier() + ", Rarity: " + shieldTemplate.getRarity() + ", Category: " + shieldTemplate.getCategory();
            namesAndDescriptions.put(name, description);
        });
        itemValues.put("ShieldTemplates", namesAndDescriptions);
        return itemValues;
    }

    public List<ShieldTemplate> createTwentyFiveNewShieldTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveShields(campaign);
        List<ShieldTemplate> savedShieldTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            ShieldTemplate shieldTemplate = new ShieldTemplate();
            shieldTemplate.setName(dto.name());
            shieldTemplate.setDescription(dto.description());
            shieldTemplate.setUserId(campaign.getUserId());
            shieldTemplate.setRarity(dto.rarity());
            shieldTemplate.setTier(dto.tier());
            shieldTemplate.setDiscovered(false);
            shieldTemplate.setQuantity(0); // templates always start at 0 quantity
            shieldTemplate.setEquipped(false);
            shieldTemplate.setCampaign(campaign);
            shieldTemplate.setCategory(ShieldCategory.valueOf(dto.category()));
            shieldTemplate.setPhysicalDefense((int) Math.round((dto.tier() * 4 * dto.rarity() * 4.5)));
            shieldTemplate.setMagicalDefense((int) Math.round((dto.tier() * 4 * dto.rarity() * 4.5)));
            shieldTemplate.setValue(
                    (shieldTemplate.getMagicalDefense() + shieldTemplate.getPhysicalDefense())
                            * shieldTemplate.getTier()
                            * shieldTemplate.getRarity());
            shieldTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedShieldTemplates.add(shieldTemplate);
        });

        shieldTemplateRepository.saveAll(savedShieldTemplates);

        log.info(savedShieldTemplates.size() + " shields successfully created an persisted");

        return savedShieldTemplates;
    }

    public List<ShieldTemplate> getAllShieldTemplatesForACampaignAndUser(String userId, Long campaignId) {
        return shieldTemplateRepository.findAllByUserIdAndCampaign_Id(userId, campaignId);
    }

    public ShieldTemplate getRandomShieldTemplate(Long campaignId, String userId) {
        return shieldTemplateRepository.findRandomByCampaignIdAndUserId(campaignId, userId);
    }

    public ShieldTemplate saveShield(ShieldTemplate shield) {
        return shieldTemplateRepository.save(shield);
    }

    private ShieldInstance instanceFromShieldTemplate(ShieldTemplate template, Inventory inventoryItBelongsTo) {
        ShieldInstance instance = new ShieldInstance();
        instance.setTemplate(template);
        instance.setDiscovered(true);
        instance.setInventory(inventoryItBelongsTo);
        instance.setDescription(template.getDescription());
        instance.setCampaign(template.getCampaign());
        instance.setEquipped(false);
        instance.setName(template.getName());
        instance.setQuantity(1);
        instance.setRarity(template.getRarity());
        instance.setTier(template.getTier());
        instance.setRequirement(Requirement.cloneRequirement(template.getRequirement()));
        instance.setValue(template.getValue());
        return instance;
    }

    public List<ShieldInstance> instancesFromShieldTemplates(List<ShieldTemplate> templates, Inventory inventoryItBelongsTo) {
        List<ShieldInstance> instances = new ArrayList<>();
        templates.forEach(template -> instances.add(instanceFromShieldTemplate(template, inventoryItBelongsTo)));
        return instances;
    }
}
