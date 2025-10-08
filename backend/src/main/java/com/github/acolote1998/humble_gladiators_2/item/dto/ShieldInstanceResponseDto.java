package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.dto.RequirementResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.enums.ShieldCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.ShieldInstance;

import java.util.List;

public record ShieldInstanceResponseDto(
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Integer quantity,
        Boolean equipped,
        RequirementResponseDto requirement,
        ShieldCategory category,
        Integer physicalDefense,
        Integer magicalDefense
) {

    public static List<ShieldInstanceResponseDto> fromInstances(List<ShieldInstance> shields) {
        if (shields == null) return List.of();
        return shields.stream()
                .map(shield -> new ShieldInstanceResponseDto(
                        shield.getName(),
                        shield.getDescription(),
                        shield.getRarity(),
                        shield.getTier(),
                        shield.getValue(),
                        shield.getQuantity(),
                        shield.getEquipped(),
                        RequirementResponseDto.fromRequirement(shield.getRequirement()),
                        shield.getTemplate().getCategory(),
                        shield.getTemplate().getPhysicalDefense(),
                        shield.getTemplate().getMagicalDefense()
                ))
                .toList();
    }
}