package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.enums.SpellCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.SpellInstance;

import java.util.List;

public record SpellInstanceResponseDto(
        Long campaignId,
        Long id,
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Integer quantity,
        Boolean equipped,
        SpellCategory category,
        Integer physicalDamage,
        Integer magicalDamage,
        Integer restoreHp,
        Boolean discovered,
        Integer mpCost,
        String imgBase64
) {

    public static List<SpellInstanceResponseDto> fromInstances(List<SpellInstance> spells) {
        if (spells == null) return List.of();
        return spells.stream()
                .map(spell -> new SpellInstanceResponseDto(
                        spell.getCampaign().getId(),
                        spell.getId(),
                        spell.getName(),
                        spell.getDescription(),
                        spell.getRarity(),
                        spell.getTier(),
                        spell.getValue(),
                        spell.getQuantity(),
                        spell.getEquipped(),
                        spell.getTemplate().getCategory(),
                        spell.getTemplate().getPhysicalDamage(),
                        spell.getTemplate().getMagicalDamage(),
                        spell.getTemplate().getRestoreHp(),
                        spell.getTemplate().getDiscovered(),
                        spell.getTemplate().getMpCost(),
                        BytesToBase64.bytesToBase64(spell.getTemplate().getImgBytes())
                ))
                .toList();
    }
}