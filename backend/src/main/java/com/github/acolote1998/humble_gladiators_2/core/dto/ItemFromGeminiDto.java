package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public record ItemFromGeminiDto(
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Boolean discovered,
        Integer quantity,
        Boolean equipped,
        @JsonProperty("campaign_id")
        Long campaignId,
        String category,
        Integer physicalDefense,
        Integer magicalDefense,
        Integer restoreHp,
        Integer restoreMp,
        Integer physicalDamage,
        Integer magicalDamage
) {
}