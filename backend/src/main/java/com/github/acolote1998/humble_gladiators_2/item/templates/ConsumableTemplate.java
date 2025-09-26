package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.model.AbstractItem;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Table(name = "consumable_template")
@Slf4j
public class ConsumableTemplate extends AbstractItem {
    private Integer restoreHp;
    private Integer restoreMp;

    public static String ObjectStructure(Long campaignId) {
        return String.format("""
                ConsumableTemplate{
                String name
                String description
                Integer rarity (1 - 5)
                Integer tier (1 - 5)
                Integer value ( (tier*13)+(rarity*13) )
                Boolean discovered (always false)
                Integer quantity (always 0)
                Boolean equipped (always false)
                Long campaign_id (%s)
                Requirement requirement (create a requirement object)
                Integer restoreHp (Math.round((super.getTier() * 1.5 * super.getRarity() * 1.5));)
                Integer restoreMp (Math.round((super.getTier() * 2 * super.getRarity() * 4));)
                }""", campaignId.toString());
    }
}
