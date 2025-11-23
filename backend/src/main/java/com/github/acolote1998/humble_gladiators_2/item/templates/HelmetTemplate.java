package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.enums.HelmetCategory;
import com.github.acolote1998.humble_gladiators_2.item.model.AbstractItem;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "helmet_template")
@Slf4j
public class HelmetTemplate extends AbstractItem {
    private Integer physicalDefense;
    private Integer magicalDefense;

    @Enumerated(EnumType.STRING)
    private HelmetCategory category;

    public static String ObjectStructure(Long campaignId) {
        return String.format(
                """
                        HelmetTemplate{
                        String name
                        String description
                        Integer rarity (1 - 5)
                        Integer tier (1 - 5)
                        Long campaign_id (%s)
                        HelmetCategory category (enum)
                        Integer physicalDefense (1 or 0)
                        Integer magicalDefense (1 or 0)
                        }
                        //
                        // ⚠️ CRITICAL: Combat effect flags validation rules ⚠️
                        //
                        //  - physicalDefense (1 = enables helmet to provide physical armor, 0 = helmet won't provide physical armor)
                        //  - magicalDefense (1 = enables helmet to provide magical armor, 0 = helmet won't provide magical armor)
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
                        """,
                campaignId.toString());
    }

    public static boolean isValidHelmet(HelmetTemplate helmet) {
        if (helmet == null) {
            log.warn("HelmetTemplate is null");
            return false;
        }

        if (helmet.getName() == null || helmet.getName().isBlank()) {
            log.warn("HelmetTemplate validation failed - name is null or blank. Name: '{}'", helmet.getName());
            return false;
        }

        if (helmet.getDescription() == null || helmet.getDescription().isBlank()) {
            log.warn("HelmetTemplate validation failed - description is null or blank. Description: '{}'", helmet.getDescription());
            return false;
        }

        if (helmet.getRarity() == null || helmet.getRarity() < 1 || helmet.getRarity() > 5) {
            log.warn("HelmetTemplate validation failed - rarity is invalid. Expected: 1-5, Got: {}", helmet.getRarity());
            return false;
        }

        if (helmet.getTier() == null || helmet.getTier() < 1 || helmet.getTier() > 5) {
            log.warn("HelmetTemplate validation failed - tier is invalid. Expected: 1-5, Got: {}", helmet.getTier());
            return false;
        }

        if (helmet.getValue() == null || helmet.getValue() < 0) {
            log.warn("HelmetTemplate validation failed - value is invalid. Expected: >= 0, Got: {}", helmet.getValue());
            return false;
        }

        if (helmet.getQuantity() == null || helmet.getQuantity() < 0 || helmet.getQuantity() > 1) {
            log.warn("HelmetTemplate validation failed - quantity is invalid. Expected: 0-1, Got: {}", helmet.getQuantity());
            return false;
        }

        if (helmet.getUserId() == null || helmet.getUserId().isBlank()) {
            log.warn("HelmetTemplate validation failed - userId is null or blank. UserId: '{}'", helmet.getUserId());
            return false;
        }

        if (helmet.getCampaign() == null) {
            log.warn("HelmetTemplate validation failed - campaign is null");
            return false;
        }

        if (helmet.getPhysicalDefense() == null || helmet.getPhysicalDefense() < 0) {
            log.warn("HelmetTemplate validation failed - physicalDefense is invalid. Expected: >= 0, Got: {}", helmet.getPhysicalDefense());
            return false;
        }
        if (helmet.getMagicalDefense() == null || helmet.getMagicalDefense() < 0) {
            log.warn("HelmetTemplate validation failed - magicalDefense is invalid. Expected: >= 0, Got: {}", helmet.getMagicalDefense());
            return false;
        }
        if (helmet.getPhysicalDefense() == 0 && helmet.getMagicalDefense() == 0) {
            log.warn("HelmetTemplate validation failed - both physicalDefense and magicalDefense are 0. PhysicalDefense: {}, MagicalDefense: {}", helmet.getPhysicalDefense(), helmet.getMagicalDefense());
            return false;
        }
        return true;
    }

    public static boolean areValidHelmets(List<HelmetTemplate> helmets, Integer expectedAmount) {
        if (helmets.size() != expectedAmount) {
            log.warn("HelmetTemplate batch validation failed - incorrect list size. Expected: {}, Got: {}", expectedAmount, helmets.size());
            return false;
        }
        
        List<String> failedItems = new ArrayList<>();
        for (HelmetTemplate helmet : helmets) {
            if (!HelmetTemplate.isValidHelmet(helmet)) {
                String itemName = helmet != null && helmet.getName() != null ? helmet.getName() : "null";
                failedItems.add(itemName);
            }
        }
        
        if (!failedItems.isEmpty()) {
            log.warn("HelmetTemplate batch validation failed - {} out of {} items failed validation. Failed items: {}", 
                    failedItems.size(), expectedAmount, String.join(", ", failedItems));
            return false;
        }
        
        return true;
    }
}
