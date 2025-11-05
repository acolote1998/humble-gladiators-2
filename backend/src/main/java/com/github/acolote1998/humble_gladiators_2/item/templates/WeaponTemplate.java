package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.item.enums.WeaponCategory;
import com.github.acolote1998.humble_gladiators_2.item.model.AbstractItem;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "weapon_template")
@Slf4j
public class WeaponTemplate extends AbstractItem {
    private Integer physicalDamage;
    private Integer magicalDamage;

    @Enumerated(EnumType.STRING)
    private WeaponCategory category;

    public static String ObjectStructure(Long campaignId) {
        return String.format("""
                WeaponTemplate{
                String name
                String description
                Integer rarity (1 - 5)
                Integer tier (1 - 5)
                Long campaign_id (%s)
                Requirement requirement (create a requirement object)
                WeaponCategory category (enum)
                Integer physicalDamage (1 or 0)
                Integer magicalDamage (1 or 0)
                }
                //
                // Combat effect flags:
                //
                //  - physicalDamage (1 = enables weapon to deal physical damage, 0 = weapon won't deal physical damage)
                //  - magicalDamage (1 = enables weapon to deal magical damage, 0 = weapon won't deal magical damage)
                //
                // Combat effect flags rules:
                //
                //  - Use 1 to enable, 0 to disable.
                //  - Never send null values
                //  - At least one of these flags must be 1
                //  - Both cannot be 0.
                """, campaignId.toString());
    }

    public static boolean isValidWeapon(WeaponTemplate weapon) {
        if (weapon == null) {
            log.warn("WeaponTemplate is null");
            return false;
        }

        if (weapon.getName() == null || weapon.getName().isBlank()) {
            log.warn("WeaponTemplate validation failed - name is null or blank. Name: '{}'", weapon.getName());
            return false;
        }

        if (weapon.getDescription() == null || weapon.getDescription().isBlank()) {
            log.warn("WeaponTemplate validation failed - description is null or blank. Description: '{}'", weapon.getDescription());
            return false;
        }

        if (weapon.getRarity() == null || weapon.getRarity() < 1 || weapon.getRarity() > 5) {
            log.warn("WeaponTemplate validation failed - rarity is invalid. Expected: 1-5, Got: {}", weapon.getRarity());
            return false;
        }

        if (weapon.getTier() == null || weapon.getTier() < 1 || weapon.getTier() > 5) {
            log.warn("WeaponTemplate validation failed - tier is invalid. Expected: 1-5, Got: {}", weapon.getTier());
            return false;
        }

        if (weapon.getValue() == null || weapon.getValue() < 0) {
            log.warn("WeaponTemplate validation failed - value is invalid. Expected: >= 0, Got: {}", weapon.getValue());
            return false;
        }

        if (weapon.getQuantity() == null || weapon.getQuantity() < 0 || weapon.getQuantity() > 1) {
            log.warn("WeaponTemplate validation failed - quantity is invalid. Expected: 0-1, Got: {}", weapon.getQuantity());
            return false;
        }

        if (weapon.getUserId() == null || weapon.getUserId().isBlank()) {
            log.warn("WeaponTemplate validation failed - userId is null or blank. UserId: '{}'", weapon.getUserId());
            return false;
        }

        if (weapon.getCampaign() == null) {
            log.warn("WeaponTemplate validation failed - campaign is null");
            return false;
        }

        if (!Requirement.isValidRequirement(weapon.getRequirement())) {
            log.warn("WeaponTemplate validation failed - requirement is invalid. Requirement: {}", weapon.getRequirement());
            return false;
        }

        if (weapon.getPhysicalDamage() == null || weapon.getPhysicalDamage() < 0) {
            log.warn("WeaponTemplate validation failed - physicalDamage is invalid. Expected: >= 0, Got: {}", weapon.getPhysicalDamage());
            return false;
        }
        if (weapon.getMagicalDamage() == null || weapon.getMagicalDamage() < 0) {
            log.warn("WeaponTemplate validation failed - magicalDamage is invalid. Expected: >= 0, Got: {}", weapon.getMagicalDamage());
            return false;
        }
        if (weapon.getPhysicalDamage() == 0 && weapon.getMagicalDamage() == 0) {
            log.warn("WeaponTemplate validation failed - both physicalDamage and magicalDamage are 0. PhysicalDamage: {}, MagicalDamage: {}", weapon.getPhysicalDamage(), weapon.getMagicalDamage());
            return false;
        }
        return true;
    }

    public static boolean areValidWeapons(List<WeaponTemplate> weapons, Integer expectedAmount) {
        if (weapons.size() != expectedAmount) {
            return false;
        }
        for (WeaponTemplate weapon : weapons) {
            if (!WeaponTemplate.isValidWeapon(weapon)) {
                return false;
            }
        }
        return true;
    }
}
