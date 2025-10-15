package com.github.acolote1998.humble_gladiators_2.item.templates;

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
            log.warn("{} has invalid name", boot);
            return false;
        }

        if (boot.getDescription() == null || boot.getDescription().isBlank()) {
            log.warn("{} has invalid description", boot);
            return false;
        }

        if (boot.getRarity() == null || boot.getRarity() < 1 || boot.getRarity() > 5) {
            log.warn("{} has invalid rarity (expected 1–5)", boot);
            return false;
        }

        if (boot.getTier() == null || boot.getTier() < 1 || boot.getTier() > 5) {
            log.warn("{} has invalid tier (expected 1-5)", boot);
            return false;
        }

        if (boot.getValue() == null || boot.getValue() < 0) {
            log.warn("{} has invalid value", boot);
            return false;
        }

        if (boot.getQuantity() == null || boot.getQuantity() < 0 || boot.getQuantity() > 1) {
            log.warn("{} has invalid quantity", boot);
            return false;
        }

        if (boot.getUserId() == null || boot.getUserId().isBlank()) {
            log.warn("{} has invalid userId", boot);
            return false;
        }

        if (boot.getCampaign() == null) {
            log.warn("{} has no campaign assigned", boot);
            return false;
        }

        if (boot.getRequirement() == null) {
            log.warn("{} has no requirement assigned", boot);
            return false;
        }

        if (boot.getPhysicalDefense() == null || boot.getPhysicalDefense() < 0) {
            log.warn("{} has invalid physical defense", boot);
            return false;
        }
        if (boot.getMagicalDefense() == null || boot.getMagicalDefense() < 0) {
            log.warn("{} has invalid magical defense", boot);
            return false;
        }
        if (boot.getPhysicalDefense() == 0 && boot.getMagicalDefense() == 0) {
            log.warn("{} has 0 in both physical and magical defense", boot);
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
