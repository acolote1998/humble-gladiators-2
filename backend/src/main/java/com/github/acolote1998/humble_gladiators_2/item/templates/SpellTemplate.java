package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.enums.SpellCategory;
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
@Table(name = "spell_template")
@Slf4j
public class SpellTemplate extends AbstractItem {
    private Integer physicalDamage;
    private Integer magicalDamage;
    private Integer restoreHp;

    @Enumerated(EnumType.STRING)
    private SpellCategory category;

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
                SpellCategory category (enum)
                
                        // Combat effect flags
                        // - Use 1 to enable, 0 to disable.
                Integer physicalDamage (1 = spell deals physical damage, 0 = does not)
                Integer magicalDamage (1 = spell deals magical damage, 0 = does not)
                Integer restoreHp (1 = spell restores HP, 0 = does not)
                
                Rules:
                  - Healing spells (restoreHp = 1) must have physicalDamage = 0 and magicalDamage = 0.
                  - Damage spells (physicalDamage = 1 or magicalDamage = 1) must have restoreHp = 0.
                  - At least one of these flags must be 1. All three cannot be 0.
                  - If all three: restoreHp, physicalDamage and magicalDamage would be 0, it will be consider a total failure.
                }""", campaignId.toString());
    }
}
