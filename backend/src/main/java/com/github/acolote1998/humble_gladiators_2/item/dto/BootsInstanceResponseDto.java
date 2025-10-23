package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.dto.RequirementResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.enums.BootsCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.BootsInstance;

import java.util.List;

public record BootsInstanceResponseDto(
        Long id,
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
        Integer magicalDefense,
        String imgBase64,
        Boolean discovered) {

    public static BootsInstanceResponseDto fromModel(BootsInstance boots) {
        return new BootsInstanceResponseDto(
                boots.getId(),
                boots.getName(),
                boots.getDescription(),
                boots.getRarity(),
                boots.getTier(),
                boots.getValue(),
                boots.getQuantity(),
                boots.getEquipped(),
                RequirementResponseDto.fromRequirement(boots.getRequirement()),
                boots.getTemplate().getCategory(),
                boots.getTemplate().getPhysicalDefense(),
                boots.getTemplate().getMagicalDefense(),
                BytesToBase64.bytesToBase64(boots.getTemplate().getImgBytes()),
                boots.getTemplate().getDiscovered()
        );
    }

    public static List<BootsInstanceResponseDto> fromInstances(List<BootsInstance> boots) {
        if (boots == null)
            return List.of();
        return boots.stream()
                .map(boot -> fromModel(boot))
                .toList();
    }
}