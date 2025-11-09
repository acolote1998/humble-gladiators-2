package com.github.acolote1998.humble_gladiators_2.core.util;

import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidGeminiEnumException;

public final class GeminiEnumParser {

    private GeminiEnumParser() {
    }

    public static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String rawValue, String contextName, String sourceName) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new InvalidGeminiEnumException(buildErrorMessage(enumClass.getSimpleName(), rawValue, contextName, sourceName));
        }
        try {
            return Enum.valueOf(enumClass, rawValue);
        } catch (IllegalArgumentException ex) {
            throw new InvalidGeminiEnumException(buildErrorMessage(enumClass.getSimpleName(), rawValue, contextName, sourceName), ex);
        }
    }

    private static String buildErrorMessage(String enumName, String value, String contextName, String sourceName) {
        return String.format("Invalid %s '%s' generated for %s '%s'", enumName, value, contextName, sourceName);
    }
}

