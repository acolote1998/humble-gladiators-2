package com.github.acolote1998.humble_gladiators_2.item.enums;

public enum ConsumablesCategory {
    FOOD,
    DRINK,
    MEDICINE,
    TREAT,
    TOY,
    TOOL,
    GADGET,
    BOOK,
    DOCUMENT,
    ACCESSORY,
    MISCELLANEOUS,
    OTHER;

    // Method to get all constants as a comma-separated string
    public static String AllConsumablesCategoryToString() {
        StringBuilder sb = new StringBuilder();
        for (ConsumablesCategory category : ConsumablesCategory.values()) {
            sb.append(category.name()).append(", ");
        }
        // Remove last comma and space
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
