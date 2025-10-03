package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.item.enums.BootsCategory;
import com.github.acolote1998.humble_gladiators_2.item.templates.BootsTemplate;

import java.util.ArrayList;
import java.util.List;

public record BootsTemplateResponseDto(
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
        BootsCategory category,
        Integer physicalDefense,
        Integer magicalDefense
) {

    private static BootsTemplateResponseDto fromModelToDto(BootsTemplate bootsTemplate) {
        return new BootsTemplateResponseDto(
                bootsTemplate.getId(),
                bootsTemplate.getName(),
                bootsTemplate.getDescription(),
                bootsTemplate.getRarity(),
                bootsTemplate.getTier(),
                bootsTemplate.getValue(),
                bootsTemplate.getDiscovered(),
                bootsTemplate.getQuantity(),
                bootsTemplate.getEquipped(),
                bootsTemplate.getCampaign().getId(),
                bootsTemplate.getCategory(),
                bootsTemplate.getPhysicalDefense(),
                bootsTemplate.getMagicalDefense()
        );
    }

    public static List<BootsTemplateResponseDto> fromListOfBootsTemplateToListOfDto(List<BootsTemplate> bootsTemplates) {
        List<BootsTemplateResponseDto> dtos = new ArrayList<>();
        bootsTemplates.forEach(bootsTemplate -> dtos.add(fromModelToDto(bootsTemplate)));
        return dtos;
    }
}