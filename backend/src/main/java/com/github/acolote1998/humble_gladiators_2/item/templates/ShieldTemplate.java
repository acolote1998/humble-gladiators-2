package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.enums.ShieldCategory;
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
@Table(name = "shield_template")
@Slf4j
public class ShieldTemplate extends AbstractItem {
    private Integer physicalDefense;
    private Integer magicalDefense;

    @Enumerated(EnumType.STRING)
    private ShieldCategory category;

    public static String ObjectStructure(Long campaignId) {
        return String.format(
                """
                        ShieldTemplate{
                        String name
                        String description
                        Integer rarity (1 - 5)
                        Integer tier (1 - 5)
                        Long campaign_id (%s)
                        Requirement requirement (create a requirement object)
                        ShieldCategory category (enum)
                        Integer physicalDefense (1 or 0)
                        Integer magicalDefense (1 or 0)
                        }
                        //
                        // Combat effect flags:
                        //
                        //  - physicalDefense (1 = enables shield to provide physical armor, 0 = shield won't provide physical armor)
                        //  - magicalDefense (1 = enables shield to have magical armor, 0 = shield won't provide magical armor)
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

    public static boolean isValidShield(ShieldTemplate shield) {
        if (shield == null) {
            log.warn("ShieldTemplate is null");
            return false;
        }

        if (shield.getName() == null || shield.getName().isBlank()) {
            log.warn("{} has invalid name", shield);
            return false;
        }

        if (shield.getDescription() == null || shield.getDescription().isBlank()) {
            log.warn("{} has invalid description", shield);
            return false;
        }

        if (shield.getRarity() == null || shield.getRarity() < 1 || shield.getRarity() > 5) {
            log.warn("{} has invalid rarity (expected 1–5)", shield);
            return false;
        }

        if (shield.getTier() == null || shield.getTier() < 1 || shield.getTier() > 5) {
            log.warn("{} has invalid tier (expected 1-5)", shield);
            return false;
        }

        if (shield.getValue() == null || shield.getValue() < 0) {
            log.warn("{} has invalid value", shield);
            return false;
        }

        if (shield.getQuantity() == null || shield.getQuantity() < 0 || shield.getQuantity() > 1) {
            log.warn("{} has invalid quantity", shield);
            return false;
        }

        if (shield.getUserId() == null || shield.getUserId().isBlank()) {
            log.warn("{} has invalid userId", shield);
            return false;
        }

        if (shield.getCampaign() == null) {
            log.warn("{} has no campaign assigned", shield);
            return false;
        }

        if (shield.getRequirement() == null) {
            log.warn("{} has no requirement assigned", shield);
            return false;
        }

        if (shield.getPhysicalDefense() == null || shield.getPhysicalDefense() < 0) {
            log.warn("{} has invalid physical defense", shield);
            return false;
        }
        if (shield.getMagicalDefense() == null || shield.getMagicalDefense() < 0) {
            log.warn("{} has invalid magical defense", shield);
            return false;
        }
        if (shield.getPhysicalDefense() == 0 && shield.getMagicalDefense() == 0) {
            log.warn("{} has 0 in both physical and magical defense", shield);
            return false;
        }
        return true;
    }

    public static boolean areValidShields(List<ShieldTemplate> shields, Integer expectedAmount) {
        if (shields.size() != expectedAmount) {
            return false;
        }
        for (ShieldTemplate shield : shields) {
            if (!ShieldTemplate.isValidShield(shield)) {
                return false;
            }
        }
        return true;
    }
}
