package com.github.acolote1998.humble_gladiators_2.characters.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Stats {
    private int constitution;
    private int intelligence;
    private int dexterity;
    private int strength;
    private int speed;
    private int luck;
    private int maxHp;
    private int currentHp;
    private int height;
    private int weight;
    private int level;
    private int currentExp;
    private int expForNextLevel;
}
