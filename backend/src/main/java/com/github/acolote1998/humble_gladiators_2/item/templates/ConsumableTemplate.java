package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.enums.ConsumablesCategory;
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
@Table(name = "consumable_template")
@Slf4j
public class ConsumableTemplate extends AbstractItem {
    private Integer restoreHp;
    private Integer restoreMp;

    @Enumerated(EnumType.STRING)
    private ConsumablesCategory category;

    public static String ObjectStructure(Long campaignId) {
        return String.format("""
                ConsumableTemplate{
                String name
                String description
                Integer rarity (1 - 5)
                Integer tier (1 - 5)
                Long campaign_id (%s)
                Requirement requirement (create a requirement object)
                ConsumablesCategory category (enum)
                Integer restoreHp (1 or 0)
                Integer restoreMp (1 or 0)
                }
                //
                // Combat effect flags:
                //
                //  - restoreHp (1 = enables consumable to restore hp upon use, 0 = consumable won't restore hp upon use)
                //  - restoreMp (1 = enables consumable to restore mp upon use, 0 = consumable won't restore mp upon use)
                //
                // Combat effect flags rules:
                //
                //  - Use 1 to enable, 0 to disable.
                //  - Never send null values
                //  - At least one of these flags must be 1
                //  - Both cannot be 0.""", campaignId.toString());
    }

    public static boolean isValidConsumable(ConsumableTemplate consumable) {
        if (consumable == null) {
            log.warn("ConsumableTemplate is null");
            return false;
        }

        if (consumable.getName() == null || consumable.getName().isBlank()) {
            log.warn("{} has invalid name", consumable);
            return false;
        }

        if (consumable.getDescription() == null || consumable.getDescription().isBlank()) {
            log.warn("{} has invalid description", consumable);
            return false;
        }

        if (consumable.getRarity() == null || consumable.getRarity() < 1 || consumable.getRarity() > 5) {
            log.warn("{} has invalid rarity (expected 1–5)", consumable);
            return false;
        }

        if (consumable.getTier() == null || consumable.getTier() < 1 || consumable.getTier() > 5) {
            log.warn("{} has invalid tier (expected 1-5)", consumable);
            return false;
        }

        if (consumable.getValue() == null || consumable.getValue() < 0) {
            log.warn("{} has invalid value", consumable);
            return false;
        }

        if (consumable.getQuantity() == null || consumable.getQuantity() < 0 || consumable.getQuantity() > 1) {
            log.warn("{} has invalid quantity", consumable);
            return false;
        }

        if (consumable.getUserId() == null || consumable.getUserId().isBlank()) {
            log.warn("{} has invalid userId", consumable);
            return false;
        }

        if (consumable.getCampaign() == null) {
            log.warn("{} has no campaign assigned", consumable);
            return false;
        }

        if (consumable.getRequirement() == null) {
            log.warn("{} has no requirement assigned", consumable);
            return false;
        }

        if (consumable.getRestoreHp() == null || consumable.getRestoreHp() < 0) {
            log.warn("{} has invalid restore HP", consumable);
            return false;
        }
        if (consumable.getRestoreMp() == null || consumable.getRestoreMp() < 0) {
            log.warn("{} has invalid restore MP", consumable);
            return false;
        }
        if (consumable.getRestoreHp() == 0 && consumable.getRestoreMp() == 0) {
            log.warn("{} has 0 in both restore HP and restore MP", consumable);
            return false;
        }
        return true;
    }

    public static boolean areValidConsumables(List<ConsumableTemplate> consumables, Integer expectedAmount) {
        if (consumables.size() != expectedAmount) {
            return false;
        }
        for (ConsumableTemplate consumable : consumables) {
            if (!ConsumableTemplate.isValidConsumable(consumable)) {
                return false;
            }
        }
        return true;
    }
}
