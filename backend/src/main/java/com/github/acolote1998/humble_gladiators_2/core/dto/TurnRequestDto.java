package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import jakarta.validation.constraints.NotNull;

public record TurnRequestDto(@NotNull Long performingCharacterId,
                             @NotNull Long targetCharacterId,
                             @NotNull ActionType action,
                             Long cardToUseId) {

}
