package com.github.acolote1998.humble_gladiators_2.item.enums;

public enum BootsCategory {
    BOOTS,
    COMBAT_BOOTS,
    SNEAKERS,
    LOAFERS,
    SLIPPERS,
    SANDALS,
    HEELS,
    MOCCASINS,
    CLOGS,
    PLATFORMS,
    OTHER;

    // Method to get all constants as a comma-separated string
    public static String AllBootsCategoryToString() {
        StringBuilder sb = new StringBuilder();
        for (BootsCategory category : BootsCategory.values()) {
            sb.append(category.name()).append(", ");
        }
        // Remove last comma and space
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
