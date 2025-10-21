package com.github.acolote1998.humble_gladiators_2.item.interfaces;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.core.model.Action;

public interface Attacker {
    Integer causePhysicalDamage();

    Integer causeMagicalDamage();

    Action usePhysicalAttack(CharacterInstance targetCharacter);
}
