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

    public static String GetCardImageGenerationGeneralRules() {
        return """
                        - The artwork should be:
                          - Standalone (no card frame, no text, no border, no logos)
                          - High-quality, vibrant, detailed, and visually striking
                          - Consistent with the campaign theme
                          - Focused on the item/character, no background clutter
                        - Background: Use a subtle, thematic background that complements the item without overwhelming it
                          (e.g., dark gradient, mystical atmosphere, textured surface, or environmental hint that matches the campaign theme)
                        - Avoid:
                          - Any text, logos, or symbols
                          - Any references to unrelated themes
                          - Plain white or completely empty backgrounds
                        - Style:
                          - Realistic fantasy illustration, painterly, with depth and shading
                          - Emphasize color, texture, and thematic storytelling (e.g., dragon scales, mystical elements)
                        - OUTPUT INSTRUCTIONS:
                            - Output ONLY the final text of the image prompt.
                            - Do NOT add introductions, explanations, or meta commentary.
                            - Do NOT include phrases like "Here is your prompt:" or "Okay, here’s...".
                            - Do NOT use markdown code blocks or backticks.
                            - Just return the raw text that will be sent to the image generator.
                """;
    }

    public static String BuildNegativePromptForRunware(String unwantedThemes) {
        return "This is a list of the themes that we DO NOT WANT to be part of the campaign: "
                + unwantedThemes
                + ", white background, plain background, empty background, studio lighting";
    }
}
