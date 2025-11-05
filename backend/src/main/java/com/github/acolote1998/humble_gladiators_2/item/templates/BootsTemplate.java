package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.item.enums.BootsCategory;
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
@Table(name = "boots_template")
@Slf4j
public class BootsTemplate extends AbstractItem {
    private Integer physicalDefense;
    private Integer magicalDefense;

    @Enumerated(EnumType.STRING)
    private BootsCategory category;

    public static String ObjectStructure(Long campaignId) {
        return String.format("""
                BootsTemplate{
                String name
                String description
                Integer rarity (1 - 5)
                Integer tier (1 - 5)
                Long campaign_id (%s)
                Requirement requirement (create a requirement object)
                BootsCategory category (enum)
                Integer physicalDefense (1 or 0)
                Integer magicalDefense (1 or 0)
                }
                //
                // Combat effect flags:
                //
                //  - physicalDefense (1 = enables boots to provide physical armor, 0 = boots won't provide physical armor)
                //  - magicalDefense (1 = enables boots to provide magical armor, 0 = boots won't provide magical armor)
                //
                // Combat effect flags rules:
                //
                //  - Use 1 to enable, 0 to disable.
                //  - Never send null values
                //  - At least one of these flags must be 1
                //  - Both cannot be 0.
                """, campaignId.toString());
    }

    public static boolean isValidBoot(BootsTemplate boot) {
        if (boot == null) {
            log.warn("BootsTemplate is null");
            return false;
        }

        if (boot.getName() == null || boot.getName().isBlank()) {
            log.warn("BootsTemplate validation failed - name is null or blank. Name: '{}'", boot.getName());
            return false;
        }

        if (boot.getDescription() == null || boot.getDescription().isBlank()) {
            log.warn("BootsTemplate validation failed - description is null or blank. Description: '{}'", boot.getDescription());
            return false;
        }

        if (boot.getRarity() == null || boot.getRarity() < 1 || boot.getRarity() > 5) {
            log.warn("BootsTemplate validation failed - rarity is invalid. Expected: 1-5, Got: {}", boot.getRarity());
            return false;
        }

        if (boot.getTier() == null || boot.getTier() < 1 || boot.getTier() > 5) {
            log.warn("BootsTemplate validation failed - tier is invalid. Expected: 1-5, Got: {}", boot.getTier());
            return false;
        }

        if (boot.getValue() == null || boot.getValue() < 0) {
            log.warn("BootsTemplate validation failed - value is invalid. Expected: >= 0, Got: {}", boot.getValue());
            return false;
        }

        if (boot.getQuantity() == null || boot.getQuantity() < 0 || boot.getQuantity() > 1) {
            log.warn("BootsTemplate validation failed - quantity is invalid. Expected: 0-1, Got: {}", boot.getQuantity());
            return false;
        }

        if (boot.getUserId() == null || boot.getUserId().isBlank()) {
            log.warn("BootsTemplate validation failed - userId is null or blank. UserId: '{}'", boot.getUserId());
            return false;
        }

        if (boot.getCampaign() == null) {
            log.warn("BootsTemplate validation failed - campaign is null");
            return false;
        }

        if (!Requirement.isValidRequirement(boot.getRequirement())) {
            log.warn("BootsTemplate validation failed - requirement is invalid. Requirement: {}", boot.getRequirement());
            return false;
        }

        if (boot.getPhysicalDefense() == null || boot.getPhysicalDefense() < 0) {
            log.warn("BootsTemplate validation failed - physicalDefense is invalid. Expected: >= 0, Got: {}", boot.getPhysicalDefense());
            return false;
        }
        if (boot.getMagicalDefense() == null || boot.getMagicalDefense() < 0) {
            log.warn("BootsTemplate validation failed - magicalDefense is invalid. Expected: >= 0, Got: {}", boot.getMagicalDefense());
            return false;
        }
        if (boot.getPhysicalDefense() == 0 && boot.getMagicalDefense() == 0) {
            log.warn("BootsTemplate validation failed - both physicalDefense and magicalDefense are 0. PhysicalDefense: {}, MagicalDefense: {}", boot.getPhysicalDefense(), boot.getMagicalDefense());
            return false;
        }
        return true;
    }

    public static boolean areValidBoots(List<BootsTemplate> boots, Integer expectedAmount) {
        if (boots.size() != expectedAmount) {
            return false;
        }
        for (BootsTemplate boot : boots) {
            if (!BootsTemplate.isValidBoot(boot)) {
                return false;
            }
        }
        return true;
    }
}
