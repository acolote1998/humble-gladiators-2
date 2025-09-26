package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.repository.ConsumableTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.ConsumableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ConsumableService {
    private ConsumableTemplateRepository consumableTemplateRepository;
    private GeminiService geminiService;

    public ConsumableService(ConsumableTemplateRepository consumableTemplateRepository, GeminiService geminiService) {
        this.consumableTemplateRepository = consumableTemplateRepository;
        this.geminiService = geminiService;
    }

    public List<ConsumableTemplate> createTwentyFiveNewConsumableTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveConsumables(campaign);
        List<ConsumableTemplate> savedConsumableTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            ConsumableTemplate consumableTemplate = new ConsumableTemplate();
            consumableTemplate.setName(dto.name());
            consumableTemplate.setDescription(dto.description());
            consumableTemplate.setRarity(dto.rarity());
            consumableTemplate.setTier(dto.tier());
            consumableTemplate.setValue(dto.value());
            consumableTemplate.setDiscovered(dto.discovered());
            consumableTemplate.setQuantity(0); // templates always start at 0 quantity
            consumableTemplate.setEquipped(dto.equipped());
            consumableTemplate.setCampaign(campaign);
            consumableTemplate.setRestoreHp(dto.restoreHp());
            consumableTemplate.setRestoreMp(dto.restoreMp());
            consumableTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedConsumableTemplates.add(consumableTemplate);
        });

        consumableTemplateRepository.saveAll(savedConsumableTemplates);

        log.info(savedConsumableTemplates.size() + " consumables successfully created an persisted");

        return savedConsumableTemplates;
    }
}
