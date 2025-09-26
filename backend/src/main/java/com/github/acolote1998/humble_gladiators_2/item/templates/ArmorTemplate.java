package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.interfaces.Defendable;
import com.github.acolote1998.humble_gladiators_2.item.model.AbstractItem;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Table(name = "armor_template")
@Slf4j
public class ArmorTemplate extends AbstractItem implements Defendable {
    private Integer physicalDefense;
    private Integer magicalDefense;

    public static String ObjectStructure(Long campaignId) {
        return String.format("""
                ArmorTemplate{
                String name
                String description
                Integer rarity (1 - 5)
                Integer tier (1 - 5)
                Integer value
                Boolean discovered (always false)
                Integer quantity (always 1)
                Boolean equipped (always false)
                Long campaign_id (%s)
                Requirement requirement (create a requirement object)
                Integer physicalDefense (always 0)
                Integer magicalDefense (always 0)
                }""", campaignId.toString());
    }

    @Override
    public Long calculateDefense() {
        return Math.round((super.getTier() * 1.5 * super.getRarity() * 2));
    }
}
