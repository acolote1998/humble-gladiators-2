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

import java.util.*;

@Service
@Slf4j
public class SpellService {
    GeminiService geminiService;
    SpellTemplateRepository spellTemplateRepository;

    public SpellService(GeminiService geminiService, SpellTemplateRepository spellTemplateRepository) {
        this.geminiService = geminiService;
        this.spellTemplateRepository = spellTemplateRepository;
    }

    public Map<String, String> getTier5SpellsContextForCampaignCover(Campaign campaign) {
        List<SpellTemplate> spells = spellTemplateRepository.findAllByTierAndCampaign_Id(5, campaign.getId());

        Map<String, String> context = new HashMap<>();
        for (SpellTemplate spell : spells) {
            if (spell.getName() != null && !spell.getName().isBlank()) {
                context.put(
                        spell.getName(),
                        spell.getDescription() != null ? spell.getDescription() : "");
            }
        }

        return context;
    }

    public Map<String, Object> getShortAIGeneratedReport(Long campaignId) {
        List<SpellTemplate> allItems = spellTemplateRepository.findAllByCampaign_Id(campaignId);
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
            String description = "Tier: " + spellTemplate.getTier() + ", Rarity: " + spellTemplate.getRarity()
                    + ", Category: " + spellTemplate.getCategory();
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
            spellTemplate.setMpCost(
                    spellTemplate.getTier() *
                            spellTemplate.getRarity() *
                            (spellTemplate.getMagicalDamage() + spellTemplate.getPhysicalDamage() + spellTemplate.getRestoreHp())
            );
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
                spellTemplate.setMagicalDamage(0); // if restoring hp, spell cannot deal dmg, setting on 0 to avoid bugs
                spellTemplate.setPhysicalDamage(0); // if restoring hp, spell cannot deal dmg, setting on 0 to avoid
                // bugs
            } else {
                spellTemplate.setRestoreHp(0);
            }
            spellTemplate.setValue(
                    (spellTemplate.getMagicalDamage() + spellTemplate.getPhysicalDamage()
                            + spellTemplate.getRestoreHp())
                            * spellTemplate.getTier()
                            * spellTemplate.getRarity());
            spellTemplate.setRequirement(RequirementService.mapRequirementFromGeminiItemDto(dto, campaign));
            savedSpellTemplates.add(spellTemplate);
        });

        if (!SpellTemplate.areValidSpells(savedSpellTemplates, 25)) {
            log.warn(String.format("Campaign %s - Generated spells not valid -> Generating again", campaign.getId()));
            return createTwentyFiveNewSpellTemplates(campaign);
        }

        spellTemplateRepository.saveAll(savedSpellTemplates);

        log.info(savedSpellTemplates.size() + " spells successfully created an persisted");

        return savedSpellTemplates;
    }

    public List<SpellTemplate> getAllSpellTemplatesForACampaignAndUser(String userId, Long campaignId) {
        return spellTemplateRepository.findAllByUserIdAndCampaign_Id(userId, campaignId);
    }

    public SpellTemplate getRandomSpellTemplateForItemBooster(Long campaignId, String userId, Integer rarity,
                                                              Integer tier) {
        return spellTemplateRepository.findRandomByCampaignAndRarityAndTier(
                campaignId,
                userId,
                rarity,
                tier);
    }

    public SpellTemplate saveSpell(SpellTemplate spell) {
        return spellTemplateRepository.save(spell);
    }

    public SpellInstance instanceFromSpellTemplate(SpellTemplate template, Inventory inventoryItBelongsTo) {
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

    public List<SpellInstance> instancesFromSpellTemplates(List<SpellTemplate> templates,
                                                           Inventory inventoryItBelongsTo) {
        List<SpellInstance> instances = new ArrayList<>();
        templates.forEach(template -> instances.add(instanceFromSpellTemplate(template, inventoryItBelongsTo)));
        return instances;
    }

    public SpellTemplate getRandomSpellByTierAndRarityAndCampaignAndUserId(Integer tier, Integer rarity, Long campaignId, String userId) {
        List<SpellTemplate> spells = spellTemplateRepository.findAllByTierAndRarityAndCampaign_IdAndUserId(tier, rarity, campaignId, userId);
        Collections.shuffle(spells);
        SpellTemplate spell = spells.stream().findFirst().orElseThrow();
        return spell;
    }
}
