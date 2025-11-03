package com.github.acolote1998.humble_gladiators_2.core.util;

public class PromptAider {

    public static String RarityToCardImageContext(Integer rarity) {
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

    public static String RarityToCardBackgroundImageContext(Integer rarity) {
        if (rarity == null) return "";
        return switch (rarity) {
            case 1 -> "A simple and grounded environment, modest in appearance and lacking notable energy or grandeur.";
            case 2 ->
                    "A slightly distinctive setting, showing hints of subtle power, color variation, or gentle atmosphere shifts.";
            case 3 ->
                    "An environment that feels charged with presence or meaning. The surroundings show balance, harmony, or faint traces of extraordinary energy.";
            case 4 ->
                    "A striking and intense environment filled with dramatic contrast, vivid lighting, and an unmistakable sense of power or wonder.";
            case 5 ->
                    "An awe-inspiring, transcendent environment that radiates immense power, majesty, and timeless significance. The atmosphere feels almost divine or cosmic in scale.";
            default -> "";
        };
    }


    public static String TierToCardImageContext(Integer tier) {
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

    public static String TierToCardBackgroundImageContext(Integer tier) {
        if (tier == null) return "";
        return switch (tier) {
            case 1 ->
                    "A small, calm, and contained scene with limited motion or energy. The environment feels localized and stable.";
            case 2 ->
                    "A scene of moderate scale and complexity. The environment begins to show layered depth, subtle motion, or a growing sense of tension.";
            case 3 ->
                    "A dynamic and engaging environment with visible contrast, motion, or evolving atmosphere that suggests rising conflict or importance.";
            case 4 ->
                    "A grand, intense environment filled with kinetic energy, vast space, and emotionally charged atmosphere. The viewer feels awe and anticipation.";
            case 5 ->
                    "An overwhelming and monumental environment of epic magnitude. The space feels boundless, luminous, and alive with elemental or cosmic intensity.";
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

    public static String GetGeneralObjectGenerationRules() {
        return """
                    - Answer with ONLY json format, not extra text or explanations.
                    - Do not include "id", "createdAt", or "updatedAt" in the JSON.
                    - If a field represents an enum (like "requirementType"), it MUST be exactly one of the allowed provided values.
                    - Do NOT invent any new enum values. Only use the ones listed above.
                    - Do NOT generate item names or descriptions that promise in-game effects or powers. For example, avoid names like "Teleportation Boots" or descriptions like "This item gives the user the power of X".
                    - Always generate text in English.
                    - When generating names for characters or items:
                        - Prefer short names by default, like "Karen Filippelli" or "Thor".
                        - Only add extra descriptors if they make the character/item funnier, more memorable, or rich/interesting in the context of the game.
                        - Avoid unnecessarily long names that include multiple descriptors without added value.
                    - When generating names or descriptions, you may mix elements from multiple wanted themes creatively, but only do so if this enhances the flavor, or thematic interest.
                        Examples:
                            - "Thor, the Fire-Breathing Bard" ✅ (Good outcome: fun, thematic from 'Marvel' + 'Medieval Fantasy' + 'Music')
                            - "Karen Filippelli" ✅ (Good outcome: short, thematic from 'The Office', no need for extra context)
                            - "Sword of Messi" ✅ (Good outcome: thematic from 'Soccer' + 'Pirates')
                            - "Michael Scott, Regional Manager, That's What She Said" ❌ (Bad outcome: Too long, unnecessary)
                    - Do not force the generation to fit the category, if an object does not fit or does not make sense, just use "OTHER"
                """;
    }
}
