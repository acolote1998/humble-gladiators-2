package com.github.acolote1998.humble_gladiators_2.core.enums;

public enum RequirementEntryOperator {
    MORETHAN,
    LESSTHAN,
    MOREOREQUALTHAN,
    LESSOREQUALTHAN,
    EQUALTHAN,
    IN,
    NOT_IN,
    EXISTS,
    NOT_EXISTS;

    // Method to get all constants as a comma-separated string
    public static String AllRequirementEntryOperatorToString() {
        StringBuilder sb = new StringBuilder();
        for (RequirementEntryOperator operator : RequirementEntryOperator.values()) {
            sb.append(operator.name()).append(", ");
        }
        // Remove last comma and space
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
