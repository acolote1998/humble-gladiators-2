package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.dto.RequirementResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.enums.HelmetCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.HelmetInstance;

import java.util.List;

public record HelmetInstanceResponseDto(
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Integer quantity,
        Boolean equipped,
        RequirementResponseDto requirement,
        HelmetCategory category,
        Integer physicalDefense,
        Integer magicalDefense
) {

    public static List<HelmetInstanceResponseDto> fromInstances(List<HelmetInstance> helmets) {
        if (helmets == null) return List.of();
        return helmets.stream()
                .map(helmet -> new HelmetInstanceResponseDto(
                        helmet.getName(),
                        helmet.getDescription(),
                        helmet.getRarity(),
                        helmet.getTier(),
                        helmet.getValue(),
                        helmet.getQuantity(),
                        helmet.getEquipped(),
                        RequirementResponseDto.fromRequirement(helmet.getRequirement()),
                        helmet.getTemplate().getCategory(),
                        helmet.getTemplate().getPhysicalDefense(),
                        helmet.getTemplate().getMagicalDefense()
                ))
                .toList();
    }
}