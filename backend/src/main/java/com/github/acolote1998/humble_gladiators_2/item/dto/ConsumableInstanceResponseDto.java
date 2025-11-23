package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.enums.ConsumablesCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.ConsumableInstance;

import java.util.List;

public record ConsumableInstanceResponseDto(
        Long campaignId,
        Long id,
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Integer quantity,
        Boolean equipped,
        ConsumablesCategory category,
        Integer restoreHp,
        Integer restoreMp,
        Boolean discovered,
        String imgBase64
) {
    public static ConsumableInstanceResponseDto fromModel(ConsumableInstance consumable) {
        return new ConsumableInstanceResponseDto(
                consumable.getCampaign().getId(),
                consumable.getId(),
                consumable.getName(),
                consumable.getDescription(),
                consumable.getRarity(),
                consumable.getTier(),
                consumable.getValue(),
                consumable.getQuantity(),
                consumable.getEquipped(),
                consumable.getTemplate().getCategory(),
                consumable.getTemplate().getRestoreHp(),
                consumable.getTemplate().getRestoreMp(),
                consumable.getTemplate().getDiscovered(),
                BytesToBase64.bytesToBase64(consumable.getTemplate().getImgBytes()));
    }

    public static List<ConsumableInstanceResponseDto> fromInstances(List<ConsumableInstance> consumables) {
        if (consumables == null) return List.of();
        return consumables.stream()
                .map(consumable -> new ConsumableInstanceResponseDto(
                        consumable.getCampaign().getId(),
                        consumable.getId(),
                        consumable.getName(),
                        consumable.getDescription(),
                        consumable.getRarity(),
                        consumable.getTier(),
                        consumable.getValue(),
                        consumable.getQuantity(),
                        consumable.getEquipped(),
                        consumable.getTemplate().getCategory(),
                        consumable.getTemplate().getRestoreHp(),
                        consumable.getTemplate().getRestoreMp(),
                        consumable.getTemplate().getDiscovered(),
                        BytesToBase64.bytesToBase64(consumable.getTemplate().getImgBytes())
                ))
                .toList();
    }
}