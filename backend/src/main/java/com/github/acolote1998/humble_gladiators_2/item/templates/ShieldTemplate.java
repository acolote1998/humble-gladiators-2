package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.enums.ShieldCategory;
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
@Table(name = "shield_template")
@Slf4j
public class ShieldTemplate extends AbstractItem {
    private Integer physicalDefense;
    private Integer magicalDefense;

    @Enumerated(EnumType.STRING)
    private ShieldCategory category;

    public static String ObjectStructure(Long campaignId) {
        return String.format(
                """
                        ShieldTemplate{
                        String name
                        String description
                        Integer rarity (1 - 5)
                        Integer tier (1 - 5)
                        Long campaign_id (%s)
                        ShieldCategory category (enum)
                        Integer physicalDefense (1 or 0)
                        Integer magicalDefense (1 or 0)
                        }
                        //
                        // ⚠️ CRITICAL: Combat effect flags validation rules ⚠️
                        //
                        //  - physicalDefense (1 = enables shield to provide physical armor, 0 = shield won't provide physical armor)
                        //  - magicalDefense (1 = enables shield to have magical armor, 0 = shield won't provide magical armor)
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

    public static boolean isValidShield(ShieldTemplate shield) {
        if (shield == null) {
            log.warn("ShieldTemplate is null");
            return false;
        }

        if (shield.getName() == null || shield.getName().isBlank()) {
            log.warn("ShieldTemplate validation failed - name is null or blank. Name: '{}'", shield.getName());
            return false;
        }

        if (shield.getDescription() == null || shield.getDescription().isBlank()) {
            log.warn("ShieldTemplate validation failed - description is null or blank. Description: '{}'", shield.getDescription());
            return false;
        }

        if (shield.getRarity() == null || shield.getRarity() < 1 || shield.getRarity() > 5) {
            log.warn("ShieldTemplate validation failed - rarity is invalid. Expected: 1-5, Got: {}", shield.getRarity());
            return false;
        }

        if (shield.getTier() == null || shield.getTier() < 1 || shield.getTier() > 5) {
            log.warn("ShieldTemplate validation failed - tier is invalid. Expected: 1-5, Got: {}", shield.getTier());
            return false;
        }

        if (shield.getValue() == null || shield.getValue() < 0) {
            log.warn("ShieldTemplate validation failed - value is invalid. Expected: >= 0, Got: {}", shield.getValue());
            return false;
        }

        if (shield.getQuantity() == null || shield.getQuantity() < 0 || shield.getQuantity() > 1) {
            log.warn("ShieldTemplate validation failed - quantity is invalid. Expected: 0-1, Got: {}", shield.getQuantity());
            return false;
        }

        if (shield.getUserId() == null || shield.getUserId().isBlank()) {
            log.warn("ShieldTemplate validation failed - userId is null or blank. UserId: '{}'", shield.getUserId());
            return false;
        }

        if (shield.getCampaign() == null) {
            log.warn("ShieldTemplate validation failed - campaign is null");
            return false;
        }

        if (shield.getPhysicalDefense() == null || shield.getPhysicalDefense() < 0) {
            log.warn("ShieldTemplate validation failed - physicalDefense is invalid. Expected: >= 0, Got: {}", shield.getPhysicalDefense());
            return false;
        }
        if (shield.getMagicalDefense() == null || shield.getMagicalDefense() < 0) {
            log.warn("ShieldTemplate validation failed - magicalDefense is invalid. Expected: >= 0, Got: {}", shield.getMagicalDefense());
            return false;
        }
        if (shield.getPhysicalDefense() == 0 && shield.getMagicalDefense() == 0) {
            log.warn("ShieldTemplate validation failed - both physicalDefense and magicalDefense are 0. PhysicalDefense: {}, MagicalDefense: {}", shield.getPhysicalDefense(), shield.getMagicalDefense());
            return false;
        }
        return true;
    }

    public static boolean areValidShields(List<ShieldTemplate> shields, Integer expectedAmount) {
        if (shields.size() != expectedAmount) {
            log.warn("ShieldTemplate batch validation failed - incorrect list size. Expected: {}, Got: {}", expectedAmount, shields.size());
            return false;
        }
        
        List<String> failedItems = new ArrayList<>();
        for (ShieldTemplate shield : shields) {
            if (!ShieldTemplate.isValidShield(shield)) {
                String itemName = shield != null && shield.getName() != null ? shield.getName() : "null";
                failedItems.add(itemName);
            }
        }
        
        if (!failedItems.isEmpty()) {
            log.warn("ShieldTemplate batch validation failed - {} out of {} items failed validation. Failed items: {}", 
                    failedItems.size(), expectedAmount, String.join(", ", failedItems));
            return false;
        }
        
        return true;
    }
}
