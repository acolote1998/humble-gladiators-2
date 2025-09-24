package com.github.acolote1998.humble_gladiators_2.characters.model;

import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import jakarta.persistence.*;

@Entity
public class CharacterInstance extends AbstractCharacter implements Discoverable {

    private Boolean discovered;

    @Override
    public void discover() {

    }


}
