package com.github.acolote1998.humble_gladiators_2.characters.enums;

import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryOperator;

public enum CharacterCategory {
    HUMANOID,
    BEAST,
    MONSTER,
    CONSTRUCT,
    SPIRIT,
    UNDEAD,
    ELEMENTAL,
    MYTHIC,
    CELESTIAL,
    FIEND,
    ABERRATION,
    OTHER;

    // Method to get all constants as a comma-separated string
    public static String AllCharacterCategoryToString() {
        StringBuilder sb = new StringBuilder();
        for (CharacterCategory category : CharacterCategory.values()) {
            sb.append(category.name()).append(", ");
        }
        // Remove last comma and space
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
