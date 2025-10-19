package com.github.acolote1998.humble_gladiators_2.item.interfaces;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.SpellInstance;

public interface Castable {
    void castSpell(SpellInstance spell, CharacterInstance targetCharacter);
}
