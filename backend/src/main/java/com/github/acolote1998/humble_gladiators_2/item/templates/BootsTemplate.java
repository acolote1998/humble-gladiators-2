package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Equippable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Tradeable;
import com.github.acolote1998.humble_gladiators_2.item.model.AbstractItem;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Slf4j
public class BootsTemplate extends AbstractItem implements Discoverable, Equippable, Tradeable {
    private Integer physicalDefense;
    private Integer magicalDefense;

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
