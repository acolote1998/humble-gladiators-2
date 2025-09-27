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
@Table(name = "weapon_template")
@Slf4j
public class WeaponTemplate extends AbstractItem {
    private Integer physicalDamage;
    private Integer magicalDamage;

    public static String ObjectStructure(Long campaignId) {
        return String.format("""
                WeaponTemplate{
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
                Integer physicalDamage (Math.round((super.getTier() * 2.5 * super.getRarity() * 3));)
                Integer magicalDamage (0)
                }""", campaignId.toString());
    }
}
