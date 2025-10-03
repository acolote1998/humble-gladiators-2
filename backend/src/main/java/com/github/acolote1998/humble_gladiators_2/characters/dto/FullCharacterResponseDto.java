package com.github.acolote1998.humble_gladiators_2.characters.dto;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterCategory;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;

import java.util.ArrayList;
import java.util.List;

public record FullCharacterResponseDto(
        CharacterStatsResponseDto stats,
        CharacterCategory category,
        CharacterType characterType,
        String name,
        String description,
        Boolean discovered,
        Long campaignId,
        Integer rarity,
        Integer tier,
        Integer goldReward,
        Integer expReward
) {

    private static FullCharacterResponseDto fromModelToDto(CharacterInstance character) {
        FullCharacterResponseDto dto = new FullCharacterResponseDto(
                new CharacterStatsResponseDto(
                        character.getStats().getConstitution(),
                        character.getStats().getIntelligence(),
                        character.getStats().getStrength(),
                        character.getStats().getSpeed(),
                        character.getStats().getLuck(),
                        character.getStats().getMaxHp(),
                        character.getStats().getCurrentHp(),
                        character.getStats().getMaxMp(),
                        character.getStats().getCurrentMp(),
                        character.getStats().getHeight(),
                        character.getStats().getWeight(),
                        character.getStats().getLevel(),
                        character.getStats().getCurrentExp(),
                        character.getStats().getExpForNextLevel()
                ),
                character.getCategory(),
                character.getCharacterType(),
                character.getName(),
                character.getDescription(),
                character.getDiscovered(),
                character.getCampaign().getId(),
                character.getRarity(),
                character.getTier(),
                character.getGoldReward(),
                character.getExpReward()
        );
        return dto;
    }

    public static List<FullCharacterResponseDto> fromListOfCharInstToListOfCharDto(List<CharacterInstance> characterInstances) {
        List<FullCharacterResponseDto> dtos = new ArrayList<>();
        characterInstances.forEach(characterInstance -> dtos.add(fromModelToDto(characterInstance)));
        return dtos;
    }

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
}
