package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.enums.BootsCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.BootsInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.BootsTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.BootsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BootsService {
    private BootsTemplateRepository bootsTemplateRepository;
    private GeminiService geminiService;

    public List<BootsTemplate> createTwentyFiveNewBootsTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveBoots(campaign);
        List<BootsTemplate> savedBootsTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            BootsTemplate bootsTemplate = new BootsTemplate();
            bootsTemplate.setName(dto.name());
            bootsTemplate.setDescription(dto.description());
            bootsTemplate.setUserId(campaign.getUserId());
            bootsTemplate.setRarity(dto.rarity());
            bootsTemplate.setTier(dto.tier());
            bootsTemplate.setDiscovered(false); //templates always start with discovered = false
            bootsTemplate.setQuantity(0); // templates always start at 0 quantity
            bootsTemplate.setEquipped(false); //templates always start with equipped = false
            bootsTemplate.setCampaign(campaign);
            bootsTemplate.setCategory(BootsCategory.valueOf(dto.category()));
            bootsTemplate.setPhysicalDefense((int) Math.round((dto.tier() * 2.5 * dto.rarity() * 3)));
            bootsTemplate.setMagicalDefense((int) Math.round((dto.tier() * 1.5 * dto.rarity() * 2)));
            bootsTemplate.setValue(
                    (bootsTemplate.getMagicalDefense() + bootsTemplate.getPhysicalDefense())
                            * bootsTemplate.getRarity()
                            * bootsTemplate.getTier());
            bootsTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedBootsTemplates.add(bootsTemplate);
        });

        bootsTemplateRepository.saveAll(savedBootsTemplates);

        log.info(savedBootsTemplates.size() + " boots successfully created an persisted");

        return savedBootsTemplates;
    }

    public BootsService(BootsTemplateRepository bootsTemplateRepository, GeminiService geminiService) {
        this.bootsTemplateRepository = bootsTemplateRepository;
        this.geminiService = geminiService;
    }

    public Map<String, Object> getShortAIGeneratedReport() {
        List<BootsTemplate> allItems = bootsTemplateRepository.findAll();
        // Sort by Tier (highest first) then Rarity (highest first)
        allItems.sort((b1, b2) -> {
            int tierComparison = Integer.compare(b2.getTier(), b1.getTier());
            if (tierComparison != 0) {
                return tierComparison;
            }
            return Integer.compare(b2.getRarity(), b1.getRarity());
        });

        Map<String, Object> itemValues = new HashMap<>();
        Map<String, String> namesAndDescriptions = new HashMap<>();
        allItems.forEach(bootsTemplate -> {
            String name = bootsTemplate.getName();
            String description = "Tier: " + bootsTemplate.getTier() + ", Rarity: " + bootsTemplate.getRarity() + ", Category: " + bootsTemplate.getCategory();
            namesAndDescriptions.put(name, description);
        });
        itemValues.put("BootsTemplates", namesAndDescriptions);
        return itemValues;
    }

    public List<BootsTemplate> getAllBootsTemplatesForACampaignAndUser(String userId, Long campaignId) {
        return bootsTemplateRepository.findAllByUserIdAndCampaign_Id(userId, campaignId);
    }

    public BootsTemplate getRandomBootTemplate(Long campaignId, String userId) {
        return bootsTemplateRepository.findRandomByCampaignIdAndUserId(campaignId, userId);
    }

    public BootsTemplate saveBoots(BootsTemplate boots) {
        return bootsTemplateRepository.save(boots);
    }

    private BootsInstance instanceFromBootsTemplate(BootsTemplate template, Inventory inventoryItBelongsTo) {
        BootsInstance instance = new BootsInstance();
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

    public List<BootsInstance> instancesFromBootsTemplates(List<BootsTemplate> templates, Inventory inventoryItBelongsTo) {
        List<BootsInstance> instances = new ArrayList<>();
        templates.forEach(template -> instances.add(instanceFromBootsTemplate(template, inventoryItBelongsTo)));
        return instances;
    }
}
