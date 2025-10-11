package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.enums.WeaponCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.WeaponInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.WeaponTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WeaponService {
    GeminiService geminiService;
    WeaponTemplateRepository weaponTemplateRepository;

    public WeaponService(GeminiService geminiService, WeaponTemplateRepository weaponTemplateRepository) {
        this.geminiService = geminiService;
        this.weaponTemplateRepository = weaponTemplateRepository;
    }

    public Map<String, Object> getShortAIGeneratedReport() {
        List<WeaponTemplate> allItems = weaponTemplateRepository.findAll();
        // Sort by Tier (highest first) then Rarity (highest first)
        allItems.sort((w1, w2) -> {
            int tierComparison = Integer.compare(w2.getTier(), w1.getTier());
            if (tierComparison != 0) {
                return tierComparison;
            }
            return Integer.compare(w2.getRarity(), w1.getRarity());
        });

        Map<String, Object> itemValues = new HashMap<>();
        Map<String, String> namesAndDescriptions = new HashMap<>();
        allItems.forEach(weaponTemplate -> {
            String name = weaponTemplate.getName();
            String description = "Tier: " + weaponTemplate.getTier() + ", Rarity: " + weaponTemplate.getRarity() + ", Category: " + weaponTemplate.getCategory();
            namesAndDescriptions.put(name, description);
        });
        itemValues.put("WeaponTemplates", namesAndDescriptions);
        return itemValues;
    }

    public List<WeaponTemplate> createTwentyFiveNewWeaponTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveWeapons(campaign);
        List<WeaponTemplate> savedWeaponTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            WeaponTemplate weaponTemplate = new WeaponTemplate();
            weaponTemplate.setName(dto.name());
            weaponTemplate.setDescription(dto.description());
            weaponTemplate.setUserId(campaign.getUserId());
            weaponTemplate.setRarity(dto.rarity());
            weaponTemplate.setTier(dto.tier());
            weaponTemplate.setDiscovered(false);
            weaponTemplate.setQuantity(0); // templates always start at 0 quantity
            weaponTemplate.setEquipped(false);
            weaponTemplate.setCampaign(campaign);
            weaponTemplate.setCategory(WeaponCategory.valueOf(dto.category()));
            if (dto.physicalDamage() == 1) {
                weaponTemplate.setPhysicalDamage((int) Math.round((dto.tier() * 2.5 * dto.rarity() * 3)));
            } else {
                weaponTemplate.setPhysicalDamage(0);
            }
            if (dto.magicalDamage() == 1) {
                weaponTemplate.setMagicalDamage((int) Math.round((dto.tier() * 2.5 * dto.rarity() * 3)));
            } else {
                weaponTemplate.setMagicalDamage(0);
            }
            weaponTemplate.setValue(
                    (weaponTemplate.getMagicalDamage() + weaponTemplate.getPhysicalDamage())
                            * weaponTemplate.getTier()
                            * weaponTemplate.getRarity());
            weaponTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedWeaponTemplates.add(weaponTemplate);
        });

        weaponTemplateRepository.saveAll(savedWeaponTemplates);

        log.info(savedWeaponTemplates.size() + " weapons successfully created an persisted");

        return savedWeaponTemplates;
    }

    public List<WeaponTemplate> getAllWeaponTemplatesForACampaignAndUser(String userId, Long campaignId) {
        return weaponTemplateRepository.findAllByUserIdAndCampaign_Id(userId, campaignId);
    }

    public WeaponTemplate getRandomWeaponTemplate(Long campaignId, String userId) {
        return weaponTemplateRepository.findRandomByCampaignIdAndUserId(campaignId, userId);
    }

    public WeaponTemplate saveWeapon(WeaponTemplate weapon) {
        return weaponTemplateRepository.save(weapon);
    }

    private WeaponInstance instanceFromWeaponTemplate(WeaponTemplate template, Inventory inventoryItBelongsTo) {
        WeaponInstance instance = new WeaponInstance();
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

    public List<WeaponInstance> instancesFromWeaponTemplates(List<WeaponTemplate> templates, Inventory inventoryItBelongsTo) {
        List<WeaponInstance> instances = new ArrayList<>();
        templates.forEach(template -> instances.add(instanceFromWeaponTemplate(template, inventoryItBelongsTo)));
        return instances;
    }
}
