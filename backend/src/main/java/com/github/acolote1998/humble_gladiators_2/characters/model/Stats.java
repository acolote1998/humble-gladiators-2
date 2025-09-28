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
                Stats{
                int constitution ((from 14 to 20 ) + rarity + tier)
                int intelligence ((from 14 to 20 ) + rarity + tier)
                int dexterity ((from 14 to 20 ) + rarity + tier)
                int strength ((from 14 to 20 ) + rarity + tier)
                int speed ((from 14 to 20 ) + rarity + tier)
                int luck ((from 14 to 20 ) + rarity + tier)
                int maxHp (always 0, calculated later on by the backend)
                int currentHp (always 0, calculated later on by the backend)
                int maxMp (always 0, calculated later on by the backend)
                int currentMp (always 0, calculated later on by the backend)
                int height (50 to 500) (for context: it is centimeters)
                int weight (20 to 2000) (for context: it is kilograms)
                int level (1 - 99)
                int currentExp (0)
                int expForNextLevel (0)
                }
                """;
    }

    public static Stats mapStatsFromCharacterFromGeminiDto(CharacterFromGeminiDto dto) {
        Stats stats = new Stats();

        // Base stats directly from Gemini
        stats.constitution = dto.stats().constitution();
        stats.intelligence = dto.stats().intelligence();
        stats.dexterity = dto.stats().dexterity();
        stats.strength = dto.stats().strength();
        stats.speed = dto.stats().speed();
        stats.luck = dto.stats().luck();
        stats.height = dto.stats().height();
        stats.weight = dto.stats().weight();
        stats.level = dto.stats().level();
        stats.currentExp = 0;
        stats.expForNextLevel = 0;

        stats.maxHp = (int) Math.round(stats.constitution * stats.level * ((dto.rarity() / 2.0) + (dto.tier() / 2.0)));
        stats.currentHp = stats.maxHp;

        stats.maxMp = (int) Math.round(stats.intelligence * stats.level * 10 * ((dto.rarity() / 2.0) + (dto.tier() / 2.0)));
        stats.currentMp = stats.maxMp;

        return stats;
    }
}
