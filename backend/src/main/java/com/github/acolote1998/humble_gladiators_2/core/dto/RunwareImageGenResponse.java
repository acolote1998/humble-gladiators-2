package com.github.acolote1998.humble_gladiators_2.core.dto;

import java.util.List;

public record RunwareImageGenResponse(
        List<ResultImageData> data
) {

    public record ResultImageData(
            String taskType,
            String imageUUID,
            String taskUUID,
            Number cost,
            Number seed,
            String imageURL
    ) {
    }
}
