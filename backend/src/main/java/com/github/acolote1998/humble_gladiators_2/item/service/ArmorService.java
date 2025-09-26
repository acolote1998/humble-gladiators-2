package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryOperator;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.model.RequirementEntry;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
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

            Requirement requirement = new Requirement();
            requirement.setCampaign(campaign);

            List<RequirementEntry> requirementEntries = new ArrayList<>();
            if (dto.requirement() != null && !dto.requirement().requirements().isEmpty()) {
                dto.requirement().requirements().forEach(entryDto -> {
                    RequirementEntry requirementEntry = new RequirementEntry();
                    requirementEntry.setCampaign(campaign);
                    requirementEntry.setRequirement(requirement);
                    requirementEntry.setValue(entryDto.value());
                    requirementEntry.setOperator(RequirementEntryOperator.valueOf(entryDto.operator()));
                    requirementEntry.setRequirementType(RequirementEntryType.valueOf(entryDto.requirementType()));
                    requirementEntries.add(requirementEntry);
                });
            }

            requirement.setRequirements(requirementEntries);
            armorTemplate.setRequirement(requirement);

            armorTemplateRepository.save(armorTemplate);
            savedArmorTemplates.add(armorTemplate);
        });

        return savedArmorTemplates;
    }

}
