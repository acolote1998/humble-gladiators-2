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

    public static String TierToContext(Integer tier) {
        if (tier == null) return "";
        return switch (tier) {
            case 1 ->
                    "Low-tier — simple and basic in form or power, made with common means or showing limited refinement.";
            case 2 -> "Moderate-tier — reliable quality or moderate potency, showing some refinement or stability.";
            case 3 ->
                    "Advanced-tier — balanced, durable, or well-developed, crafted or manifested with notable skill or energy.";
            case 4 ->
                    "High-tier — exceptional quality or strong magical energy, refined, polished, or imbued with clear power.";
            case 5 ->
                    "Legendary-tier — extraordinary and awe-inspiring, a masterpiece or source of immense power and prestige.";
            default -> "";
        };
    }
}
