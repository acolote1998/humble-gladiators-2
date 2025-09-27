package com.github.acolote1998.humble_gladiators_2.core.enums;

public enum RequirementEntryType {
    LEVEL,
    HP,
    MP,
    //    CLASSID,
//    QUEST,
//    RACEID,
//    QUESTID,
    HEIGHT,
    WEIGHT,
    CONSTITUTION,
    INTELLIGENCE,
    DEXTERITY,
    STRENGTH,
    SPEED,
    LUCK,
    LOCALVICTORIES,
    ONLINEVICTORIES,
    TOTALVICTORIES,
    ITEMID,
    GOLD;

    // Method to get all constants as a comma-separated string
    public static String AllRequirementEntryTypeToString() {
        StringBuilder sb = new StringBuilder();
        for (RequirementEntryType type : RequirementEntryType.values()) {
            sb.append(type.name()).append(", ");
        }
        // Remove last comma and space
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

}