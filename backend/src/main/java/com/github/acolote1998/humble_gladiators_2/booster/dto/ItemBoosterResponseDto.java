package com.github.acolote1998.humble_gladiators_2.booster.dto;

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
}
