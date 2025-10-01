package com.github.acolote1998.humble_gladiators_2.item.enums;

public enum ArmorCategory {
    ROBE,
    PLATE,
    MAIL,
    SHIRT,
    CAPE,
    CLOAK,
    BACKPACK,
    OTHER;

    // Method to get all constants as a comma-separated string
    public static String AllArmorCategoryToString() {
        StringBuilder sb = new StringBuilder();
        for (ArmorCategory category : ArmorCategory.values()) {
            sb.append(category.name()).append(", ");
        }
        // Remove last comma and space
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
