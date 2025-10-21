package com.github.acolote1998.humble_gladiators_2.item.interfaces;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;

public interface Attacker {
    Integer casuePhysicalDamage();

    Integer causeMagicalDamage();

    Integer usePhysicalAttack(CharacterInstance targetCharacter);
}
