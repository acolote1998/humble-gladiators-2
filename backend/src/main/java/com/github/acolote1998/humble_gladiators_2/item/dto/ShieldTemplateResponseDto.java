package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.enums.ShieldCategory;
import com.github.acolote1998.humble_gladiators_2.item.templates.ShieldTemplate;

import java.util.ArrayList;
import java.util.List;

public record ShieldTemplateResponseDto(
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
        ShieldCategory category,
        Integer physicalDefense,
        Integer magicalDefense,
        String imgBase64
) {

    private static ShieldTemplateResponseDto fromModelToDto(ShieldTemplate shieldTemplate) {
        return new ShieldTemplateResponseDto(
                shieldTemplate.getId(),
                shieldTemplate.getName(),
                shieldTemplate.getDescription(),
                shieldTemplate.getRarity(),
                shieldTemplate.getTier(),
                shieldTemplate.getValue(),
                shieldTemplate.getDiscovered(),
                shieldTemplate.getQuantity(),
                shieldTemplate.getEquipped(),
                shieldTemplate.getCampaign().getId(),
                shieldTemplate.getCategory(),
                shieldTemplate.getPhysicalDefense(),
                shieldTemplate.getMagicalDefense(),
                BytesToBase64.bytesToBase64(shieldTemplate.getImgBytes())
        );
    }

    public static List<ShieldTemplateResponseDto> fromListOfShieldTemplateToListOfDto(List<ShieldTemplate> shieldTemplates) {
        List<ShieldTemplateResponseDto> dtos = new ArrayList<>();
        shieldTemplates.forEach(shieldTemplate -> dtos.add(fromModelToDto(shieldTemplate)));
        return dtos;
    }
}