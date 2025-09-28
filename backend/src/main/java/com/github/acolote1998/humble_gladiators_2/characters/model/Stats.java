package com.github.acolote1998.humble_gladiators_2.characters.model;

import com.github.acolote1998.humble_gladiators_2.core.dto.CharacterFromGeminiDto;
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
                int constitution ((from 14 to 20 ) + rarity + tier)
                int intelligence ((from 14 to 20 ) + rarity + tier)
                int dexterity ((from 14 to 20 ) + rarity + tier)
                int strength ((from 14 to 20 ) + rarity + tier)
                int speed ((from 14 to 20 ) + rarity + tier)
                int luck ((from 14 to 20 ) + rarity + tier)
                int maxHp (constitution * level) * ((rarity/2) + (tier/2))
                int currentHp (same as maxHp)
                int maxMp (intelligence * level * 10) * ((rarity/2) + (tier/2))
                int currentMp (same as maxMp)
                int height (50 to 500) (for context: it is centimeters)
                int weight (20 to 2000) (for context: it is kilograms)
                int level (1 - 99)
                int currentExp (0)
                int expForNextLevel (0)
                """;
    }

    public static Stats mapStatsFromCharacterFromGeminiDto(CharacterFromGeminiDto dto) {
        Stats stats = new Stats();
        stats.constitution = dto.stats().constitution();
        stats.intelligence = dto.stats().intelligence();
        stats.dexterity = dto.stats().dexterity();
        stats.strength = dto.stats().strength();
        stats.speed = dto.stats().speed();
        stats.luck = dto.stats().luck();
        stats.maxHp = dto.stats().maxHp();
        stats.currentHp = dto.stats().currentHp();
        stats.maxMp = dto.stats().maxMp();
        stats.currentMp = dto.stats().currentMp();
        stats.height = dto.stats().height();
        stats.weight = dto.stats().weight();
        stats.level = dto.stats().level();
        stats.currentExp = dto.stats().currentExp();
        stats.expForNextLevel = dto.stats().expForNextLevel();
        return stats;
    }
}
