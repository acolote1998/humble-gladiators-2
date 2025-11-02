package com.github.acolote1998.humble_gladiators_2.characters.util;

import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.core.dto.CharacterFromGeminiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class StatsMapper {


    @Value("${MINIMUM_STAT_VALUE}")
    private Integer MINIMUM_STAT_VALUE;

    @Value("${MAXIMUM_STAT_VALUE}")
    private Integer MAXIMUM_STAT_VALUE;

    @Value("${CONSTITUTION_HP_MODIFIER}")
    private Integer CONSTITUTION_HP_MODIFIER;

    @Value("${CONSTITUTION_MP_MODIFIER}")
    private Integer CONSTITUTION_MP_MODIFIER;

    private Integer getRandomStat() {
        Random randomNumber = new Random();
        if (MINIMUM_STAT_VALUE == null ||
                MAXIMUM_STAT_VALUE == null ||
                MAXIMUM_STAT_VALUE < MINIMUM_STAT_VALUE) {
            log.error("CRITICAL - not valid minimum / maximum stat value in configuration. Using 16 as default value");
            return 16;
        }
        return randomNumber.nextInt(MINIMUM_STAT_VALUE, (MAXIMUM_STAT_VALUE + 1));
    }

    private Integer calculateHp(Integer constitution) {
        if (CONSTITUTION_HP_MODIFIER == null) {
            log.error("CRITICAL - not valid constitution/hp modifier in configuration. Using 5 as default value");
            return constitution * 5;
        }
        return constitution * CONSTITUTION_HP_MODIFIER;
    }

    private Integer calculateMp(Integer intelligence) {
        if (CONSTITUTION_MP_MODIFIER == null) {
            log.error("CRITICAL - not valid intelligence/mp modifier in configuration. Using 10 as default value");
            return intelligence * 10;
        }
        return intelligence * CONSTITUTION_MP_MODIFIER;
    }

    public Stats mapStatsFromCharacterFromGeminiDto(CharacterFromGeminiDto dto) {
        Stats stats = new Stats();

        stats.setConstitution(getRandomStat() + Math.round((float) (dto.rarity() + dto.tier()) / 3));
        stats.setIntelligence(getRandomStat() + Math.round((float) (dto.rarity() + dto.tier()) / 3));
        stats.setStrength(getRandomStat() + Math.round((float) (dto.rarity() + dto.tier()) / 3));
        stats.setSpeed(getRandomStat() + Math.round((float) (dto.rarity() + dto.tier()) / 3));
        stats.setLuck(getRandomStat() + Math.round((float) (dto.rarity() + dto.tier()) / 3));
        stats.setHeight(dto.stats().height());
        stats.setWeight(dto.stats().weight());
        stats.setLevel(dto.stats().level());
        stats.setCurrentExp(0);
        stats.setExpForNextLevel(0);

        stats.setMaxHp(calculateHp(stats.getConstitution()) + (dto.tier() + dto.rarity() * 2));
        stats.setCurrentHp(stats.getMaxHp());
        stats.setMaxMp(calculateMp(stats.getIntelligence()) + (dto.tier() + dto.rarity() * 2));
        stats.setCurrentMp(stats.getMaxMp());

        return stats;
    }

    public Stats createRandomInitialStats() {
        Stats stats = new Stats();
        Random randomNumber = new Random();
        stats.setConstitution(getRandomStat());
        stats.setIntelligence(getRandomStat());
        stats.setStrength(getRandomStat());
        stats.setSpeed(getRandomStat());
        stats.setLuck(getRandomStat());
        stats.setMaxHp(calculateHp(stats.getConstitution()));
        stats.setCurrentHp(stats.getMaxHp());
        stats.setMaxMp(calculateMp(stats.getIntelligence()));
        stats.setCurrentMp(stats.getMaxMp());
        stats.setHeight(randomNumber.nextInt(50, 251));// between 50 and 250 cm
        stats.setWeight(randomNumber.nextInt(50, 251));// between 50 and 250 kg
        stats.setLevel(1);
        stats.setCurrentExp(0);
        stats.setExpForNextLevel(0); //rework this
        return stats;
    }


    public static Stats CloneStats(Stats statsToClone) {
        Stats newStats = new Stats();
        newStats.setConstitution(statsToClone.getConstitution());
        newStats.setIntelligence(statsToClone.getIntelligence());
        newStats.setStrength(statsToClone.getStrength());
        newStats.setSpeed(statsToClone.getSpeed());
        newStats.setLuck(statsToClone.getLuck());
        newStats.setMaxHp(statsToClone.getMaxHp());
        newStats.setCurrentHp(statsToClone.getCurrentHp());
        newStats.setMaxMp(statsToClone.getMaxMp());
        newStats.setCurrentMp(statsToClone.getCurrentMp());
        newStats.setWeight(statsToClone.getWeight());
        newStats.setHeight(statsToClone.getHeight());
        newStats.setLevel(statsToClone.getLevel());
        newStats.setCurrentExp(statsToClone.getCurrentExp());
        newStats.setExpForNextLevel(statsToClone.getExpForNextLevel());
        return newStats;
    }
}
