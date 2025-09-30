package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;

import java.util.ArrayList;
import java.util.List;

public record CampaignResponseDto(Long id, String name, ThemeResponseDto theme,
                                  CampaignCreationStateType campaignCreationState) {
    private record ThemeResponseDto(List<String> wantedThemes, List<String> unwantedThemes) {
    }

    private static CampaignResponseDto fromEntityToCampaignResponseDto(
            Campaign campaignToMap
    ) {
        return new CampaignResponseDto(
                campaignToMap.getId(),
                campaignToMap.getName(),
                new ThemeResponseDto(campaignToMap.getTheme().getWantedThemes(), campaignToMap.getTheme().getUnwantedThemes()),
                campaignToMap.getCampaignCreationState());
    }

    public static List<CampaignResponseDto> mapCampaignEntityToResponseDtos(List<Campaign> campaigns) {
        List<CampaignResponseDto> responseDtos = new ArrayList<>();
        campaigns.forEach(campaign -> responseDtos.add(fromEntityToCampaignResponseDto(campaign)));
        return responseDtos;
    }
}
