package com.github.acolote1998.humble_gladiators_2.characters.model;

import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class CharacterInstance extends AbstractCharacter implements Discoverable {

    private List<Battle> victories;

    private List<Battle> defeats;

    private Boolean discovered;

    @Override
    public void discover() {

    }


}
