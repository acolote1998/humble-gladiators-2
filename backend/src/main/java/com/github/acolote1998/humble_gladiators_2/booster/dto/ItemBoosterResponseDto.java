package com.github.acolote1998.humble_gladiators_2.booster.dto;

import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import com.github.acolote1998.humble_gladiators_2.item.dto.*;

import java.util.List;

public record ItemBoosterResponseDto(
        List<ArmorTemplateResponseDto> armors,
        List<BootsTemplateResponseDto> boots,
        List<ConsumableTemplateResponseDto> consumables,
        List<HelmetTemplateResponseDto> helmets,
        List<ShieldTemplateResponseDto> shields,
        List<SpellTemplateResponseDto> spells,
        List<WeaponTemplateResponseDto> weapons) {

    static public ItemBoosterResponseDto fromModelToDto(ItemsBooster model) {
        ItemBoosterResponseDto dto = new ItemBoosterResponseDto(
                ArmorTemplateResponseDto.fromListOfArmorTemplateToListOfDto(model.getArmors()),
                BootsTemplateResponseDto.fromListOfBootsTemplateToListOfDto(model.getBoots()),
                ConsumableTemplateResponseDto.fromListOfConsumableTemplateToListOfDto(model.getConsumables()),
                HelmetTemplateResponseDto.fromListOfHelmetTemplateToListOfDto(model.getHelmets()),
                ShieldTemplateResponseDto.fromListOfShieldTemplateToListOfDto(model.getShields()),
                SpellTemplateResponseDto.fromListOfSpellTemplateToListOfDto(model.getSpells()),
                WeaponTemplateResponseDto.fromListOfWeaponTemplateToListOfDto(model.getWeapons())

        );
        return dto;
    }
}
