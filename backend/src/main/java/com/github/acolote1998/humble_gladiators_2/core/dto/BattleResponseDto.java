package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.github.acolote1998.humble_gladiators_2.characters.dto.FullCharacterResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import com.github.acolote1998.humble_gladiators_2.core.enums.StateType;

import java.util.List;

public record BattleResponseDto(
        Long id,
        List<TurnResponseDto> turns,
        List<FullCharacterResponseDto> startingTeamOne,
        List<FullCharacterResponseDto> startingTeamTwo,
        List<FullCharacterResponseDto> teamOne,
        List<FullCharacterResponseDto> teamTwo,
        List<FullCharacterResponseDto> winningTeam,
        List<FullCharacterResponseDto> losingTeam,
        FullCharacterResponseDto currentCharacterToPlay,
        boolean onGoing

) {
    public record TurnResponseDto(
            FullCharacterResponseDto performingCharacter,
            FullCharacterResponseDto targetCharacter,
            ActionResponseDto action
    ) {
        public record ActionResponseDto(
                Integer damageCaused,
                Integer healingCaused,
                ActionType actionType,
                StateType stateCaused
        ) {
        }
    }
}
