package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.enums.BootsCategory;
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
            bootsTemplate.setRarity(dto.rarity());
            bootsTemplate.setTier(dto.tier());
            bootsTemplate.setValue(dto.value());
            bootsTemplate.setDiscovered(dto.discovered());
            bootsTemplate.setQuantity(0); // templates always start at 0 quantity
            bootsTemplate.setEquipped(dto.equipped());
            bootsTemplate.setCampaign(campaign);
            bootsTemplate.setCategory(BootsCategory.valueOf(dto.category()));
            bootsTemplate.setPhysicalDefense((int) Math.round((dto.tier() * 2.5 * dto.rarity() * 3)));
            bootsTemplate.setMagicalDefense((int) Math.round((dto.tier() * 1.5 * dto.rarity() * 2)));
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
        Map<String, Object> itemValues = new HashMap<>();
        Map<String, String> namesAndDescriptions = new HashMap<>();
        allItems.forEach(bootsTemplate -> {
            String name = bootsTemplate.getName();
            String description = bootsTemplate.getDescription();
            namesAndDescriptions.put(name, description);
        });
        itemValues.put("BootsTemplates", namesAndDescriptions);
        return itemValues;
    }
}
