package com.github.acolote1998.humble_gladiators_2.booster.dto;

import com.github.acolote1998.humble_gladiators_2.booster.model.CharacterBooster;
import com.github.acolote1998.humble_gladiators_2.characters.dto.FullCharacterResponseDto;

import java.util.List;

public record CharacterBoosterResponseDto(List<FullCharacterResponseDto> characters) {

    static public CharacterBoosterResponseDto fromModelToDto(CharacterBooster model) {
        CharacterBoosterResponseDto dto = new CharacterBoosterResponseDto(FullCharacterResponseDto.fromListOfCharInstToListOfCharDto(model.getCharacters()));
        return dto;
    }
}
