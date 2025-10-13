package com.github.acolote1998.humble_gladiators_2.core.util;

public class PromptAider {

    public static String RarityToContext(Integer rarity) {
        if (rarity == null) return "";
        return switch (rarity) {
            case 1 -> "Common rarity — ordinary and widely found, simple or unremarkable in nature.";
            case 2 -> "Uncommon rarity — slightly distinctive, showing minor magical or unusual qualities.";
            case 3 -> "Rare rarity — unique or exceptional in appearance, power, or craftsmanship.";
            case 4 -> "Epic rarity — striking and powerful, infused with great energy, artistry, or legend.";
            case 5 ->
                    "Legendary rarity — one-of-a-kind and mythical, surrounded by stories of greatness or divine origin.";
            default -> "";
        };
    }
}
