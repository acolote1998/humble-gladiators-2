package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.enums.SpellCategory;
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
@Table(name = "spell_template")
@Slf4j
public class SpellTemplate extends AbstractItem {
    private Integer physicalDamage;
    private Integer magicalDamage;
    private Integer restoreHp;
    private Integer mpCost;

    @Enumerated(EnumType.STRING)
    private SpellCategory category;

    public static String ObjectStructure(Long campaignId) {
        return String.format("""
                SpellTemplate{
                String name
                String description
                Integer rarity (1 - 5)
                Integer tier (1 - 5)
                Long campaign_id (%s)
                SpellCategory category (enum)
                Integer physicalDamage (1 or 0)
                Integer magicalDamage (1 or 0)
                Integer restoreHp (1 or 0)
                }
                //
                // ⚠️ CRITICAL: Combat effect flags validation rules ⚠️
                //
                //  - physicalDamage (1 = enables spell to deal physical damage, 0 = spell won't deal physical damage)
                //  - magicalDamage (1 = enables spell to deal magical damage, 0 = spell won't deal magical damage)
                //  - restoreHp (1 = enables spell to restore Hp, 0 = spell won't be able to restore Hp)
                //
                // ⚠️ NON-NEGOTIABLE RULES (validation will fail if violated):
                //
                //  1. At least ONE of these flags MUST be set to 1 (physicalDamage, magicalDamage, or restoreHp).
                //  2. All three flags CANNOT be 0. If all are 0, validation will fail.
                //  3. Use 1 to enable, 0 to disable. Never send null values.
                //  4. Healing spells (restoreHp = 1) must have physicalDamage = 0 and magicalDamage = 0.
                //  5. Damage spells (physicalDamage = 1 or magicalDamage = 1) must have restoreHp = 0.
                //
                // Valid examples:
                //  - {physicalDamage: 1, magicalDamage: 0, restoreHp: 0} ✅
                //  - {physicalDamage: 0, magicalDamage: 1, restoreHp: 0} ✅
                //  - {physicalDamage: 0, magicalDamage: 0, restoreHp: 1} ✅
                //  - {physicalDamage: 1, magicalDamage: 1, restoreHp: 0} ✅
                //  - {physicalDamage: 0, magicalDamage: 0, restoreHp: 0} ❌ INVALID (all flags are 0)
                //  - {physicalDamage: 1, magicalDamage: 0, restoreHp: 1} ❌ INVALID (healing cannot deal damage)
                //
                """, campaignId.toString());
    }

    public static boolean isValidSpell(SpellTemplate spell) {
        if (spell == null) {
            log.warn("SpellTemplate is null");
            return false;
        }

        if (spell.getName() == null || spell.getName().isBlank()) {
            log.warn("SpellTemplate validation failed - name is null or blank. Name: '{}'", spell.getName());
            return false;
        }

        if (spell.getDescription() == null || spell.getDescription().isBlank()) {
            log.warn("SpellTemplate validation failed - description is null or blank. Description: '{}'", spell.getDescription());
            return false;
        }

        if (spell.getRarity() == null || spell.getRarity() < 1 || spell.getRarity() > 5) {
            log.warn("SpellTemplate validation failed - rarity is invalid. Expected: 1-5, Got: {}", spell.getRarity());
            return false;
        }

        if (spell.getTier() == null || spell.getTier() < 1 || spell.getTier() > 5) {
            log.warn("SpellTemplate validation failed - tier is invalid. Expected: 1-5, Got: {}", spell.getTier());
            return false;
        }

        if (spell.getValue() == null || spell.getValue() < 0) {
            log.warn("SpellTemplate validation failed - value is invalid. Expected: >= 0, Got: {}", spell.getValue());
            return false;
        }

        if (spell.getQuantity() == null || spell.getQuantity() < 0 || spell.getQuantity() > 1) {
            log.warn("SpellTemplate validation failed - quantity is invalid. Expected: 0-1, Got: {}", spell.getQuantity());
            return false;
        }

        if (spell.getUserId() == null || spell.getUserId().isBlank()) {
            log.warn("SpellTemplate validation failed - userId is null or blank. UserId: '{}'", spell.getUserId());
            return false;
        }

        if (spell.getCampaign() == null) {
            log.warn("SpellTemplate validation failed - campaign is null");
            return false;
        }

        if (spell.getPhysicalDamage() == null || spell.getPhysicalDamage() < 0) {
            log.warn("SpellTemplate validation failed - physicalDamage is invalid. Expected: >= 0, Got: {}", spell.getPhysicalDamage());
            return false;
        }
        if (spell.getMagicalDamage() == null || spell.getMagicalDamage() < 0) {
            log.warn("SpellTemplate validation failed - magicalDamage is invalid. Expected: >= 0, Got: {}", spell.getMagicalDamage());
            return false;
        }
        if (spell.getRestoreHp() == null || spell.getRestoreHp() < 0) {
            log.warn("SpellTemplate validation failed - restoreHp is invalid. Expected: >= 0, Got: {}", spell.getRestoreHp());
            return false;
        }
        if (spell.getPhysicalDamage() == 0 && spell.getMagicalDamage() == 0 && spell.getRestoreHp() == 0) {
            log.warn("SpellTemplate validation failed - all combat effects are 0. PhysicalDamage: {}, MagicalDamage: {}, RestoreHp: {}", spell.getPhysicalDamage(), spell.getMagicalDamage(), spell.getRestoreHp());
            return false;
        }
        // Healing spells cannot deal damage
        if (spell.getRestoreHp() > 0 && (spell.getPhysicalDamage() > 0 || spell.getMagicalDamage() > 0)) {
            log.warn("SpellTemplate validation failed - healing spell also deals damage. RestoreHp: {}, PhysicalDamage: {}, MagicalDamage: {}", spell.getRestoreHp(), spell.getPhysicalDamage(), spell.getMagicalDamage());
            return false;
        }
        return true;
    }

    public static boolean areValidSpells(List<SpellTemplate> spells, Integer expectedAmount) {
        if (spells.size() != expectedAmount) {
            log.warn("SpellTemplate batch validation failed - incorrect list size. Expected: {}, Got: {}", expectedAmount, spells.size());
            return false;
        }
        
        List<String> failedItems = new ArrayList<>();
        for (SpellTemplate spell : spells) {
            if (!SpellTemplate.isValidSpell(spell)) {
                String itemName = spell != null && spell.getName() != null ? spell.getName() : "null";
                failedItems.add(itemName);
            }
        }
        
        if (!failedItems.isEmpty()) {
            log.warn("SpellTemplate batch validation failed - {} out of {} items failed validation. Failed items: {}", 
                    failedItems.size(), expectedAmount, String.join(", ", failedItems));
            return false;
        }
        
        return true;
    }
}
