package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.dto.RequirementResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.enums.BootsCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.BootsInstance;

import java.util.List;

public record BootsInstanceResponseDto(
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Integer quantity,
        Boolean equipped,
        RequirementResponseDto requirement,
        BootsCategory category,
        Integer physicalDefense,
        Integer magicalDefense
) {

    public static List<BootsInstanceResponseDto> fromInstances(List<BootsInstance> boots) {
        if (boots == null) return List.of();
        return boots.stream()
                .map(boot -> new BootsInstanceResponseDto(
                        boot.getName(),
                        boot.getDescription(),
                        boot.getRarity(),
                        boot.getTier(),
                        boot.getValue(),
                        boot.getQuantity(),
                        boot.getEquipped(),
                        RequirementResponseDto.fromRequirement(boot.getRequirement()),
                        boot.getTemplate().getCategory(),
                        boot.getTemplate().getPhysicalDefense(),
                        boot.getTemplate().getMagicalDefense()
                ))
                .toList();
    }
}