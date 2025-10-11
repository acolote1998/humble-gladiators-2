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
                Integer restoreHp (always 0, calculated later on by the backend)
                Integer restoreMp (always 0, calculated later on by the backend)
                }""", campaignId.toString());
    }
}
