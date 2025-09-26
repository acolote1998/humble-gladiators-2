package com.github.acolote1998.humble_gladiators_2.item.instances;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Tradeable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Usable;
import com.github.acolote1998.humble_gladiators_2.item.model.AbstractItem;
import com.github.acolote1998.humble_gladiators_2.item.templates.ConsumableTemplate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Table(name = "consumable_instance")
@Slf4j
public class ConsumableInstance extends AbstractItem implements Usable, Tradeable, Discoverable {
    private Integer restoreHp;
    private Integer restoreMp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private ConsumableTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Override
    public void use() {

    }

    @Override
    public void discover() {

    }

    @Override
    public void buy() {

    }

    @Override
    public void sell() {

    }
}
