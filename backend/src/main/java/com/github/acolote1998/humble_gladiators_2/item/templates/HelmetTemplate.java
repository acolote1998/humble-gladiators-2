package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.enums.HelmetCategory;
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
@Table(name = "helmet_template")
@Slf4j
public class HelmetTemplate extends AbstractItem {
    private Integer physicalDefense;
    private Integer magicalDefense;

    @Enumerated(EnumType.STRING)
    private HelmetCategory category;

    public static String ObjectStructure(Long campaignId) {
        return String.format(
                """
                        HelmetTemplate{
                        String name
                        String description
                        Integer rarity (1 - 5)
                        Integer tier (1 - 5)
                        Long campaign_id (%s)
                        Requirement requirement (create a requirement object)
                        HelmetCategory category (enum)
                        Integer physicalDefense (1 or 0)
                        Integer magicalDefense (1 or 0)
                        }
                        //
                        // Combat effect flags:
                        //
                        //  - physicalDefense (1 = enables helmet to provide physical armor, 0 = helmet won't provide physical armor)
                        //  - magicalDefense (1 = enables helmet to provide magical armor, 0 = helmet won't provide magical armor)
                        //
                        // Combat effect flags rules:
                        //
                        //  - Use 1 to enable, 0 to disable.
                        //  - Never send null values
                        //  - At least one of these flags must be 1
                        //  - Both cannot be 0.
                        """,
                campaignId.toString());
    }

    public static boolean isValidHelmet(HelmetTemplate helmet) {
        if (helmet == null) {
            log.warn("HelmetTemplate is null");
            return false;
        }

        if (helmet.getName() == null || helmet.getName().isBlank()) {
            log.warn("{} has invalid name", helmet);
            return false;
        }

        if (helmet.getDescription() == null || helmet.getDescription().isBlank()) {
            log.warn("{} has invalid description", helmet);
            return false;
        }

        if (helmet.getRarity() == null || helmet.getRarity() < 1 || helmet.getRarity() > 5) {
            log.warn("{} has invalid rarity (expected 1–5)", helmet);
            return false;
        }

        if (helmet.getTier() == null || helmet.getTier() < 1 || helmet.getTier() > 5) {
            log.warn("{} has invalid tier (expected 1-5)", helmet);
            return false;
        }

        if (helmet.getValue() == null || helmet.getValue() < 0) {
            log.warn("{} has invalid value", helmet);
            return false;
        }

        if (helmet.getQuantity() == null || helmet.getQuantity() < 0 || helmet.getQuantity() > 1) {
            log.warn("{} has invalid quantity", helmet);
            return false;
        }

        if (helmet.getUserId() == null || helmet.getUserId().isBlank()) {
            log.warn("{} has invalid userId", helmet);
            return false;
        }

        if (helmet.getCampaign() == null) {
            log.warn("{} has no campaign assigned", helmet);
            return false;
        }

        if (helmet.getRequirement() == null) {
            log.warn("{} has no requirement assigned", helmet);
            return false;
        }

        if (helmet.getPhysicalDefense() == null || helmet.getPhysicalDefense() < 0) {
            log.warn("{} has invalid physical defense", helmet);
            return false;
        }
        if (helmet.getMagicalDefense() == null || helmet.getMagicalDefense() < 0) {
            log.warn("{} has invalid magical defense", helmet);
            return false;
        }
        if (helmet.getPhysicalDefense() == 0 && helmet.getMagicalDefense() == 0) {
            log.warn("{} has 0 in both physical and magical defense", helmet);
            return false;
        }
        return true;
    }

    public static boolean areValidHelmets(List<HelmetTemplate> helmets, Integer expectedAmount) {
        if (helmets.size() != expectedAmount) {
            return false;
        }
        for (HelmetTemplate helmet : helmets) {
            if (!HelmetTemplate.isValidHelmet(helmet)) {
                return false;
            }
        }
        return true;
    }
}
