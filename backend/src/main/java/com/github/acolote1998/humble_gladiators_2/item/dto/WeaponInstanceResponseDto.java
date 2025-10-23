package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.dto.RequirementResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.enums.WeaponCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.WeaponInstance;

import java.util.List;

public record WeaponInstanceResponseDto(
        Long campaignId,
        Long id,
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Integer quantity,
        Boolean equipped,
        RequirementResponseDto requirement,
        WeaponCategory category,
        Integer physicalDamage,
        Integer magicalDamage,
        String imgBase64,
        Boolean discovered) {

    public static WeaponInstanceResponseDto fromModel(WeaponInstance weapon) {
        return new WeaponInstanceResponseDto(
                weapon.getCampaign().getId(),
                weapon.getId(),
                weapon.getName(),
                weapon.getDescription(),
                weapon.getRarity(),
                weapon.getTier(),
                weapon.getValue(),
                weapon.getQuantity(),
                weapon.getEquipped(),
                RequirementResponseDto.fromRequirement(weapon.getRequirement()),
                weapon.getTemplate().getCategory(),
                weapon.getTemplate().getPhysicalDamage(),
                weapon.getTemplate().getMagicalDamage(),
                BytesToBase64.bytesToBase64(weapon.getTemplate().getImgBytes()),
                weapon.getTemplate().getDiscovered());
    }

    public static List<WeaponInstanceResponseDto> fromInstances(List<WeaponInstance> weapons) {
        if (weapons == null)
            return List.of();
        return weapons.stream()
                .map(weapon -> fromModel(weapon))
                .toList();
    }
}