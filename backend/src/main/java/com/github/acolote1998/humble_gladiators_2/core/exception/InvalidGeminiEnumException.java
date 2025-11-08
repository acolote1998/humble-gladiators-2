package com.github.acolote1998.humble_gladiators_2.core.exception;

public class InvalidGeminiEnumException extends RuntimeException {
    public InvalidGeminiEnumException(String message) {
        super(message);
    }

    public InvalidGeminiEnumException(String message, Throwable cause) {
        super(message, cause);
    }
}

