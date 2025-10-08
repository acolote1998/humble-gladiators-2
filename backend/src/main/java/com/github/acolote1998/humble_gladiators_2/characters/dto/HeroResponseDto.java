package com.github.acolote1998.humble_gladiators_2.characters.dto;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryOperator;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryType;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import com.github.acolote1998.humble_gladiators_2.item.dto.ArmorInstanceResponseDto;
import com.github.acolote1998.humble_gladiators_2.item.enums.*;
import com.github.acolote1998.humble_gladiators_2.item.instances.*;

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

        public record BootsInstanceResponseDto(
                String name,
                String description,
                Integer rarity,
                Integer tier,
                Integer value,
                Integer quantity,
                Boolean equipped,
                RequirementResponseDto requirement,
                BootsCategory category,
                Integer physicalDefense,
                Integer magicalDefense
        ) {

        }

        private record ConsumableInstanceResponseDto(
                String name,
                String description,
                Integer rarity,
                Integer tier,
                Integer value,
                Integer quantity,
                Boolean equipped,
                RequirementResponseDto requirement,
                ConsumablesCategory category,
                Integer restoreHp,
                Integer restoreMp
        ) {

        }

        private record HelmetInstanceResponseDto(
                String name,
                String description,
                Integer rarity,
                Integer tier,
                Integer value,
                Integer quantity,
                Boolean equipped,
                RequirementResponseDto requirement,
                HelmetCategory category,
                Integer physicalDefense,
                Integer magicalDefense
        ) {

        }

        private record ShieldInstanceResponseDto(
                String name,
                String description,
                Integer rarity,
                Integer tier,
                Integer value,
                Integer quantity,
                Boolean equipped,
                RequirementResponseDto requirement,
                ShieldCategory category,
                Integer physicalDefense,
                Integer magicalDefense
        ) {

        }

        private record SpellInstanceResponseDto(
                String name,
                String description,
                Integer rarity,
                Integer tier,
                Integer value,
                Integer quantity,
                Boolean equipped,
                RequirementResponseDto requirement,
                SpellCategory category,
                Integer physicalDamage,
                Integer magicalDamage,
                Integer restoreHp
        ) {

        }

        private record WeaponInstanceResponseDto(
                String name,
                String description,
                Integer rarity,
                Integer tier,
                Integer value,
                Integer quantity,
                Boolean equipped,
                RequirementResponseDto requirement,
                WeaponCategory category,
                Integer physicalDamage,
                Integer magicalDamage
        ) {

        }

        public record RequirementResponseDto(List<RequirementEntryResponseDto> requirements) {

            private record RequirementEntryResponseDto(
                    RequirementEntryType requirementType,
                    RequirementEntryOperator operator,
                    String value
            ) {
            }
        }
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
                mapArmorInstances(inventory.getArmors()),
                mapBootsInstances(inventory.getBoots()),
                mapConsumableInstances(inventory.getConsumables()),
                mapHelmetInstances(inventory.getHelmets()),
                mapShieldInstances(inventory.getShields()),
                mapSpellInstances(inventory.getSpells()),
                mapWeaponInstances(inventory.getWeapons())
        );
    }

    private static List<ArmorInstanceResponseDto> mapArmorInstances(List<ArmorInstance> armors) {
        if (armors == null) return List.of();
        return armors.stream()
                .map(armor -> new ArmorInstanceResponseDto(
                        armor.getName(),
                        armor.getDescription(),
                        armor.getRarity(),
                        armor.getTier(),
                        armor.getValue(),
                        armor.getQuantity(),
                        armor.getEquipped(),
                        mapRequirement(armor.getRequirement()),
                        armor.getTemplate().getCategory(),
                        armor.getTemplate().getPhysicalDefense(),
                        armor.getTemplate().getMagicalDefense(),
                        BytesToBase64.bytesToBase64(armor.getImgBytes())
                ))
                .toList();
    }

    private static List<CharacterInventoryResponseDto.BootsInstanceResponseDto> mapBootsInstances(List<BootsInstance> boots) {
        if (boots == null) return List.of();
        return boots.stream()
                .map(boot -> new CharacterInventoryResponseDto.BootsInstanceResponseDto(
                        boot.getName(),
                        boot.getDescription(),
                        boot.getRarity(),
                        boot.getTier(),
                        boot.getValue(),
                        boot.getQuantity(),
                        boot.getEquipped(),
                        mapRequirement(boot.getRequirement()),
                        boot.getTemplate().getCategory(),
                        boot.getTemplate().getPhysicalDefense(),
                        boot.getTemplate().getMagicalDefense()
                ))
                .toList();
    }

    private static List<CharacterInventoryResponseDto.ConsumableInstanceResponseDto> mapConsumableInstances(List<ConsumableInstance> consumables) {
        if (consumables == null) return List.of();
        return consumables.stream()
                .map(consumable -> new CharacterInventoryResponseDto.ConsumableInstanceResponseDto(
                        consumable.getName(),
                        consumable.getDescription(),
                        consumable.getRarity(),
                        consumable.getTier(),
                        consumable.getValue(),
                        consumable.getQuantity(),
                        consumable.getEquipped(),
                        mapRequirement(consumable.getRequirement()),
                        consumable.getTemplate().getCategory(),
                        consumable.getTemplate().getRestoreHp(),
                        consumable.getTemplate().getRestoreMp()
                ))
                .toList();
    }

    private static List<CharacterInventoryResponseDto.HelmetInstanceResponseDto> mapHelmetInstances(List<HelmetInstance> helmets) {
        if (helmets == null) return List.of();
        return helmets.stream()
                .map(helmet -> new CharacterInventoryResponseDto.HelmetInstanceResponseDto(
                        helmet.getName(),
                        helmet.getDescription(),
                        helmet.getRarity(),
                        helmet.getTier(),
                        helmet.getValue(),
                        helmet.getQuantity(),
                        helmet.getEquipped(),
                        mapRequirement(helmet.getRequirement()),
                        helmet.getTemplate().getCategory(),
                        helmet.getTemplate().getPhysicalDefense(),
                        helmet.getTemplate().getMagicalDefense()
                ))
                .toList();
    }

    private static List<CharacterInventoryResponseDto.ShieldInstanceResponseDto> mapShieldInstances(List<ShieldInstance> shields) {
        if (shields == null) return List.of();
        return shields.stream()
                .map(shield -> new CharacterInventoryResponseDto.ShieldInstanceResponseDto(
                        shield.getName(),
                        shield.getDescription(),
                        shield.getRarity(),
                        shield.getTier(),
                        shield.getValue(),
                        shield.getQuantity(),
                        shield.getEquipped(),
                        mapRequirement(shield.getRequirement()),
                        shield.getTemplate().getCategory(),
                        shield.getTemplate().getPhysicalDefense(),
                        shield.getTemplate().getMagicalDefense()
                ))
                .toList();
    }

    private static List<CharacterInventoryResponseDto.SpellInstanceResponseDto> mapSpellInstances(List<SpellInstance> spells) {
        if (spells == null) return List.of();
        return spells.stream()
                .map(spell -> new CharacterInventoryResponseDto.SpellInstanceResponseDto(
                        spell.getName(),
                        spell.getDescription(),
                        spell.getRarity(),
                        spell.getTier(),
                        spell.getValue(),
                        spell.getQuantity(),
                        spell.getEquipped(),
                        mapRequirement(spell.getRequirement()),
                        spell.getTemplate().getCategory(),
                        spell.getTemplate().getPhysicalDamage(),
                        spell.getTemplate().getMagicalDamage(),
                        spell.getTemplate().getRestoreHp()
                ))
                .toList();
    }

    private static List<CharacterInventoryResponseDto.WeaponInstanceResponseDto> mapWeaponInstances(List<WeaponInstance> weapons) {
        if (weapons == null) return List.of();
        return weapons.stream()
                .map(weapon -> new CharacterInventoryResponseDto.WeaponInstanceResponseDto(
                        weapon.getName(),
                        weapon.getDescription(),
                        weapon.getRarity(),
                        weapon.getTier(),
                        weapon.getValue(),
                        weapon.getQuantity(),
                        weapon.getEquipped(),
                        mapRequirement(weapon.getRequirement()),
                        weapon.getTemplate().getCategory(),
                        weapon.getTemplate().getPhysicalDamage(),
                        weapon.getTemplate().getMagicalDamage()
                ))
                .toList();
    }

    private static CharacterInventoryResponseDto.RequirementResponseDto mapRequirement(Requirement requirement) {
        if (requirement == null || requirement.getRequirements() == null) {
            return new CharacterInventoryResponseDto.RequirementResponseDto(List.of());
        }
        return new CharacterInventoryResponseDto.RequirementResponseDto(
                requirement.getRequirements().stream()
                        .map(entry -> new CharacterInventoryResponseDto.RequirementResponseDto.RequirementEntryResponseDto(
                                entry.getRequirementType(),
                                entry.getOperator(),
                                entry.getValue()
                        ))
                        .toList()
        );
    }
}