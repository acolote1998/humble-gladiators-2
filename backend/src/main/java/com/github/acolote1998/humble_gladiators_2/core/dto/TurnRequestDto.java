package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;

public record TurnRequestDto(Long performingCharacterId,
                             Long targetCharacterId,
                             ActionType action,
                             Long cardToUseId) {

}
