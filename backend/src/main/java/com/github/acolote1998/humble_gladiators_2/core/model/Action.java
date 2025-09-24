package com.github.acolote1998.humble_gladiators_2.core.model;

import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import com.github.acolote1998.humble_gladiators_2.core.enums.StateType;
import jakarta.persistence.Embeddable;

@Embeddable
public class Action {
    int damageCaused;
    int healingCaused;
    ActionType actionType;
    StateType stateCaused;
}
