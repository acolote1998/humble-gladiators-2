package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.dto.RequirementResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.ArmorInstance;

import java.util.List;

public record ArmorInstanceResponseDto(
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
        Integer magicalDefense,
        String imgBase64
) {

    public static List<ArmorInstanceResponseDto> fromInstances(List<ArmorInstance> armors) {
        if (armors == null) return List.of();
        return armors.stream()
                .map(armor -> new ArmorInstanceResponseDto(
                        armor.getName(),
                        armor.getDescription(),
                        armor.getRarity(),
                        armor.getTier(),
                        armor.getValue(),
                        armor.getQuantity(),
                        armor.getEquipped(),
                        RequirementResponseDto.fromRequirement(armor.getRequirement()),
                        armor.getTemplate().getCategory(),
                        armor.getTemplate().getPhysicalDefense(),
                        armor.getTemplate().getMagicalDefense(),
                        BytesToBase64.bytesToBase64(armor.getTemplate().getImgBytes())
                ))
                .toList();
    }
}
