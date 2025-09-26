package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.repository.ArmorTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArmorService {
    GeminiService geminiService;
    ArmorTemplateRepository armorTemplateRepository;

    public ArmorService(GeminiService geminiService, ArmorTemplateRepository armorTemplateRepository) {
        this.geminiService = geminiService;
        this.armorTemplateRepository = armorTemplateRepository;
    }

    public List<ArmorTemplate> createTwentyFiveNewArmorTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveArmors(campaign);
        List<ArmorTemplate> savedArmorTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            ArmorTemplate armorTemplate = new ArmorTemplate();
            armorTemplate.setName(dto.name());
            armorTemplate.setDescription(dto.description());
            armorTemplate.setRarity(dto.rarity());
            armorTemplate.setTier(dto.tier());
            armorTemplate.setValue(dto.value());
            armorTemplate.setDiscovered(dto.discovered());
            armorTemplate.setQuantity(0); // templates always start at 0 quantity
            armorTemplate.setEquipped(dto.equipped());
            armorTemplate.setCampaign(campaign);
            armorTemplate.setMagicalDefense(dto.magicalDefense());
            armorTemplate.setPhysicalDefense(dto.physicalDefense());
            armorTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedArmorTemplates.add(armorTemplate);
        });

        armorTemplateRepository.saveAll(savedArmorTemplates);
        return savedArmorTemplates;
    }

}
