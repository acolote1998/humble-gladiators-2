package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.enums.HelmetCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.HelmetInstance;

import java.util.List;

public record HelmetInstanceResponseDto(
        Long campaignId,
        Long id,
        String name,
        String description,
        Integer rarity,
        Integer tier,
        Integer value,
        Integer quantity,
        Boolean equipped,
        HelmetCategory category,
        Integer physicalDefense,
        Integer magicalDefense,
        String imgBase64,
        Boolean discovered) {

    public static HelmetInstanceResponseDto fromModel(HelmetInstance helmet) {
        return new HelmetInstanceResponseDto(
                helmet.getCampaign().getId(),
                helmet.getId(),
                helmet.getName(),
                helmet.getDescription(),
                helmet.getRarity(),
                helmet.getTier(),
                helmet.getValue(),
                helmet.getQuantity(),
                helmet.getEquipped(),
                helmet.getTemplate().getCategory(),
                helmet.getTemplate().getPhysicalDefense(),
                helmet.getTemplate().getMagicalDefense(),
                BytesToBase64.bytesToBase64(helmet.getTemplate().getImgBytes()),
                helmet.getTemplate().getDiscovered()
        );
    }

    public static List<HelmetInstanceResponseDto> fromInstances(List<HelmetInstance> helmets) {
        if (helmets == null)
            return List.of();
        return helmets.stream()
                .map(helmet -> fromModel(helmet))
                .toList();
    }
}