package com.github.acolote1998.humble_gladiators_2.item.enums;

public enum ShieldCategory {
    SHIELD,
    BOOK,
    AMULET,
    RING,
    BADGE,
    TROPHY,
    TOOL,
    MISCELLANEOUS,
    BAG,
    STATIONERY,
    SPELLBOOK,
    OTHER;

    // Method to get all constants as a comma-separated string
    public static String AllShieldCategoryToString() {
        StringBuilder sb = new StringBuilder();
        for (ShieldCategory category : ShieldCategory.values()) {
            sb.append(category.name()).append(", ");
        }
        // Remove last comma and space
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
