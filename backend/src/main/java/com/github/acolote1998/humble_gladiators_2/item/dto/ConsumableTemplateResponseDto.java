package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.enums.ConsumablesCategory;
import com.github.acolote1998.humble_gladiators_2.item.templates.ConsumableTemplate;

import java.util.ArrayList;
import java.util.List;

public record ConsumableTemplateResponseDto(
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
        ConsumablesCategory category,
        Integer restoreHp,
        Integer restoreMp,
        String imgBase64
) {

    private static ConsumableTemplateResponseDto fromModelToDto(ConsumableTemplate consumableTemplate) {
        return new ConsumableTemplateResponseDto(
                consumableTemplate.getId(),
                consumableTemplate.getName(),
                consumableTemplate.getDescription(),
                consumableTemplate.getRarity(),
                consumableTemplate.getTier(),
                consumableTemplate.getValue(),
                consumableTemplate.getDiscovered(),
                consumableTemplate.getQuantity(),
                consumableTemplate.getEquipped(),
                consumableTemplate.getCampaign().getId(),
                consumableTemplate.getCategory(),
                consumableTemplate.getRestoreHp(),
                consumableTemplate.getRestoreMp(),
                BytesToBase64.bytesToBase64(consumableTemplate.getImgBytes())
        );
    }

    public static List<ConsumableTemplateResponseDto> fromListOfConsumableTemplateToListOfDto(List<ConsumableTemplate> consumableTemplates) {
        List<ConsumableTemplateResponseDto> dtos = new ArrayList<>();
        consumableTemplates.forEach(consumableTemplate -> dtos.add(fromModelToDto(consumableTemplate)));
        return dtos;
    }
}