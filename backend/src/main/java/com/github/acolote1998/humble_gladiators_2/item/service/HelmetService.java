package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.repository.HelmetTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HelmetService {
    GeminiService geminiService;
    HelmetTemplateRepository helmetTemplateRepository;

    public HelmetService(GeminiService geminiService, HelmetTemplateRepository helmetTemplateRepository) {
        this.geminiService = geminiService;
        this.helmetTemplateRepository = helmetTemplateRepository;
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
            helmetTemplate.setMagicalDefense(dto.magicalDefense());
            helmetTemplate.setPhysicalDefense(dto.physicalDefense());
            helmetTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedHelmetsTemplates.add(helmetTemplate);
        });

        helmetTemplateRepository.saveAll(savedHelmetsTemplates);

        log.info(savedHelmetsTemplates.size() + " helmets successfully created an persisted");

        return savedHelmetsTemplates;
    }
}
