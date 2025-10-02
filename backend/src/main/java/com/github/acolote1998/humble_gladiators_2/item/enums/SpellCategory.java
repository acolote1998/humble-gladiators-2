package com.github.acolote1998.humble_gladiators_2.item.enums;

public enum SpellCategory {
    FIRE_SPELL,
    ICE_SPELL,
    EARTH_SPELL,
    WATER_SPELL,
    AIR_SPELL,
    LIGHTNING_SPELL,
    HEALING_SPELL,
    BUFF_SPELL,
    DEBUFF_SPELL,
    SUMMON_SPELL,
    GENERAL_SPELL,
    OTHER;

    // Method to get all constants as a comma-separated string
    public static String AllSpellCategoryToString() {
        StringBuilder sb = new StringBuilder();
        for (SpellCategory category : SpellCategory.values()) {
            sb.append(category.name()).append(", ");
        }
        // Remove last comma and space
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
