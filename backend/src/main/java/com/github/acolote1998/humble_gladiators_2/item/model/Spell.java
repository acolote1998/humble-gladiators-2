package com.github.acolote1998.humble_gladiators_2.item.model;

import com.github.acolote1998.humble_gladiators_2.item.interfaces.Castable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Equippable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Tradeable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Slf4j
public class Spell extends AbstractItem implements Castable, Discoverable, Equippable, Tradeable {
    @Override
    public void cast() {

    }

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
