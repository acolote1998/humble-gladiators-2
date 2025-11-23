package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory;
import com.github.acolote1998.humble_gladiators_2.item.model.AbstractItem;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "armor_template")
@Slf4j
public class ArmorTemplate extends AbstractItem {
    private Integer physicalDefense;
    private Integer magicalDefense;

    @Enumerated(EnumType.STRING)
    private ArmorCategory category;

    public static String ObjectStructure(Long campaignId) {
        return String.format("""
                ArmorTemplate{
                String name
                String description
                Integer rarity (1 - 5)
                Integer tier (1 - 5)
                Long campaign_id (%s)
                ArmorCategory category (enum)
                Integer physicalDefense (1 or 0)
                Integer magicalDefense (1 or 0)
                }
                //
                // ⚠️ CRITICAL: Combat effect flags validation rules ⚠️
                //
                //  - physicalDefense (1 = enables armors to provide physical armor, 0 = armors won't provide physical armor)
                //  - magicalDefense (1 = enables armors to provide magical armor, 0 = armors won't provide magical armor)
                //
                // ⚠️ NON-NEGOTIABLE RULES (validation will fail if violated):
                //
                //  1. At least ONE of these flags MUST be set to 1 (physicalDefense or magicalDefense).
                //  2. Both flags CANNOT be 0. If both are 0, validation will fail.
                //  3. Use 1 to enable, 0 to disable. Never send null values.
                //
                // Valid examples:
                //  - {physicalDefense: 1, magicalDefense: 0} ✅
                //  - {physicalDefense: 0, magicalDefense: 1} ✅
                //  - {physicalDefense: 1, magicalDefense: 1} ✅
                //  - {physicalDefense: 0, magicalDefense: 0} ❌ INVALID (both flags are 0)
                //
                """, campaignId.toString());
    }

    public static boolean isValidArmor(ArmorTemplate armor) {
        if (armor == null) {
            log.warn("ArmorTemplate is null");
            return false;
        }

        if (armor.getName() == null || armor.getName().isBlank()) {
            log.warn("ArmorTemplate validation failed - name is null or blank. Name: '{}'", armor.getName());
            return false;
        }

        if (armor.getDescription() == null || armor.getDescription().isBlank()) {
            log.warn("ArmorTemplate validation failed - description is null or blank. Description: '{}'", armor.getDescription());
            return false;
        }

        if (armor.getRarity() == null || armor.getRarity() < 1 || armor.getRarity() > 5) {
            log.warn("ArmorTemplate validation failed - rarity is invalid. Expected: 1-5, Got: {}", armor.getRarity());
            return false;
        }

        if (armor.getTier() == null || armor.getTier() < 1 || armor.getTier() > 5) {
            log.warn("ArmorTemplate validation failed - tier is invalid. Expected: 1-5, Got: {}", armor.getTier());
            return false;
        }

        if (armor.getValue() == null || armor.getValue() < 0) {
            log.warn("ArmorTemplate validation failed - value is invalid. Expected: >= 0, Got: {}", armor.getValue());
            return false;
        }

        if (armor.getQuantity() == null || armor.getQuantity() < 0 || armor.getQuantity() > 1) {
            log.warn("ArmorTemplate validation failed - quantity is invalid. Expected: 0-1, Got: {}", armor.getQuantity());
            return false;
        }

        if (armor.getUserId() == null || armor.getUserId().isBlank()) {
            log.warn("ArmorTemplate validation failed - userId is null or blank. UserId: '{}'", armor.getUserId());
            return false;
        }

        if (armor.getCampaign() == null) {
            log.warn("ArmorTemplate validation failed - campaign is null");
            return false;
        }

        if (armor.getPhysicalDefense() == null || armor.getPhysicalDefense() < 0) {
            log.warn("ArmorTemplate validation failed - physicalDefense is invalid. Expected: >= 0, Got: {}", armor.getPhysicalDefense());
            return false;
        }
        if (armor.getMagicalDefense() == null || armor.getMagicalDefense() < 0) {
            log.warn("ArmorTemplate validation failed - magicalDefense is invalid. Expected: >= 0, Got: {}", armor.getMagicalDefense());
            return false;
        }
        if (armor.getPhysicalDefense() == 0 && armor.getMagicalDefense() == 0) {
            log.warn("ArmorTemplate validation failed - both physicalDefense and magicalDefense are 0. PhysicalDefense: {}, MagicalDefense: {}", armor.getPhysicalDefense(), armor.getMagicalDefense());
            return false;
        }
        return true;
    }

    public static boolean areValidArmors(List<ArmorTemplate> armors, Integer expectedAmount) {
        if (armors.size() != expectedAmount) {
            log.warn("ArmorTemplate batch validation failed - incorrect list size. Expected: {}, Got: {}", expectedAmount, armors.size());
            return false;
        }
        
        List<String> failedItems = new ArrayList<>();
        for (ArmorTemplate armor : armors) {
            if (!ArmorTemplate.isValidArmor(armor)) {
                String itemName = armor != null && armor.getName() != null ? armor.getName() : "null";
                failedItems.add(itemName);
            }
        }
        
        if (!failedItems.isEmpty()) {
            log.warn("ArmorTemplate batch validation failed - {} out of {} items failed validation. Failed items: {}", 
                    failedItems.size(), expectedAmount, String.join(", ", failedItems));
            return false;
        }
        
        return true;
    }
}
