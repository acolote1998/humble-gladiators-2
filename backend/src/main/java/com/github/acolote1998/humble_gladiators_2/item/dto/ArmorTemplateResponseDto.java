package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;

import java.util.ArrayList;
import java.util.List;

public record ArmorTemplateResponseDto(
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
        ArmorCategory category,
        Integer physicalDefense,
        Integer magicalDefense,
        String imgBase64
) {

    private static ArmorTemplateResponseDto fromModelToDto(ArmorTemplate armorTemplate) {
        return new ArmorTemplateResponseDto(
                armorTemplate.getId(),
                armorTemplate.getName(),
                armorTemplate.getDescription(),
                armorTemplate.getRarity(),
                armorTemplate.getTier(),
                armorTemplate.getValue(),
                armorTemplate.getDiscovered(),
                armorTemplate.getQuantity(),
                armorTemplate.getEquipped(),
                armorTemplate.getCampaign().getId(),
                armorTemplate.getCategory(),
                armorTemplate.getPhysicalDefense(),
                armorTemplate.getMagicalDefense(),
                BytesToBase64.bytesToBase64(armorTemplate.getImgBytes())
        );
    }

    public static List<ArmorTemplateResponseDto> fromListOfArmorTemplateToListOfDto(List<ArmorTemplate> armorTemplates) {
        List<ArmorTemplateResponseDto> dtos = new ArrayList<>();
        armorTemplates.forEach(armorTemplate -> dtos.add(fromModelToDto(armorTemplate)));
        return dtos;
    }
}