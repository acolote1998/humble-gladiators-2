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
@Table(name = "armor_template")
@Slf4j
public class ArmorTemplate extends AbstractItem {
    private Integer physicalDefense;
    private Integer magicalDefense;

    public static String objectStructure(Long campaignId) {
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
                Integer physicalDefense (0 - 50)
                Integer magicalDefense (0 - 50)
                }""", campaignId.toString());
    }
}
