package com.github.acolote1998.humble_gladiators_2.characters.dto;

import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryOperator;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryType;
import com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory;

import java.util.List;

public record HeroResponseDto(
        String name,
        CharacterStatsResponseDto stats,
        CharacterInventoryResponseDto inventory
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

    private record CharacterInventoryResponseDto(
            List<ArmorInstanceResponseDto> armors
    ) {
        private record ArmorInstanceResponseDto(
                String name,
                String description,
                Integer rarity,
                Integer tier,
                Integer value,
                Integer quantity,
                Boolean equipped,
                RequirementResponseDto requirement,
                ArmorCategory category,
                Integer physicalDefense,
                Integer magicalDefense
        ) {

        }

        private record RequirementResponseDto(List<RequirementEntryResponseDto> requirements) {

            private record RequirementEntryResponseDto(
                    RequirementEntryType requirementType,
                    RequirementEntryOperator operator,
                    String value
            ) {
            }
        }
    }
}