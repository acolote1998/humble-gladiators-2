package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.repository.ShieldTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.ShieldTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ShieldService {
    GeminiService geminiService;
    ShieldTemplateRepository shieldTemplateRepository;

    public ShieldService(GeminiService geminiService, ShieldTemplateRepository shieldTemplateRepository) {
        this.geminiService = geminiService;
        this.shieldTemplateRepository = shieldTemplateRepository;
    }

    public List<ShieldTemplate> createTwentyFiveNewShieldTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveShields(campaign);
        List<ShieldTemplate> savedShieldTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            ShieldTemplate shieldTemplate = new ShieldTemplate();
            shieldTemplate.setName(dto.name());
            shieldTemplate.setDescription(dto.description());
            shieldTemplate.setRarity(dto.rarity());
            shieldTemplate.setTier(dto.tier());
            shieldTemplate.setValue(dto.value());
            shieldTemplate.setDiscovered(dto.discovered());
            shieldTemplate.setQuantity(0); // templates always start at 0 quantity
            shieldTemplate.setEquipped(dto.equipped());
            shieldTemplate.setCampaign(campaign);
            shieldTemplate.setMagicalDefense(dto.magicalDefense());
            shieldTemplate.setPhysicalDefense(dto.physicalDefense());
            shieldTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedShieldTemplates.add(shieldTemplate);
        });

        shieldTemplateRepository.saveAll(savedShieldTemplates);

        log.info(savedShieldTemplates.size() + " shields successfully created an persisted");

        return savedShieldTemplates;
    }
}
