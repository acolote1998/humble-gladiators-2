package com.github.acolote1998.humble_gladiators_2.core.dto;

import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;

import java.util.ArrayList;
import java.util.List;

public record CampaignResponseDto(Long id, String name, ThemeResponseDto theme,
                                  CampaignCreationStateType campaignCreationState, String coverImgBase64,
                                  String cardBackImgBase64) {
    private record ThemeResponseDto(List<String> wantedThemes, List<String> unwantedThemes) {
    }

    public static CampaignResponseDto fromEntityToCampaignResponseDto(
            Campaign campaignToMap
    ) {
        return new CampaignResponseDto(
                campaignToMap.getId(),
                campaignToMap.getName(),
                new ThemeResponseDto(campaignToMap.getTheme().getWantedThemes(), campaignToMap.getTheme().getUnwantedThemes()),
                campaignToMap.getCampaignCreationState(),
                BytesToBase64.bytesToBase64(campaignToMap.getCoverImgBytes()),
                BytesToBase64.bytesToBase64(campaignToMap.getCardBackImgBytes()));
    }

    public static List<CampaignResponseDto> mapCampaignEntityToResponseDtos(List<Campaign> campaigns) {
        List<CampaignResponseDto> responseDtos = new ArrayList<>();
        campaigns.forEach(campaign -> responseDtos.add(fromEntityToCampaignResponseDto(campaign)));
        return responseDtos;
    }
}
