package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryOperator;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryType;
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
                requirementEntry.setOperator(RequirementEntryOperator.valueOf(entryDto.operator()));
                requirementEntry.setRequirementType(RequirementEntryType.valueOf(entryDto.requirementType()));
                requirementEntries.add(requirementEntry);
            });
        }
        requirement.setRequirements(requirementEntries);
        return requirement;
    }
}
