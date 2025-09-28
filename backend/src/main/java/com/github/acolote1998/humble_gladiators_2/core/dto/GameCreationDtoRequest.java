package com.github.acolote1998.humble_gladiators_2.core.dto;

import java.util.List;

public record GameCreationDtoRequest(
        String campaignName,
        ThemeDtoRequest theme
) {
    public record ThemeDtoRequest(
            List<String> wantedThemesDto,
            List<String> unwantedThemesDto
    ) {
    }
}
