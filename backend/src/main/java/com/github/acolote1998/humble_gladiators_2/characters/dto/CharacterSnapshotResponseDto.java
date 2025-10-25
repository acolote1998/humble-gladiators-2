//package com.github.acolote1998.humble_gladiators_2.characters.dto;
//
//import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterCategory;
//import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
//import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
//import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
//import com.github.acolote1998.humble_gladiators_2.core.dto.RequirementResponseDto;
//import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
//import com.github.acolote1998.humble_gladiators_2.item.enums.*;
//import com.github.acolote1998.humble_gladiators_2.item.instances.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public record CharacterSnapshotResponseDto(
//        Long id,
//        CharacterSnapshotStats stats,
//        CharacterSnapshotInventory inventory,
//        CharacterCategory category,
//        CharacterType characterType,
//        String name,
//        String description,
//        Boolean discovered,
//        Long campaignId,
//        Integer rarity,
//        Integer tier,
//        Integer goldReward,
//        Integer expReward,
//        String imgBase64
//
//) {
//
//    public static CharacterSnapshotResponseDto fromModelToSnapshotDto(CharacterInstance character) {
//        if (character == null) {
//            return null;
//        }
//        CharacterSnapshotResponseDto dto = new CharacterSnapshotResponseDto(
//                character.getId(),
//                HeroResponseDto.MapStats(character),
//                HeroResponseDto.mapInventory(character.getInventory()),
//                character.getCategory(),
//                character.getCharacterType(),
//                character.getName(),
//                character.getDescription(),
//                character.getDiscovered(),
//                character.getCampaign().getId(),
//                character.getRarity(),
//                character.getTier(),
//                character.getGoldReward(),
//                character.getExpReward(),
//                BytesToBase64.bytesToBase64(character.getImgBytes())
//        );
//        return dto;
//    }
//
//    public static List<CharacterSnapshotResponseDto> fromListOfCharInstToListOfCharSnapshotDto(List<CharacterInstance> characterInstances) {
//        List<CharacterSnapshotResponseDto> dtos = new ArrayList<>();
//        characterInstances.forEach(characterInstance -> dtos.add(fromModelToSnapshotDto(characterInstance)));
//        return dtos;
//    }
//
//    public record CharacterSnapshotStats(
//            int constitution,
//            int intelligence,
//            int strength,
//            int speed,
//            int luck,
//            int maxHp,
//            int currentHp,
//            int maxMp,
//            int currentMp,
//            int height,
//            int weight,
//            int level,
//            int currentExp,
//            int expForNextLevel,
//            int physicalDefense,
//            int magicalDefense,
//            int physicalDamage,
//            int magicalDamage
//    ) {
//        public static CharacterSnapshotStats MapStats(CharacterInstance character) {
//            Stats stats = character.getStats();
//            return new CharacterSnapshotStats(
//                    stats.getConstitution(),
//                    stats.getIntelligence(),
//                    stats.getStrength(),
//                    stats.getSpeed(),
//                    stats.getLuck(),
//                    stats.getMaxHp(),
//                    stats.getCurrentHp(),
//                    stats.getMaxMp(),
//                    stats.getCurrentMp(),
//                    stats.getHeight(),
//                    stats.getWeight(),
//                    stats.getLevel(),
//                    stats.getCurrentExp(),
//                    stats.getExpForNextLevel(),
//                    character.getPhysicalDefense(),
//                    character.getMagicalDefense(),
//                    character.getPhysicalDamage(),
//                    character.getMagicalDamage()
//            );
//        }
//    }
//
//    private record CharacterSnapshotInventory(List<ArmorInstanceSnapshot> armors,
//                                              List<BootsInstanceSnapshot> boots,
//                                              List<ConsumableInstanceSnapshot> consumables,
//                                              List<HelmetInstanceSnapshot> helmets,
//                                              List<ShieldInstanceSnapshot> shields,
//                                              List<SpellInstanceSnapshot> spells,
//                                              List<WeaponInstanceSnapshot> weapons) {
//        public record ArmorInstanceSnapshot(
//                Long campaignId,
//                Long id,
//                String name,
//                String description,
//                Integer rarity,
//                Integer tier,
//                Integer value,
//                Integer quantity,
//                Boolean equipped,
//                RequirementResponseDto requirement,
//                ArmorCategory category,
//                Integer physicalDefense,
//                Integer magicalDefense,
//                String imgBase64,
//                Boolean discovered
//        ) {
//            public static ArmorInstanceSnapshot fromInstance(ArmorInstance armor) {
//                return new ArmorInstanceSnapshot(
//                        armor.getCampaign().getId(),
//                        armor.getId(),
//                        armor.getName(),
//                        armor.getDescription(),
//                        armor.getRarity(),
//                        armor.getTier(),
//                        armor.getValue(),
//                        armor.getQuantity(),
//                        armor.getEquipped(),
//                        RequirementResponseDto.fromRequirement(armor.getRequirement()),
//                        armor.getTemplate().getCategory(),
//                        armor.getTemplate().getPhysicalDefense(),
//                        armor.getTemplate().getMagicalDefense(),
//                        BytesToBase64.bytesToBase64(armor.getTemplate().getImgBytes()),
//                        armor.getTemplate().getDiscovered()
//                );
//            }
//
//            public static List<ArmorInstanceSnapshot> fromInstances(List<ArmorInstance> armors) {
//                if (armors == null) return List.of();
//                return armors.stream()
//                        .map(armor -> fromInstance(armor))
//                        .toList();
//            }
//        }
//
//        public record BootsInstanceSnapshot(Long campaignId,
//                                            Long id,
//                                            String name,
//                                            String description,
//                                            Integer rarity,
//                                            Integer tier,
//                                            Integer value,
//                                            Integer quantity,
//                                            Boolean equipped,
//                                            RequirementResponseDto requirement,
//                                            BootsCategory category,
//                                            Integer physicalDefense,
//                                            Integer magicalDefense,
//                                            String imgBase64,
//                                            Boolean discovered) {
//            public static BootsInstanceSnapshot fromInstance(BootsInstance boots) {
//                return new BootsInstanceSnapshot(
//                        boots.getCampaign().getId(),
//                        boots.getId(),
//                        boots.getName(),
//                        boots.getDescription(),
//                        boots.getRarity(),
//                        boots.getTier(),
//                        boots.getValue(),
//                        boots.getQuantity(),
//                        boots.getEquipped(),
//                        RequirementResponseDto.fromRequirement(boots.getRequirement()),
//                        boots.getTemplate().getCategory(),
//                        boots.getTemplate().getPhysicalDefense(),
//                        boots.getTemplate().getMagicalDefense(),
//                        BytesToBase64.bytesToBase64(boots.getTemplate().getImgBytes()),
//                        boots.getTemplate().getDiscovered()
//                );
//            }
//
//            public static List<BootsInstanceSnapshot> fromInstances(List<BootsInstance> boots) {
//                if (boots == null)
//                    return List.of();
//                return boots.stream()
//                        .map(boot -> fromInstance(boot))
//                        .toList();
//            }
//        }
//
//        public record ConsumableInstanceSnapshot(
//                Long campaignId,
//                Long id,
//                String name,
//                String description,
//                Integer rarity,
//                Integer tier,
//                Integer value,
//                Integer quantity,
//                Boolean equipped,
//                RequirementResponseDto requirement,
//                ConsumablesCategory category,
//                Integer restoreHp,
//                Integer restoreMp,
//                Boolean discovered,
//                String imgBase64
//        ) {
//
//            public static List<ConsumableInstanceSnapshot> fromInstances(List<ConsumableInstance> consumables) {
//                if (consumables == null) return List.of();
//                return consumables.stream()
//                        .map(consumable -> new ConsumableInstanceSnapshot(
//                                consumable.getCampaign().getId(),
//                                consumable.getId(),
//                                consumable.getName(),
//                                consumable.getDescription(),
//                                consumable.getRarity(),
//                                consumable.getTier(),
//                                consumable.getValue(),
//                                consumable.getQuantity(),
//                                consumable.getEquipped(),
//                                RequirementResponseDto.fromRequirement(consumable.getRequirement()),
//                                consumable.getTemplate().getCategory(),
//                                consumable.getTemplate().getRestoreHp(),
//                                consumable.getTemplate().getRestoreMp(),
//                                consumable.getTemplate().getDiscovered(),
//                                BytesToBase64.bytesToBase64(consumable.getTemplate().getImgBytes())
//                        ))
//                        .toList();
//            }
//        }
//
//        public record HelmetInstanceSnapshot(
//                Long campaignId,
//                Long id,
//                String name,
//                String description,
//                Integer rarity,
//                Integer tier,
//                Integer value,
//                Integer quantity,
//                Boolean equipped,
//                RequirementResponseDto requirement,
//                HelmetCategory category,
//                Integer physicalDefense,
//                Integer magicalDefense,
//                String imgBase64,
//                Boolean discovered) {
//
//            public static HelmetInstanceSnapshot fromInstance(HelmetInstance helmet) {
//                return new HelmetInstanceSnapshot(
//                        helmet.getCampaign().getId(),
//                        helmet.getId(),
//                        helmet.getName(),
//                        helmet.getDescription(),
//                        helmet.getRarity(),
//                        helmet.getTier(),
//                        helmet.getValue(),
//                        helmet.getQuantity(),
//                        helmet.getEquipped(),
//                        RequirementResponseDto.fromRequirement(helmet.getRequirement()),
//                        helmet.getTemplate().getCategory(),
//                        helmet.getTemplate().getPhysicalDefense(),
//                        helmet.getTemplate().getMagicalDefense(),
//                        BytesToBase64.bytesToBase64(helmet.getTemplate().getImgBytes()),
//                        helmet.getTemplate().getDiscovered()
//                );
//            }
//
//            public static List<HelmetInstanceSnapshot> fromInstances(List<HelmetInstance> helmets) {
//                if (helmets == null)
//                    return List.of();
//                return helmets.stream()
//                        .map(helmet -> fromInstance(helmet))
//                        .toList();
//            }
//        }
//
//        public record ShieldInstanceSnapshot(
//                Long campaignId,
//                Long id,
//                String name,
//                String description,
//                Integer rarity,
//                Integer tier,
//                Integer value,
//                Integer quantity,
//                Boolean equipped,
//                RequirementResponseDto requirement,
//                ShieldCategory category,
//                Integer physicalDefense,
//                Integer magicalDefense,
//                String imgBase64,
//                Boolean discovered) {
//
//            public static ShieldInstanceSnapshot fromInstance(ShieldInstance shield) {
//                return new ShieldInstanceSnapshot(
//                        shield.getCampaign().getId(),
//                        shield.getId(),
//                        shield.getName(),
//                        shield.getDescription(),
//                        shield.getRarity(),
//                        shield.getTier(),
//                        shield.getValue(),
//                        shield.getQuantity(),
//                        shield.getEquipped(),
//                        RequirementResponseDto.fromRequirement(shield.getRequirement()),
//                        shield.getTemplate().getCategory(),
//                        shield.getTemplate().getPhysicalDefense(),
//                        shield.getTemplate().getMagicalDefense(),
//                        BytesToBase64.bytesToBase64(shield.getTemplate().getImgBytes()),
//                        shield.getTemplate().getDiscovered()
//                );
//            }
//
//            public static List<ShieldInstanceSnapshot> fromInstances(List<ShieldInstance> shields) {
//                if (shields == null)
//                    return List.of();
//                return shields.stream()
//                        .map(shield -> fromInstance(shield))
//                        .toList();
//            }
//        }
//
//        public record SpellInstanceSnapshot(
//                Long campaignId,
//                Long id,
//                String name,
//                String description,
//                Integer rarity,
//                Integer tier,
//                Integer value,
//                Integer quantity,
//                Boolean equipped,
//                RequirementResponseDto requirement,
//                SpellCategory category,
//                Integer physicalDamage,
//                Integer magicalDamage,
//                Integer restoreHp,
//                Boolean discovered,
//                String imgBase64
//        ) {
//
//            public static List<SpellInstanceSnapshot> fromInstances(List<SpellInstance> spells) {
//                if (spells == null) return List.of();
//                return spells.stream()
//                        .map(spell -> new SpellInstanceSnapshot(
//                                spell.getCampaign().getId(),
//                                spell.getId(),
//                                spell.getName(),
//                                spell.getDescription(),
//                                spell.getRarity(),
//                                spell.getTier(),
//                                spell.getValue(),
//                                spell.getQuantity(),
//                                spell.getEquipped(),
//                                RequirementResponseDto.fromRequirement(spell.getRequirement()),
//                                spell.getTemplate().getCategory(),
//                                spell.getTemplate().getPhysicalDamage(),
//                                spell.getTemplate().getMagicalDamage(),
//                                spell.getTemplate().getRestoreHp(),
//                                spell.getTemplate().getDiscovered(),
//                                BytesToBase64.bytesToBase64(spell.getTemplate().getImgBytes())
//                        ))
//                        .toList();
//            }
//        }
//
//        public record WeaponInstanceSnapshot(
//                Long campaignId,
//                Long id,
//                String name,
//                String description,
//                Integer rarity,
//                Integer tier,
//                Integer value,
//                Integer quantity,
//                Boolean equipped,
//                RequirementResponseDto requirement,
//                WeaponCategory category,
//                Integer physicalDamage,
//                Integer magicalDamage,
//                String imgBase64,
//                Boolean discovered) {
//
//            public static WeaponInstanceSnapshot fromInstance(WeaponInstance weapon) {
//                return new WeaponInstanceSnapshot(
//                        weapon.getCampaign().getId(),
//                        weapon.getId(),
//                        weapon.getName(),
//                        weapon.getDescription(),
//                        weapon.getRarity(),
//                        weapon.getTier(),
//                        weapon.getValue(),
//                        weapon.getQuantity(),
//                        weapon.getEquipped(),
//                        RequirementResponseDto.fromRequirement(weapon.getRequirement()),
//                        weapon.getTemplate().getCategory(),
//                        weapon.getTemplate().getPhysicalDamage(),
//                        weapon.getTemplate().getMagicalDamage(),
//                        BytesToBase64.bytesToBase64(weapon.getTemplate().getImgBytes()),
//                        weapon.getTemplate().getDiscovered());
//            }
//
//            public static List<WeaponInstanceSnapshot> fromInstances(List<WeaponInstance> weapons) {
//                if (weapons == null)
//                    return List.of();
//                return weapons.stream()
//                        .map(weapon -> fromInstance(weapon))
//                        .toList();
//            }
//        }
//    }
//}
