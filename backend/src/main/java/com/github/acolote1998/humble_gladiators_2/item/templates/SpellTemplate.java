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
@Table(name = "spell_template")
@Slf4j
public class SpellTemplate extends AbstractItem {
    private Integer physicalDamage;
    private Integer magicalDamage;
    private Integer restoreHp;

    public static String ObjectStructure(Long campaignId) {
        return String.format("""
                SpellTemplate{
                String name
                String description
                Integer rarity (1 - 5)
                Integer tier (1 - 5)
                Integer value ( (tier*80)+(rarity*280) )
                Boolean discovered (always false)
                Integer quantity (always 0)
                Boolean equipped (always false)
                Long campaign_id (%s)
                Requirement requirement (create a requirement object)
                Integer physicalDamage (always 0, calculated later on by the backend)
                Integer magicalDamage (always 0, calculated later on by the backend)
                Integer restoreHp (always 0, calculated later on by the backend)
                
                Important:
                -If the spell will have restoreHp (healing spell), then magicalDamage value must be zero
                -If the spell will have magicalDamage (damage spell), then the restoreHp value must be zero
                }""", campaignId.toString());
    }
}
