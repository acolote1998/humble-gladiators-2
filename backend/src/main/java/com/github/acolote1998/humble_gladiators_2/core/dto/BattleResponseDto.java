package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.github.acolote1998.humble_gladiators_2.characters.dto.FullCharacterResponseDto;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterSnapshot;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import com.github.acolote1998.humble_gladiators_2.core.enums.StateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Action;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.model.Turn;
import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;

import java.util.ArrayList;
import java.util.List;

public record BattleResponseDto(
        Long id,
        Long campaignId,
        List<TurnResponseDto> turns,
        List<CharacterSnapshotResponseDto> startingTeamOne,
        List<CharacterSnapshotResponseDto> startingTeamTwo,
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
                model.getCampaign().getId(),
                TurnResponseDto.fromModelList(model.getTurns()),
                CharacterSnapshotResponseDto.fromListOfCharSnapshotToDto(model.getStartingTeamOne()),
                CharacterSnapshotResponseDto.fromListOfCharSnapshotToDto(model.getStartingTeamTwo()),
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

    public record CharacterSnapshotResponseDto(
            String userId,
            CharacterSnapshotStats stats,
            Long campaignId,
            String imgBase64,
            String name,
            String description
    ) {

        private static CharacterSnapshotResponseDto fromSnapshotToDto(CharacterSnapshot character) {
            if (character == null) {
                return null;
            }
            CharacterSnapshotResponseDto dto = new CharacterSnapshotResponseDto(
                    character.getUserId(),
                    MapCharSnapshotStats(character),
                    character.getCampaign().getId(),
                    BytesToBase64.bytesToBase64(character.getImgBytes()),
                    character.getName(),
                    character.getDescription()

            );
            return dto;
        }

        public record CharacterSnapshotStats(
                Integer maxP,
                Integer currentHp,
                Integer maxMp,
                Integer currentMp) {
        }

        public static CharacterSnapshotStats MapCharSnapshotStats(CharacterSnapshot character) {
            Stats stats = character.getStats();
            return new CharacterSnapshotStats(
                    stats.getMaxHp(),
                    stats.getCurrentHp(),
                    stats.getMaxMp(),
                    stats.getCurrentMp()
            );
        }

        public static List<CharacterSnapshotResponseDto> fromListOfCharSnapshotToDto(List<CharacterSnapshot> characterSnapshots) {
            List<CharacterSnapshotResponseDto> dtos = new ArrayList<>();
            characterSnapshots.forEach(characterSnapshot -> dtos.add(fromSnapshotToDto(characterSnapshot)));
            return dtos;
        }
    }
}
