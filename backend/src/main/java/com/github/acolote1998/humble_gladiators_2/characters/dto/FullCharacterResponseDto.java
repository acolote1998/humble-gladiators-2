package com.github.acolote1998.humble_gladiators_2.characters.dto;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterCategory;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;

public record FullCharacterResponseDto(
        CharacterStatsResponseDto stats,
        CharacterCategory category,
        CharacterType characterType,
        String name,
        String description,
        Boolean discovered,
        Long campaignId,
        Integer rarity,
        Integer tier,
        Integer goldReward,
        Integer expReward
) {

    private record CharacterStatsResponseDto(
            int constitution,
            int intelligence,
            int strength,
            int speed,
            int luck,
            int maxHp,
            int currentHp,
            int maxMp,
            int currentMp,
            int height,
            int weight,
            int level,
            int currentExp,
            int expForNextLevel
    ) {
    }
}
