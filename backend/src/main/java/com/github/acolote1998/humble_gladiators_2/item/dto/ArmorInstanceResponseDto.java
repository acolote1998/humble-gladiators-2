package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.characters.dto.HeroResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory;

public record ArmorInstanceResponseDto(
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Integer quantity,
        Boolean equipped,
        HeroResponseDto.CharacterInventoryResponseDto.RequirementResponseDto requirement,
        ArmorCategory category,
        Integer physicalDefense,
        Integer magicalDefense,
        String imgBase64
) {

}
