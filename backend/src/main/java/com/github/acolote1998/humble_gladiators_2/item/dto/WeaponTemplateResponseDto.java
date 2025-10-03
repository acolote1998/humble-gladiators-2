package com.github.acolote1998.humble_gladiators_2.item.dto;

import com.github.acolote1998.humble_gladiators_2.item.enums.WeaponCategory;
import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;

import java.util.ArrayList;
import java.util.List;

public record WeaponTemplateResponseDto(
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
        WeaponCategory category,
        Integer physicalDamage,
        Integer magicalDamage
) {

    private static WeaponTemplateResponseDto fromModelToDto(WeaponTemplate weaponTemplate) {
        return new WeaponTemplateResponseDto(
                weaponTemplate.getId(),
                weaponTemplate.getName(),
                weaponTemplate.getDescription(),
                weaponTemplate.getRarity(),
                weaponTemplate.getTier(),
                weaponTemplate.getValue(),
                weaponTemplate.getDiscovered(),
                weaponTemplate.getQuantity(),
                weaponTemplate.getEquipped(),
                weaponTemplate.getCampaign().getId(),
                weaponTemplate.getCategory(),
                weaponTemplate.getPhysicalDamage(),
                weaponTemplate.getMagicalDamage()
        );
    }

    public static List<WeaponTemplateResponseDto> fromListOfWeaponTemplateToListOfDto(List<WeaponTemplate> weaponTemplates) {
        List<WeaponTemplateResponseDto> dtos = new ArrayList<>();
        weaponTemplates.forEach(weaponTemplate -> dtos.add(fromModelToDto(weaponTemplate)));
        return dtos;
    }
}