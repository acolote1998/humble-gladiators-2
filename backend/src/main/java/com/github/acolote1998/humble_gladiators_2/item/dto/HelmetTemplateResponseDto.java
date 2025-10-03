package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.item.enums.HelmetCategory;
import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;

import java.util.ArrayList;
import java.util.List;

public record HelmetTemplateResponseDto(
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
        HelmetCategory category,
        Integer physicalDefense,
        Integer magicalDefense
) {

    private static HelmetTemplateResponseDto fromModelToDto(HelmetTemplate helmetTemplate) {
        return new HelmetTemplateResponseDto(
                helmetTemplate.getId(),
                helmetTemplate.getName(),
                helmetTemplate.getDescription(),
                helmetTemplate.getRarity(),
                helmetTemplate.getTier(),
                helmetTemplate.getValue(),
                helmetTemplate.getDiscovered(),
                helmetTemplate.getQuantity(),
                helmetTemplate.getEquipped(),
                helmetTemplate.getCampaign().getId(),
                helmetTemplate.getCategory(),
                helmetTemplate.getPhysicalDefense(),
                helmetTemplate.getMagicalDefense()
        );
    }

    public static List<HelmetTemplateResponseDto> fromListOfHelmetTemplateToListOfDto(List<HelmetTemplate> helmetTemplates) {
        List<HelmetTemplateResponseDto> dtos = new ArrayList<>();
        helmetTemplates.forEach(helmetTemplate -> dtos.add(fromModelToDto(helmetTemplate)));
        return dtos;
    }
}