package com.github.acolote1998.humble_gladiators_2.characters.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter

public class Level {
    private int level;
    private int currentExp;
    private int expForNextLevel;
}
