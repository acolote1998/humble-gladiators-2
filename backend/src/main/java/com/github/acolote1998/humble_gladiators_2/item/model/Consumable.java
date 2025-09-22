package com.github.acolote1998.humble_gladiators_2.item.model;

import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Tradeable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Usable;
import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
public class Consumable extends AbstractItem implements Usable, Tradeable, Discoverable {
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
