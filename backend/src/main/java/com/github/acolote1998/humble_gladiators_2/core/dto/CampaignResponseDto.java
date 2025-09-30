package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;

import java.util.List;

public record CampaignResponseDto(Long id, String name, ThemeResponseDto theme,
                                  CampaignCreationStateType campaignCreationState) {
    private record ThemeResponseDto(List<String> wantedThemes, List<String> unwantedThemes) {
    }

    public static CampaignResponseDto fromEntityToCampaignResponseDto(
            Campaign campaignToMap
    ) {
        return new CampaignResponseDto(
                campaignToMap.getId(),
                campaignToMap.getName(),
                new ThemeResponseDto(campaignToMap.getTheme().getWantedThemes(), campaignToMap.getTheme().getUnwantedThemes()),
                campaignToMap.getCampaignCreationState());
    }
}
