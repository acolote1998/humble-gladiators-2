package com.github.acolote1998.humble_gladiators_2.core.model;

import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import com.github.acolote1998.humble_gladiators_2.core.enums.StateType;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Action {
    private Integer damageCaused;
    private Integer healingCaused;
    private ActionType actionType;
    private StateType stateCaused;
}
