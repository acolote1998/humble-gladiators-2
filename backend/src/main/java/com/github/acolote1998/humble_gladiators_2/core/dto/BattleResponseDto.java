package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.github.acolote1998.humble_gladiators_2.characters.dto.FullCharacterResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import com.github.acolote1998.humble_gladiators_2.core.enums.StateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Action;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.model.Turn;

import java.util.ArrayList;
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
    public static BattleResponseDto fromModel(Battle model) {
        return new BattleResponseDto(
                model.getId(),
                TurnResponseDto.fromModelList(model.getTurns()),
                null,
                null,
                FullCharacterResponseDto.fromListOfCharInstToListOfCharDto(model.getTeamOne()),
                FullCharacterResponseDto.fromListOfCharInstToListOfCharDto(model.getTeamTwo()),
                FullCharacterResponseDto.fromListOfCharInstToListOfCharDto(model.getWinningTeam()),
                FullCharacterResponseDto.fromListOfCharInstToListOfCharDto(model.getLosingTeam()),
                FullCharacterResponseDto.fromModelToDto(model.getCurrentCharacterToPlay()),
                model.isOngoing()
        );
    }

    public record TurnResponseDto(
            FullCharacterResponseDto performingCharacter,
            FullCharacterResponseDto targetCharacter,
            ActionResponseDto action
    ) {
        public static TurnResponseDto fromModel(Turn model) {
            return new TurnResponseDto(
                    FullCharacterResponseDto.fromModelToDto(model.getPerformingCharacter()),
                    FullCharacterResponseDto.fromModelToDto(model.getTargetCharacter()),
                    ActionResponseDto.fromModel(model.getAction())
            );
        }

        public static List<TurnResponseDto> fromModelList(List<Turn> modelList) {
            List<TurnResponseDto> response = new ArrayList<>();
            modelList.forEach(turn -> response.add(fromModel(turn)));
            return response;
        }

        public record ActionResponseDto(
                Integer damageCaused,
                Integer healingCaused,
                ActionType actionType,
                StateType stateCaused
        ) {
            public static ActionResponseDto fromModel(Action model) {
                return new ActionResponseDto(
                        model.getDamageCaused(),
                        model.getHealingCaused(),
                        model.getActionType(),
                        model.getStateCaused()
                );
            }
        }
    }
}
