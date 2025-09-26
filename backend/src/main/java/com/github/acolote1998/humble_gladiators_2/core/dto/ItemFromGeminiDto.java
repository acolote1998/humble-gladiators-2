package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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
        GeminiResponseItemFormat.ItemFromGemini.RequirementDto requirement,
        Integer physicalDefense,
        Integer magicalDefense


) {
    public record RequirementDto(Long campaignId,
                                 List<GeminiResponseItemFormat.ItemFromGemini.RequirementDto.RequirementEntry> requirements) {
        @JsonIgnoreProperties
        public record RequirementEntry(
                String requirementType,
                String operator,
                String value,
                Long campaignId
        ) {
        }
    }
}