package com.github.acolote1998.humble_gladiators_2.characters.dto;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterCategory;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;

import java.util.ArrayList;
import java.util.List;

public record FullCharacterResponseDto(
        Long id,
        HeroResponseDto.CharacterStatsResponseDto stats,
        HeroResponseDto.CharacterInventoryResponseDto inventory,
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
        String imgBase64
) {

    public static FullCharacterResponseDto fromModelToDto(CharacterInstance character) {
        if (character == null) {
            return null;
        }
        FullCharacterResponseDto dto = new FullCharacterResponseDto(
                character.getId(),
                HeroResponseDto.MapCharInstanceStats(character),
                HeroResponseDto.mapInventory(character.getInventory()),
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
                BytesToBase64.bytesToBase64(character.getImgBytes())
        );
        return dto;
    }

    public static List<FullCharacterResponseDto> fromListOfCharInstToListOfCharDto(List<CharacterInstance> characterInstances) {
        List<FullCharacterResponseDto> dtos = new ArrayList<>();
        characterInstances.forEach(characterInstance -> dtos.add(fromModelToDto(characterInstance)));
        return dtos;
    }

}
