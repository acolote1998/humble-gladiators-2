package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Tradeable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Usable;
import com.github.acolote1998.humble_gladiators_2.item.model.AbstractItem;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Slf4j
public class ConsumableTemplate extends AbstractItem implements Usable, Tradeable, Discoverable {
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
