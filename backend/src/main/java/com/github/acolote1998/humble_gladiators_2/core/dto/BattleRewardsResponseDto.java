package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.github.acolote1998.humble_gladiators_2.core.enums.BattleResultEnum;
import com.github.acolote1998.humble_gladiators_2.core.model.BattleReward;
import com.github.acolote1998.humble_gladiators_2.item.dto.*;

import java.util.List;

public record BattleRewardsResponseDto(
        Integer expReward,
        Integer goldReward,
        List<ArmorInstanceResponseDto> armorLoot,
        List<BootsInstanceResponseDto> bootsLoot,
        List<ConsumableInstanceResponseDto> consumablesLoot,
        List<HelmetInstanceResponseDto> helmetsLoot,
        List<ShieldInstanceResponseDto> shieldsLoot,
        List<SpellInstanceResponseDto> spellsLoot,
        List<WeaponInstanceResponseDto> weaponsLoot,
        BattleResultEnum battleResult

) {
    public static BattleRewardsResponseDto fromModel(BattleReward model) {
        return new BattleRewardsResponseDto(
                model.getExpReward(),
                model.getGoldReward(),
                ArmorInstanceResponseDto.fromInstances(model.getArmorLoot()),
                BootsInstanceResponseDto.fromInstances(model.getBootsLoot()),
                ConsumableInstanceResponseDto.fromInstances(model.getConsumablesLoot()),
                HelmetInstanceResponseDto.fromInstances(model.getHelmetsLoot()),
                ShieldInstanceResponseDto.fromInstances(model.getShieldsLoot()),
                SpellInstanceResponseDto.fromInstances(model.getSpellsLoot()),
                WeaponInstanceResponseDto.fromInstances(model.getWeaponsLoot()),
                model.getBattleResult()
        );
    }
}
