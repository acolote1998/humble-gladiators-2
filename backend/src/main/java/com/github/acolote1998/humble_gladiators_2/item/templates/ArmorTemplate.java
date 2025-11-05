package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory;
import com.github.acolote1998.humble_gladiators_2.item.model.AbstractItem;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

        if (!Requirement.isValidRequirement(armor.getRequirement())) {
            log.warn("ArmorTemplate validation failed - requirement is invalid. Requirement: {}", armor.getRequirement());
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
