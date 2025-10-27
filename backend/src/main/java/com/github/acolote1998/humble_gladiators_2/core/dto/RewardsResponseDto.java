package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.github.acolote1998.humble_gladiators_2.core.enums.BattleResultEnum;
import com.github.acolote1998.humble_gladiators_2.item.dto.*;
import com.github.acolote1998.humble_gladiators_2.item.instances.*;

import java.util.List;

public record RewardsResponseDto(
        Integer expReward,
        Integer goldReward,
        ItemLoot itemLoot,
        BattleResultEnum battleResult

) {
    public record ItemLoot(
            List<ArmorInstanceResponseDto> armorLoot,
            List<BootsInstanceResponseDto> bootsLoot,
            List<ConsumableInstanceResponseDto> consumablesLoot,
            List<HelmetInstanceResponseDto> helmetsLoot,
            List<ShieldInstanceResponseDto> shieldsLoot,
            List<SpellInstanceResponseDto> spellsLoot,
            List<WeaponInstanceResponseDto> weaponsLoot

    ) {
        public static ItemLoot fromListOfItems(List<ArmorInstance> armorInstances,
                                               List<BootsInstance> bootsInstances,
                                               List<ConsumableInstance> consumableInstances,
                                               List<HelmetInstance> helmetInstances,
                                               List<ShieldInstance> shieldInstances,
                                               List<SpellInstance> spellInstances,
                                               List<WeaponInstance> weaponInstances) {
            return new ItemLoot(
                    ArmorInstanceResponseDto.fromInstances(armorInstances),
                    BootsInstanceResponseDto.fromInstances(bootsInstances),
                    ConsumableInstanceResponseDto.fromInstances(consumableInstances),
                    HelmetInstanceResponseDto.fromInstances(helmetInstances),
                    ShieldInstanceResponseDto.fromInstances(shieldInstances),
                    SpellInstanceResponseDto.fromInstances(spellInstances),
                    WeaponInstanceResponseDto.fromInstances(weaponInstances)
            );
        }
    }
}
