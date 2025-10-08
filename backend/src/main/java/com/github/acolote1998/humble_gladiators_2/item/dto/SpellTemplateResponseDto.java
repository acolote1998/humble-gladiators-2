package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.enums.SpellCategory;
import com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate;

import java.util.ArrayList;
import java.util.List;

public record SpellTemplateResponseDto(
        Long id,
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Boolean discovered,
        Integer quantity,
        Boolean equipped,
        Long campaignId,
        SpellCategory category,
        Integer physicalDamage,
        Integer magicalDamage,
        Integer restoreHp,
        String imgBase64
) {

    private static SpellTemplateResponseDto fromModelToDto(SpellTemplate spellTemplate) {
        return new SpellTemplateResponseDto(
                spellTemplate.getId(),
                spellTemplate.getName(),
                spellTemplate.getDescription(),
                spellTemplate.getRarity(),
                spellTemplate.getTier(),
                spellTemplate.getValue(),
                spellTemplate.getDiscovered(),
                spellTemplate.getQuantity(),
                spellTemplate.getEquipped(),
                spellTemplate.getCampaign().getId(),
                spellTemplate.getCategory(),
                spellTemplate.getPhysicalDamage(),
                spellTemplate.getMagicalDamage(),
                spellTemplate.getRestoreHp(),
                BytesToBase64.bytesToBase64(spellTemplate.getImgBytes())
        );
    }

    public static List<SpellTemplateResponseDto> fromListOfSpellTemplateToListOfDto(List<SpellTemplate> spellTemplates) {
        List<SpellTemplateResponseDto> dtos = new ArrayList<>();
        spellTemplates.forEach(spellTemplate -> dtos.add(fromModelToDto(spellTemplate)));
        return dtos;
    }
}