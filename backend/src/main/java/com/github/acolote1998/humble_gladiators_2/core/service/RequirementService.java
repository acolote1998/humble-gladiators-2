package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryOperator;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryType;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidGeminiEnumException;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.model.RequirementEntry;

import java.util.ArrayList;
import java.util.List;

public class RequirementService {

    public static Requirement mapRequirementFromGeminiItemDto(ItemFromGeminiDto dto, Campaign campaign) {
        Requirement requirement = new Requirement();
        requirement.setCampaign(campaign);
        List<RequirementEntry> requirementEntries = new ArrayList<>();
        if (dto.requirement() != null && !dto.requirement().requirements().isEmpty()) {
            dto.requirement().requirements().forEach(entryDto -> {
                RequirementEntry requirementEntry = new RequirementEntry();
                requirementEntry.setCampaign(campaign);
                requirementEntry.setRequirement(requirement);
                requirementEntry.setValue(entryDto.value());
                requirementEntry.setOperator(parseRequirementOperator(entryDto.operator(), dto.name()));
                requirementEntry.setRequirementType(parseRequirementType(entryDto.requirementType(), dto.name()));
                requirementEntries.add(requirementEntry);
            });
        }
        requirement.setRequirements(requirementEntries);
        return requirement;
    }

    private static RequirementEntryType parseRequirementType(String rawValue, String itemName) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new InvalidGeminiEnumException(buildErrorMessage("RequirementEntryType", rawValue, itemName));
        }
        try {
            return RequirementEntryType.valueOf(rawValue);
        } catch (IllegalArgumentException ex) {
            throw new InvalidGeminiEnumException(buildErrorMessage("RequirementEntryType", rawValue, itemName), ex);
        }
    }

    private static RequirementEntryOperator parseRequirementOperator(String rawValue, String itemName) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new InvalidGeminiEnumException(buildErrorMessage("RequirementEntryOperator", rawValue, itemName));
        }
        try {
            return RequirementEntryOperator.valueOf(rawValue);
        } catch (IllegalArgumentException ex) {
            throw new InvalidGeminiEnumException(buildErrorMessage("RequirementEntryOperator", rawValue, itemName), ex);
        }
    }

    private static String buildErrorMessage(String enumName, String value, String itemName) {
        return String.format("Invalid %s '%s' generated for item '%s'", enumName, value, itemName);
    }
}
