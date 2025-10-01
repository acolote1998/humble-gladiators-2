package com.github.acolote1998.humble_gladiators_2.item.enums;

public enum HelmetCategory {
    HELMET,
    HARD_HAT,
    MILITARY_HELMET,
    VISOR,
    DECORATIVE_HELMET,
    MASK,
    HAT,
    OTHER;

    // Method to get all constants as a comma-separated string
    public static String AllHelmetCategoryToString() {
        StringBuilder sb = new StringBuilder();
        for (HelmetCategory category : HelmetCategory.values()) {
            sb.append(category.name()).append(", ");
        }
        // Remove last comma and space
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
