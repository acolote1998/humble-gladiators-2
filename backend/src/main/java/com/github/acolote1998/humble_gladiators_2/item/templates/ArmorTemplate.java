package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory;
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
                Requirement requirement (create a requirement object)
                ArmorCategory category (enum)
                Integer physicalDefense (1 or 0)
                Integer magicalDefense (1 or 0)
                }
                //
                // Combat effect flags:
                //
                //  - physicalDefense (1 = enables armors to provide physical armor, 0 = armors won't provide physical armor)
                //  - magicalDefense (1 = enables armors to provide magical armor, 0 = armors won't provide magical armor)
                //
                // Combat effect flags rules:
                //
                //  - Use 1 to enable, 0 to disable.
                //  - Never send null values
                //  - At least one of these flags must be 1
                //  - Both cannot be 0.""", campaignId.toString());
    }

    public static boolean isValidArmor(ArmorTemplate armor) {
        if (armor == null) {
            log.warn("ArmorTemplate is null");
            return false;
        }

        if (armor.getName() == null || armor.getName().isBlank()) {
            log.warn("{} has invalid name", armor);
            return false;
        }

        if (armor.getDescription() == null || armor.getDescription().isBlank()) {
            log.warn("{} has invalid description", armor);
            return false;
        }

        if (armor.getRarity() == null || armor.getRarity() < 1 || armor.getRarity() > 5) {
            log.warn("{} has invalid rarity (expected 1–5)", armor);
            return false;
        }

        if (armor.getTier() == null || armor.getTier() < 1 || armor.getTier() > 5) {
            log.warn("{} has invalid tier (expected 1-5)", armor);
            return false;
        }

        if (armor.getValue() == null || armor.getValue() < 0) {
            log.warn("{} has invalid value", armor);
            return false;
        }

        if (armor.getQuantity() == null || armor.getQuantity() < 0 || armor.getQuantity() > 1) {
            log.warn("{} has invalid quantity", armor);
            return false;
        }

        if (armor.getUserId() == null || armor.getUserId().isBlank()) {
            log.warn("{} has invalid userId", armor);
            return false;
        }

        if (armor.getCampaign() == null) {
            log.warn("{} has no campaign assigned", armor);
            return false;
        }

        if (armor.getRequirement() == null) {
            log.warn("{} has no requirement assigned", armor);
            return false;
        }

        if (armor.getPhysicalDefense() == null || armor.getPhysicalDefense() < 0) {
            log.warn("{} has invalid physical defense", armor);
            return false;
        }
        if (armor.getMagicalDefense() == null || armor.getMagicalDefense() < 0) {
            log.warn("{} has invalid magical defense", armor);
            return false;
        }
        if (armor.getPhysicalDefense() == 0 && armor.getMagicalDefense() == 0) {
            log.warn("{} has 0 in both physical and magical defense", armor);
            return false;
        }
        return true;
    }

    public static boolean areValidArmors(List<ArmorTemplate> armors, Integer expectedAmount) {
        if (armors.size() != expectedAmount) {
            return false;
        }
        for (ArmorTemplate armor : armors) {
            if (!ArmorTemplate.isValidArmor(armor)) {
                return false;
            }
        }
        return true;
    }
}
