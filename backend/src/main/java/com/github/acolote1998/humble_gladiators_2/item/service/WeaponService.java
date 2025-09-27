package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.repository.WeaponTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate;
import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WeaponService {
    GeminiService geminiService;
    WeaponTemplateRepository weaponTemplateRepository;

    public WeaponService(GeminiService geminiService, WeaponTemplateRepository weaponTemplateRepository) {
        this.geminiService = geminiService;
        this.weaponTemplateRepository = weaponTemplateRepository;
    }

    public List<WeaponTemplate> createTwentyFiveNewWeaponTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveWeapons(campaign);
        List<WeaponTemplate> savedWeaponTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            WeaponTemplate weaponTemplate = new WeaponTemplate();
            weaponTemplate.setName(dto.name());
            weaponTemplate.setDescription(dto.description());
            weaponTemplate.setRarity(dto.rarity());
            weaponTemplate.setTier(dto.tier());
            weaponTemplate.setValue(dto.value());
            weaponTemplate.setDiscovered(dto.discovered());
            weaponTemplate.setQuantity(0); // templates always start at 0 quantity
            weaponTemplate.setEquipped(dto.equipped());
            weaponTemplate.setCampaign(campaign);
            weaponTemplate.setPhysicalDamage(dto.physicalDamage());
            weaponTemplate.setMagicalDamage(dto.magicalDamage());
            weaponTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedWeaponTemplates.add(weaponTemplate);
        });

        weaponTemplateRepository.saveAll(savedWeaponTemplates);

        log.info(savedWeaponTemplates.size() + " weapons successfully created an persisted");

        return savedWeaponTemplates;
    }
}
