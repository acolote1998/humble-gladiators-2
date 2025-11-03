package com.github.acolote1998.humble_gladiators_2.characters.dto;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterCategory;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.dto.*;

import java.util.ArrayList;
import java.util.List;

public record FullCharacterResponseDto(
        Long id,
        CharacterStatsResponseDto stats,
        CharacterInventoryResponseDto inventory,
        CharacterCategory category,
        CharacterType characterType,
        String name,
        String description,
        Boolean discovered,
        Long campaignId,
        Integer rarity,
        Integer tier,
        Integer goldReward,
        Integer expReward,
        String imgBase64,
        String backgroundImgBase64
) {
    public record CharacterStatsResponseDto(
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
            int expForNextLevel,
            int physicalDefense,
            int magicalDefense,
            int physicalDamage,
            int magicalDamage
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

    public static CharacterStatsResponseDto MapCharInstanceStats(CharacterInstance character) {
        Stats stats = character.getStats();
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
                stats.getExpForNextLevel(),
                character.getPhysicalDefense(),
                character.getMagicalDefense(),
                character.getPhysicalDamage(),
                character.getMagicalDamage()
        );
    }


    public static CharacterInventoryResponseDto MapInventory(Inventory inventory) {
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

    public static FullCharacterResponseDto fromModelToDto(CharacterInstance character) {
        if (character == null) {
            return null;
        }
        FullCharacterResponseDto dto = new FullCharacterResponseDto(
                character.getId(),
                MapCharInstanceStats(character),
                MapInventory(character.getInventory()),
                character.getCategory(),
                character.getCharacterType(),
                character.getName(),
                character.getDescription(),
                character.getDiscovered(),
                character.getCampaign().getId(),
                character.getRarity(),
                character.getTier(),
                character.getGoldReward(),
                character.getExpReward(),
                BytesToBase64.bytesToBase64(character.getImgBytes()),
                BytesToBase64.bytesToBase64(character.getBackgroundImgBytes())
        );
        return dto;
    }

    public static List<FullCharacterResponseDto> fromListOfCharInstToListOfCharDto(List<CharacterInstance> characterInstances) {
        List<FullCharacterResponseDto> dtos = new ArrayList<>();
        characterInstances.forEach(characterInstance -> dtos.add(fromModelToDto(characterInstance)));
        return dtos;
    }

}
