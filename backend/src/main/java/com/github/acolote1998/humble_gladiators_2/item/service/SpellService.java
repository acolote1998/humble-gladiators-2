package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.item.enums.SpellCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.SpellInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.SpellTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SpellService {
    GeminiService geminiService;
    SpellTemplateRepository spellTemplateRepository;

    public SpellService(GeminiService geminiService, SpellTemplateRepository spellTemplateRepository) {
        this.geminiService = geminiService;
        this.spellTemplateRepository = spellTemplateRepository;
    }

    public Map<String, Object> getShortAIGeneratedReport() {
        List<SpellTemplate> allItems = spellTemplateRepository.findAll();
        // Sort by Tier (highest first) then Rarity (highest first)
        allItems.sort((s1, s2) -> {
            int tierComparison = Integer.compare(s2.getTier(), s1.getTier());
            if (tierComparison != 0) {
                return tierComparison;
            }
            return Integer.compare(s2.getRarity(), s1.getRarity());
        });

        Map<String, Object> itemValues = new HashMap<>();
        Map<String, String> namesAndDescriptions = new HashMap<>();
        allItems.forEach(spellTemplate -> {
            String name = spellTemplate.getName();
            String description = "Tier: " + spellTemplate.getTier() + ", Rarity: " + spellTemplate.getRarity() + ", Category: " + spellTemplate.getCategory();
            namesAndDescriptions.put(name, description);
        });
        itemValues.put("SpellTemplates", namesAndDescriptions);
        return itemValues;
    }

    public List<SpellTemplate> createTwentyFiveNewSpellTemplates(Campaign campaign) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateTwentyFiveSpells(campaign);
        List<SpellTemplate> savedSpellTemplates = new ArrayList<>();

        generatedDtos.forEach(dto -> {
            SpellTemplate spellTemplate = new SpellTemplate();
            spellTemplate.setName(dto.name());
            spellTemplate.setDescription(dto.description());
            spellTemplate.setUserId(campaign.getUserId());
            spellTemplate.setRarity(dto.rarity());
            spellTemplate.setTier(dto.tier());
            spellTemplate.setDiscovered(false);
            spellTemplate.setQuantity(0); // templates always start at 0 quantity
            spellTemplate.setEquipped(false);
            spellTemplate.setCampaign(campaign);
            spellTemplate.setCategory(SpellCategory.valueOf(dto.category()));
            if (dto.physicalDamage() == 1) {
                spellTemplate.setPhysicalDamage((int) Math.round((dto.tier() * 2.5 * dto.rarity() * 3)));
            } else {
                spellTemplate.setPhysicalDamage(0);
            }
            if (dto.magicalDamage() == 1) {
                spellTemplate.setMagicalDamage((int) Math.round((dto.tier() * 2.5 * dto.rarity() * 3)));
            } else {
                spellTemplate.setMagicalDamage(0);
            }
            if (dto.restoreHp() == 1) {
                spellTemplate.setRestoreHp((int) Math.round((dto.tier() * 2.5 * dto.rarity() * 3)));
                spellTemplate.setMagicalDamage(0); //if restoring hp, spell cannot deal dmg, setting on 0 to avoid bugs
                spellTemplate.setPhysicalDamage(0); //if restoring hp, spell cannot deal dmg, setting on 0 to avoid bugs
            } else {
                spellTemplate.setRestoreHp(0);
            }
            spellTemplate.setValue(
                    (spellTemplate.getMagicalDamage() + spellTemplate.getPhysicalDamage() + spellTemplate.getRestoreHp())
                            * spellTemplate.getTier()
                            * spellTemplate.getRarity());
            spellTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedSpellTemplates.add(spellTemplate);
        });

        spellTemplateRepository.saveAll(savedSpellTemplates);

        log.info(savedSpellTemplates.size() + " spells successfully created an persisted");

        return savedSpellTemplates;
    }

    public List<SpellTemplate> getAllSpellTemplatesForACampaignAndUser(String userId, Long campaignId) {
        return spellTemplateRepository.findAllByUserIdAndCampaign_Id(userId, campaignId);
    }

    public SpellTemplate getRandomSpellTemplate(Long campaignId, String userId) {
        return spellTemplateRepository.findRandomByCampaignIdAndUserId(campaignId, userId);
    }

    public SpellTemplate saveSpell(SpellTemplate spell) {
        return spellTemplateRepository.save(spell);
    }

    private SpellInstance instanceFromSpellTemplate(SpellTemplate template, Inventory inventoryItBelongsTo) {
        SpellInstance instance = new SpellInstance();
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

    public List<SpellInstance> instancesFromSpellTemplates(List<SpellTemplate> templates, Inventory inventoryItBelongsTo) {
        List<SpellInstance> instances = new ArrayList<>();
        templates.forEach(template -> instances.add(instanceFromSpellTemplate(template, inventoryItBelongsTo)));
        return instances;
    }
}
