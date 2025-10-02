package com.github.acolote1998.humble_gladiators_2.item.enums;

public enum WeaponCategory {
    SWORD,
    AXE,
    MACE,
    DAGGER,
    SPEAR,
    STAFF,
    CLUB,
    HAMMER,
    BOW,
    GUN,
    WAND,
    MAGIC_ORB,
    ENCHANTED_GLOVE,
    TOOL,
    WHIP,
    THROWABLE,
    FOOD,
    OTHER;

    // Method to get all constants as a comma-separated string
    public static String AllWeaponCategoryToString() {
        StringBuilder sb = new StringBuilder();
        for (WeaponCategory category : WeaponCategory.values()) {
            sb.append(category.name()).append(", ");
        }
        // Remove last comma and space
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
