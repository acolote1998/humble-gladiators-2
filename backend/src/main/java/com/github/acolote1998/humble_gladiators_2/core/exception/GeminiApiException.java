package com.github.acolote1998.humble_gladiators_2.core.exception;

import com.github.acolote1998.humble_gladiators_2.core.dto.ResponseGeminiApiError;
import lombok.Getter;

@Getter
public class GeminiApiException extends RuntimeException {
    private final ResponseGeminiApiError responseGeminiApiError;

    public GeminiApiException(String message) {
        super(message);
        this.responseGeminiApiError = new ResponseGeminiApiError(message, true);
    }

    public GeminiApiException(String message, Throwable cause) {
        super(message, cause);
        this.responseGeminiApiError = new ResponseGeminiApiError(message, true);
    }
}

