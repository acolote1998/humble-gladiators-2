package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryOperator;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryType;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;

import java.util.List;

public record RequirementResponseDto(List<RequirementEntryResponseDto> requirements) {

    public record RequirementEntryResponseDto(
            RequirementEntryType requirementType,
            RequirementEntryOperator operator,
            String value
    ) {
    }

    public static RequirementResponseDto fromRequirement(Requirement requirement) {
        if (requirement == null || requirement.getRequirements() == null) {
            return new RequirementResponseDto(List.of());
        }
        return new RequirementResponseDto(
                requirement.getRequirements().stream()
                        .map(entry -> new RequirementEntryResponseDto(
                                entry.getRequirementType(),
                                entry.getOperator(),
                                entry.getValue()
                        ))
                        .toList()
        );
    }
}