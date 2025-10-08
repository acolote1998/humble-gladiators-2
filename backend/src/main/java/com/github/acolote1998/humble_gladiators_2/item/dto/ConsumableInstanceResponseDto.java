package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.dto.RequirementResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.enums.ConsumablesCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.ConsumableInstance;

import java.util.List;

public record ConsumableInstanceResponseDto(
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Integer quantity,
        Boolean equipped,
        RequirementResponseDto requirement,
        ConsumablesCategory category,
        Integer restoreHp,
        Integer restoreMp
) {

    public static List<ConsumableInstanceResponseDto> fromInstances(List<ConsumableInstance> consumables) {
        if (consumables == null) return List.of();
        return consumables.stream()
                .map(consumable -> new ConsumableInstanceResponseDto(
                        consumable.getName(),
                        consumable.getDescription(),
                        consumable.getRarity(),
                        consumable.getTier(),
                        consumable.getValue(),
                        consumable.getQuantity(),
                        consumable.getEquipped(),
                        RequirementResponseDto.fromRequirement(consumable.getRequirement()),
                        consumable.getTemplate().getCategory(),
                        consumable.getTemplate().getRestoreHp(),
                        consumable.getTemplate().getRestoreMp()
                ))
                .toList();
    }
}