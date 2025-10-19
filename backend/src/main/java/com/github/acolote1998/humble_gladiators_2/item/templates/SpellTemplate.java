package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.item.enums.SpellCategory;
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
                Requirement requirement (create a requirement object)
                SpellCategory category (enum)
                Integer physicalDamage (1 or 0)
                Integer magicalDamage (1 or 0)
                Integer restoreHp (1 or 0)
                }
                //
                // Combat effect flags:
                //
                //  - physicalDamage (1 = enables spell to deal physical damage, 0 = spell won't deal physical damage)
                //  - magicalDamage (1 = enables spell to deal magical damage, 0 = spell won't deal magical damage)
                //  - restoreHp (1 = enables spell to restore Hp, 0 = spell won't be able to restore Hp)
                //
                // Combat effect flags rules:
                //
                //  - Use 1 to enable, 0 to disable.
                //  - Never send null values
                //  - Healing spells (restoreHp = 1) must have physicalDamage = 0 and magicalDamage = 0.
                //  - Damage spells (physicalDamage = 1 or magicalDamage = 1) must have restoreHp = 0.
                //  - At least one of these flags must be 1.
                //  - All three cannot be 0.
                """, campaignId.toString());
    }

    public static boolean isValidSpell(SpellTemplate spell) {
        if (spell == null) {
            log.warn("SpellTemplate is null");
            return false;
        }

        if (spell.getName() == null || spell.getName().isBlank()) {
            log.warn("{} has invalid name", spell);
            return false;
        }

        if (spell.getDescription() == null || spell.getDescription().isBlank()) {
            log.warn("{} has invalid description", spell);
            return false;
        }

        if (spell.getRarity() == null || spell.getRarity() < 1 || spell.getRarity() > 5) {
            log.warn("{} has invalid rarity (expected 1–5)", spell);
            return false;
        }

        if (spell.getTier() == null || spell.getTier() < 1 || spell.getTier() > 5) {
            log.warn("{} has invalid tier (expected 1-5)", spell);
            return false;
        }

        if (spell.getValue() == null || spell.getValue() < 0) {
            log.warn("{} has invalid value", spell);
            return false;
        }

        if (spell.getQuantity() == null || spell.getQuantity() < 0 || spell.getQuantity() > 1) {
            log.warn("{} has invalid quantity", spell);
            return false;
        }

        if (spell.getUserId() == null || spell.getUserId().isBlank()) {
            log.warn("{} has invalid userId", spell);
            return false;
        }

        if (spell.getCampaign() == null) {
            log.warn("{} has no campaign assigned", spell);
            return false;
        }

        if (!Requirement.isValidRequirement(spell.getRequirement())) {
            log.warn("{} has invalid requirement", spell);
            return false;
        }

        if (spell.getPhysicalDamage() == null || spell.getPhysicalDamage() < 0) {
            log.warn("{} has invalid physical damage", spell);
            return false;
        }
        if (spell.getMagicalDamage() == null || spell.getMagicalDamage() < 0) {
            log.warn("{} has invalid magical damage", spell);
            return false;
        }
        if (spell.getRestoreHp() == null || spell.getRestoreHp() < 0) {
            log.warn("{} has invalid restore HP", spell);
            return false;
        }
        if (spell.getPhysicalDamage() == 0 && spell.getMagicalDamage() == 0 && spell.getRestoreHp() == 0) {
            log.warn("{} has 0 in all combat effects (physical damage, magical damage, and restore HP)", spell);
            return false;
        }
        // Healing spells cannot deal damage
        if (spell.getRestoreHp() > 0 && (spell.getPhysicalDamage() > 0 || spell.getMagicalDamage() > 0)) {
            log.warn("{} is a healing spell but also deals damage", spell);
            return false;
        }
        return true;
    }

    public static boolean areValidSpells(List<SpellTemplate> spells, Integer expectedAmount) {
        if (spells.size() != expectedAmount) {
            return false;
        }
        for (SpellTemplate spell : spells) {
            if (!SpellTemplate.isValidSpell(spell)) {
                return false;
            }
        }
        return true;
    }
}
