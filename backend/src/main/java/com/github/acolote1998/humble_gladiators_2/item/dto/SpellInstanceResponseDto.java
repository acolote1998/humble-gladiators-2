package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.dto.RequirementResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.enums.SpellCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.SpellInstance;

import java.util.List;

public record SpellInstanceResponseDto(
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Integer quantity,
        Boolean equipped,
        RequirementResponseDto requirement,
        SpellCategory category,
        Integer physicalDamage,
        Integer magicalDamage,
        Integer restoreHp
) {

    public static List<SpellInstanceResponseDto> fromInstances(List<SpellInstance> spells) {
        if (spells == null) return List.of();
        return spells.stream()
                .map(spell -> new SpellInstanceResponseDto(
                        spell.getName(),
                        spell.getDescription(),
                        spell.getRarity(),
                        spell.getTier(),
                        spell.getValue(),
                        spell.getQuantity(),
                        spell.getEquipped(),
                        RequirementResponseDto.fromRequirement(spell.getRequirement()),
                        spell.getTemplate().getCategory(),
                        spell.getTemplate().getPhysicalDamage(),
                        spell.getTemplate().getMagicalDamage(),
                        spell.getTemplate().getRestoreHp()
                ))
                .toList();
    }
}