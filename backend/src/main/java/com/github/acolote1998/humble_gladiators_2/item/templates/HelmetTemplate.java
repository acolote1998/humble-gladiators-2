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
        return String.format("""
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
                //  - Use 1 to enable, 0 to disable.
                //
                // Combat effect flags rules:
                //
                //  - At least one of these flags must be 1
                //  - Both cannot be 0.
                """, campaignId.toString());
    }
}
