package com.github.acolote1998.humble_gladiators_2.item.service;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.config.GameBalanceConfig;
import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidGeminiEnumException;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.util.GeminiEnumParser;
import com.github.acolote1998.humble_gladiators_2.item.enums.WeaponCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.WeaponInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.WeaponTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class WeaponService {
    GeminiService geminiService;
    WeaponTemplateRepository weaponTemplateRepository;
    GameBalanceConfig balanceConfig;

    public WeaponService(GeminiService geminiService, WeaponTemplateRepository weaponTemplateRepository, GameBalanceConfig balanceConfig) {
        this.geminiService = geminiService;
        this.weaponTemplateRepository = weaponTemplateRepository;
        this.balanceConfig = balanceConfig;
    }

    public Map<String, String> getTier5WeaponsContextForCampaignCover(Campaign campaign) {
        List<WeaponTemplate> weapons = weaponTemplateRepository.findAllByTierAndCampaign_Id(5, campaign.getId());

        Map<String, String> context = new HashMap<>();
        for (WeaponTemplate weapon : weapons) {
            if (weapon.getName() != null && !weapon.getName().isBlank()) {
                context.put(
                        weapon.getName(),
                        weapon.getDescription() != null ? weapon.getDescription() : "");
            }
        }

        return context;
    }

    public Map<String, Object> getShortAIGeneratedReport(Long campaignId) {
        List<WeaponTemplate> allItems = weaponTemplateRepository.findAllByCampaign_Id(campaignId);
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
            String description = "Tier: " + weaponTemplate.getTier() + ", Rarity: " + weaponTemplate.getRarity()
                    + ", Category: " + weaponTemplate.getCategory();
            namesAndDescriptions.put(name, description);
        });
        itemValues.put("WeaponTemplates", namesAndDescriptions);
        return itemValues;
    }

    public List<WeaponTemplate> createFiveNewWeaponTemplatesOfTier(Campaign campaign, Integer tier) {
        List<ItemFromGeminiDto> generatedDtos = geminiService.generateFiveWeaponsOfTier(campaign, tier);
        List<WeaponTemplate> savedWeaponTemplates = new ArrayList<>();

        try {
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
                weaponTemplate.setCategory(GeminiEnumParser.parseEnum(WeaponCategory.class, dto.category(), "WeaponTemplate", dto.name()));
                if (dto.physicalDamage() == 1) {
                    weaponTemplate.setPhysicalDamage((int) Math.round((dto.tier() * dto.rarity() * balanceConfig.getPhysicalDamageMultiplier())));
                } else {
                    weaponTemplate.setPhysicalDamage(0);
                }
                if (dto.magicalDamage() == 1) {
                    weaponTemplate.setMagicalDamage((int) Math.round((dto.tier() * dto.rarity() * balanceConfig.getMagicalDamageMultiplier())));
                } else {
                    weaponTemplate.setMagicalDamage(0);
                }
                weaponTemplate.setValue(
                        (weaponTemplate.getMagicalDamage() + weaponTemplate.getPhysicalDamage())
                                * weaponTemplate.getTier()
                                * weaponTemplate.getRarity());
                savedWeaponTemplates.add(weaponTemplate);
            });
        } catch (InvalidGeminiEnumException ex) {
            log.warn(String.format("Campaign %s - Generated weapons tier %s not valid (enum mismatch) -> Generating again", campaign.getId(), tier), ex);
            return createFiveNewWeaponTemplatesOfTier(campaign, tier);
        }

        if (!WeaponTemplate.areValidWeapons(savedWeaponTemplates, 5)) {
            log.warn(String.format("Campaign %s - Generated weapons tier %s not valid -> Generating again", campaign.getId(), tier));
            return createFiveNewWeaponTemplatesOfTier(campaign, tier);
        }

        weaponTemplateRepository.saveAll(savedWeaponTemplates);

        log.info(savedWeaponTemplates.size() + " weapons tier " + tier + " successfully created an persisted");

        return savedWeaponTemplates;
    }

    public List<WeaponTemplate> getAllWeaponTemplatesForACampaignAndUser(String userId, Long campaignId) {
        return weaponTemplateRepository.findAllByUserIdAndCampaign_Id(userId, campaignId);
    }

    public WeaponTemplate getRandomWeaponTemplateForItemBooster(Long campaignId, String userId, Integer rarity,
                                                                Integer tier) {
        return weaponTemplateRepository.findRandomByCampaignAndRarityAndTier(
                campaignId,
                userId,
                rarity,
                tier);
    }

    public WeaponTemplate saveWeapon(WeaponTemplate weapon) {
        return weaponTemplateRepository.save(weapon);
    }

    public WeaponInstance instanceFromWeaponTemplate(WeaponTemplate template, Inventory inventoryItBelongsTo) {
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
        instance.setValue(template.getValue());
        return instance;
    }

    public List<WeaponInstance> instancesFromWeaponTemplates(List<WeaponTemplate> templates,
                                                             Inventory inventoryItBelongsTo) {
        List<WeaponInstance> instances = new ArrayList<>();
        templates.forEach(template -> instances.add(instanceFromWeaponTemplate(template, inventoryItBelongsTo)));
        return instances;
    }

    public WeaponTemplate getRandomWeaponByTierAndRarityAndCampaignAndUserId(Integer tier, Integer rarity, Long campaignId, String userId) {
        List<WeaponTemplate> weapons = weaponTemplateRepository.findAllByTierAndRarityAndCampaign_IdAndUserId(tier, rarity, campaignId, userId);
        Collections.shuffle(weapons);
        WeaponTemplate weapon = weapons.stream().findFirst().orElseThrow();
        return weapon;
    }
}
