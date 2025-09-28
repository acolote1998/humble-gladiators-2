package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.repository.SpellTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SpellService {
    GeminiService geminiService;
    SpellTemplateRepository spellTemplateRepository;

    public SpellService(GeminiService geminiService, SpellTemplateRepository spellTemplateRepository) {
        this.geminiService = geminiService;
        this.spellTemplateRepository = spellTemplateRepository;
    }

    public List<SpellTemplate> createTwentyFiveNewSpellTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveSpells(campaign);
        List<SpellTemplate> savedSpellTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            SpellTemplate spellTemplate = new SpellTemplate();
            spellTemplate.setName(dto.name());
            spellTemplate.setDescription(dto.description());
            spellTemplate.setRarity(dto.rarity());
            spellTemplate.setTier(dto.tier());
            spellTemplate.setValue(dto.value());
            spellTemplate.setDiscovered(dto.discovered());
            spellTemplate.setQuantity(0); // templates always start at 0 quantity
            spellTemplate.setEquipped(dto.equipped());
            spellTemplate.setCampaign(campaign);
            spellTemplate.setPhysicalDamage(0);
            spellTemplate.setMagicalDamage((int) Math.round((dto.tier() * 2.5 * dto.rarity() * 3)));
            spellTemplate.setRestoreHp((int) Math.round((dto.tier() * 2.5 * dto.rarity() * 3)));
            spellTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedSpellTemplates.add(spellTemplate);
        });

        spellTemplateRepository.saveAll(savedSpellTemplates);

        log.info(savedSpellTemplates.size() + " spells successfully created an persisted");

        return savedSpellTemplates;
    }
}
