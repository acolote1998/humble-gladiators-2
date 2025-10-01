package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.enums.HelmetCategory;
import com.github.acolote1998.humble_gladiators_2.item.repository.HelmetTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HelmetService {
    GeminiService geminiService;
    HelmetTemplateRepository helmetTemplateRepository;

    public HelmetService(GeminiService geminiService, HelmetTemplateRepository helmetTemplateRepository) {
        this.geminiService = geminiService;
        this.helmetTemplateRepository = helmetTemplateRepository;
    }

    public Map<String, Object> getShortAIGeneratedReport() {
        List<HelmetTemplate> allItems = helmetTemplateRepository.findAll();
        Map<String, Object> itemValues = new HashMap<>();
        Map<String, String> namesAndDescriptions = new HashMap<>();
        allItems.forEach(helmetTemplate -> {
            String name = helmetTemplate.getName();
            String description = "Tier: " + helmetTemplate.getTier() + ", Rarity: " + helmetTemplate.getRarity() + ", Category: " + helmetTemplate.getCategory();
            namesAndDescriptions.put(name, description);
        });
        itemValues.put("HelmetTemplates", namesAndDescriptions);
        return itemValues;
    }

    public List<HelmetTemplate> createTwentyFiveNewHelmetsTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveHelmets(campaign);
        List<HelmetTemplate> savedHelmetsTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            HelmetTemplate helmetTemplate = new HelmetTemplate();
            helmetTemplate.setName(dto.name());
            helmetTemplate.setDescription(dto.description());
            helmetTemplate.setRarity(dto.rarity());
            helmetTemplate.setTier(dto.tier());
            helmetTemplate.setValue(dto.value());
            helmetTemplate.setDiscovered(dto.discovered());
            helmetTemplate.setQuantity(0); // templates always start at 0 quantity
            helmetTemplate.setEquipped(dto.equipped());
            helmetTemplate.setCampaign(campaign);
            helmetTemplate.setCategory(HelmetCategory.valueOf(dto.category()));
            helmetTemplate.setPhysicalDefense((int) Math.round((dto.tier() * 1.5 * dto.rarity() * 2)));
            helmetTemplate.setMagicalDefense((int) Math.round((dto.tier() * 2.5 * dto.rarity() * 3)));
            helmetTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedHelmetsTemplates.add(helmetTemplate);
        });

        helmetTemplateRepository.saveAll(savedHelmetsTemplates);

        log.info(savedHelmetsTemplates.size() + " helmets successfully created an persisted");

        return savedHelmetsTemplates;
    }
}
