package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;

public record CharacterFromGeminiDto(
        StatsFromGemini stats,
        CharacterType characterType,
        String name,
        Boolean discovered,
        @JsonProperty("campaign_id")
        Long campaignId,
        Integer rarity,
        Integer tier,
        Integer goldReward,
        Integer expReward
) {

    public record StatsFromGemini(
            Integer constitution,
            Integer intelligence,
            Integer dexterity,
            Integer strength,
            Integer speed,
            Integer luck,
            Integer maxHp,
            Integer currentHp,
            Integer maxMp,
            Integer currentMp,
            Integer height,
            Integer weight,
            Integer level,
            Integer currentExp,
            Integer expForNextLevel
    ) {
    }
}
