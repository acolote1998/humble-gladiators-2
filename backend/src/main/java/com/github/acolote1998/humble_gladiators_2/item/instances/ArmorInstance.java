package com.github.acolote1998.humble_gladiators_2.item.instances;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Equippable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Tradeable;
import com.github.acolote1998.humble_gladiators_2.item.model.AbstractItem;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Table(name = "armor_instance")
@Slf4j
public class ArmorInstance extends AbstractItem implements Discoverable, Equippable, Tradeable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private ArmorTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Override
    public void discover() {

    }

    @Override
    public void equip() {

    }

    @Override
    public void unequip() {

    }

    @Override
    public void buy() {

    }

    @Override
    public void sell() {

    }
}
