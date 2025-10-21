package com.github.acolote1998.humble_gladiators_2.item.interfaces;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.core.model.Action;
import com.github.acolote1998.humble_gladiators_2.item.instances.SpellInstance;

public interface Attacker {
    Integer causePhysicalDamage();

    Integer causePhysicalSpellDamage(SpellInstance spellToUse);

    Integer causeMagicalDamage(SpellInstance spellToUse);

    Action usePhysicalAttack(CharacterInstance targetCharacter);
}
