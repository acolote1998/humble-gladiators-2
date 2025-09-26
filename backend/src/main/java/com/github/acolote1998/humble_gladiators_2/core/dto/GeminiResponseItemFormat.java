package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GeminiResponseItemFormat(List<ItemFromGemini> generatedItems) {
    @JsonIgnoreProperties
    public record ItemFromGemini(
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
            RequirementDto requirement,
            Integer physicalDefense,
            Integer magicalDefense


    ) {
        public record RequirementDto(Long campaignId, List<RequirementEntry> requirements) {
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
}
