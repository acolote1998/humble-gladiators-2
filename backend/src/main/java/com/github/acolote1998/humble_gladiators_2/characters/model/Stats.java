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
    private int maxMp;
    private int currentMp;
    private int height;
    private int weight;
    private int level;
    private int currentExp;
    private int expForNextLevel;

    public static String ObjectStructure() {
        return """
                int constitution (from 14 to 20)
                int intelligence (from 14 to 20)
                int dexterity (from 14 to 20)
                int strength (from 14 to 20)
                int speed (from 14 to 20)
                int luck (from 14 to 20)
                int maxHp (constitution * level)
                int currentHp (same as maxHp)
                int maxMp (intelligence * level * 10)
                int currentMp (same as maxMp)
                int height (50 to 500) (for context: it is centimeters)
                int weight (20 to 2000) (for context: it is kilograms)
                int level (1 - 99)
                int currentExp (0)
                int expForNextLevel (0)
                """;
    }
}
