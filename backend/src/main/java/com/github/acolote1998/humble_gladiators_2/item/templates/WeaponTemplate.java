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
            log.warn("{} has invalid name", weapon);
            return false;
        }

        if (weapon.getDescription() == null || weapon.getDescription().isBlank()) {
            log.warn("{} has invalid description", weapon);
            return false;
        }

        if (weapon.getRarity() == null || weapon.getRarity() < 1 || weapon.getRarity() > 5) {
            log.warn("{} has invalid rarity (expected 1–5)", weapon);
            return false;
        }

        if (weapon.getTier() == null || weapon.getTier() < 1 || weapon.getTier() > 5) {
            log.warn("{} has invalid tier (expected 1-5)", weapon);
            return false;
        }

        if (weapon.getValue() == null || weapon.getValue() < 0) {
            log.warn("{} has invalid value", weapon);
            return false;
        }

        if (weapon.getQuantity() == null || weapon.getQuantity() < 0 || weapon.getQuantity() > 1) {
            log.warn("{} has invalid quantity", weapon);
            return false;
        }

        if (weapon.getUserId() == null || weapon.getUserId().isBlank()) {
            log.warn("{} has invalid userId", weapon);
            return false;
        }

        if (weapon.getCampaign() == null) {
            log.warn("{} has no campaign assigned", weapon);
            return false;
        }

        if (!Requirement.isValidRequirement(weapon.getRequirement())) {
            log.warn("{} has invalid requirement", weapon);
            return false;
        }

        if (weapon.getPhysicalDamage() == null || weapon.getPhysicalDamage() < 0) {
            log.warn("{} has invalid physical damage", weapon);
            return false;
        }
        if (weapon.getMagicalDamage() == null || weapon.getMagicalDamage() < 0) {
            log.warn("{} has invalid magical damage", weapon);
            return false;
        }
        if (weapon.getPhysicalDamage() == 0 && weapon.getMagicalDamage() == 0) {
            log.warn("{} has 0 in both physical and magical damage", weapon);
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
