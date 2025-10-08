package com.github.acolote1998.humble_gladiators_2.characters.dto;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.item.dto.*;

import java.util.List;

public record HeroResponseDto(
        String name,
        CharacterStatsResponseDto stats,
        CharacterInventoryResponseDto inventory
) {
    private record CharacterStatsResponseDto(
            int constitution,
            int intelligence,
            int strength,
            int speed,
            int luck,
            int maxHp,
            int currentHp,
            int maxMp,
            int currentMp,
            int height,
            int weight,
            int level,
            int currentExp,
            int expForNextLevel
    ) {
    }

    public record CharacterInventoryResponseDto(
            List<ArmorInstanceResponseDto> armors,
            List<BootsInstanceResponseDto> boots,
            List<ConsumableInstanceResponseDto> consumables,
            List<HelmetInstanceResponseDto> helmets,
            List<ShieldInstanceResponseDto> shields,
            List<SpellInstanceResponseDto> spells,
            List<WeaponInstanceResponseDto> weapons
    ) {
    }

    public static HeroResponseDto fromModelToDto(CharacterInstance model) {
        return new HeroResponseDto(
                model.getName(),
                mapStats(model.getStats()),
                mapInventory(model.getInventory())
        );
    }

    private static CharacterStatsResponseDto mapStats(Stats stats) {
        return new CharacterStatsResponseDto(
                stats.getConstitution(),
                stats.getIntelligence(),
                stats.getStrength(),
                stats.getSpeed(),
                stats.getLuck(),
                stats.getMaxHp(),
                stats.getCurrentHp(),
                stats.getMaxMp(),
                stats.getCurrentMp(),
                stats.getHeight(),
                stats.getWeight(),
                stats.getLevel(),
                stats.getCurrentExp(),
                stats.getExpForNextLevel()
        );
    }

    public static CharacterInventoryResponseDto mapInventory(Inventory inventory) {
        return new CharacterInventoryResponseDto(
                ArmorInstanceResponseDto.fromInstances(inventory.getArmors()),
                BootsInstanceResponseDto.fromInstances(inventory.getBoots()),
                ConsumableInstanceResponseDto.fromInstances(inventory.getConsumables()),
                HelmetInstanceResponseDto.fromInstances(inventory.getHelmets()),
                ShieldInstanceResponseDto.fromInstances(inventory.getShields()),
                SpellInstanceResponseDto.fromInstances(inventory.getSpells()),
                WeaponInstanceResponseDto.fromInstances(inventory.getWeapons())
        );
    }


}