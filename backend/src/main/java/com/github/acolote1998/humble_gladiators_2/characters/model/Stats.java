package com.github.acolote1998.humble_gladiators_2.characters.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Embeddable
@Getter
@Setter
@Slf4j
public class Stats {
    private int constitution;
    private int intelligence;
    private int strength;
    private int speed;
    private int luck;
    private int maxHp;
    private int currentHp;
    private int maxMp;
    private int currentMp;
    private int height;
    private int weight;
    private int level;
    private int currentExp;
    private int expForNextLevel;


    public static String ObjectStructure() {
        return """
                Stats{
                int height (50 to 500) (for context: it is centimeters)
                int weight (20 to 2000) (for context: it is kilograms)
                int level (1 - 99)
                }
                """;
    }

}
