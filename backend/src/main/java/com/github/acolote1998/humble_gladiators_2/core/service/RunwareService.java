package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.RunwareImageGenResponse;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
import com.github.acolote1998.humble_gladiators_2.item.templates.BootsTemplate;
import com.github.acolote1998.humble_gladiators_2.item.templates.ConsumableTemplate;
import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;
import com.github.acolote1998.humble_gladiators_2.item.templates.ShieldTemplate;
import com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate;
import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RunwareService {
    RestTemplate restTemplate = new RestTemplate();
    GeminiService geminiService;

    @Value("${RUNWARE_IMG_GEN_URL}")
    String IMG_GEN_URL;

    @Value("${RUNWARE_API_KEY}")
    String API_KEY;

    @Autowired
    public RunwareService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    List<HashMap<String, Object>> getRequestBody(String prompt, String unwantedThemesContext) {
        HashMap<String, Object> requestBodyObject = new HashMap<>();
        requestBodyObject.put("taskType", "imageInference");
        requestBodyObject.put("taskUUID", UUID.randomUUID().toString());
        requestBodyObject.put("positivePrompt", prompt);
        requestBodyObject.put("negativePrompt", unwantedThemesContext);
        requestBodyObject.put("width", 768);
        requestBodyObject.put("height", 576);
        requestBodyObject.put("model", "runware:101@1");
        requestBodyObject.put("numberResults", 1);
        requestBodyObject.put("includeCost", true);

        List<HashMap<String, Object>> requestBody = new ArrayList<>();
        requestBody.add(requestBodyObject);
        return requestBody;
    }

    byte[] imgUrlToBytes(String imgUrl) throws InterruptedException {
        try (InputStream in = new URL(imgUrl).openStream()) {
            byte[] imageBytes = in.readAllBytes();
            log.info("Image bytes successfully processed");
            return imageBytes;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error processing image to bytes, retrying");
            Thread.sleep(500);
            return imgUrlToBytes(imgUrl);
        }
    }

    public ResponseEntity<RunwareImageGenResponse> sendRequestToImageGenerator(String prompt, String negativePrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        HttpEntity<List<HashMap<String, Object>>> entityToSend =
                new HttpEntity<>(getRequestBody(prompt, negativePrompt), headers);

        return restTemplate.exchange(IMG_GEN_URL, HttpMethod.POST, entityToSend, RunwareImageGenResponse.class);
    }

    public String tierToContext(Integer tier) {
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


    public String rarityToContext(Integer rarity) {
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

    public String getGeneralRules() {
        return """
                        - The artwork should be:
                          - Standalone (no card frame, no text, no border, no logos)
                          - High-quality, vibrant, detailed, and visually striking
                          - Consistent with the campaign theme
                          - Focused on the item/character, no background clutter
                        - Avoid:
                          - Any text, logos, or symbols
                          - Any references to unrelated themes
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

    public byte[] generateArmorTemplateImageToBytes(Campaign campaign, ArmorTemplate armorTemplate) {
        log.info(String.format("Attempt to generate image for %s - %s", armorTemplate.getName(), ArmorTemplate.class));
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating an armor, render only the armor—no body, mannequin, or person wearing it unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Armor",
                campaign.getTheme().getWantedThemes().toString(),
                armorTemplate.getName(),
                armorTemplate.getDescription(),
                tierToContext(armorTemplate.getTier()),
                rarityToContext(armorTemplate.getRarity()),
                getGeneralRules());
        String positivePrompt = geminiService.getPositivePromptForRuneware(promptForGemini);
        String negativePrompt = "This is a list of the themes that we DO NOT WANT to be part of the campaign: "
                + campaign.getTheme().getUnwantedThemes().toString();

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(positivePrompt, negativePrompt);

        if (response.getStatusCode().is2xxSuccessful()) {
            String imgUrl = response.getBody().data().getFirst().imageURL();
            try {
                byte[] imgBytes = imgUrlToBytes(imgUrl);
                return imgBytes;
            } catch (Exception e) {
                log.error("Could not convert img url to bytes - " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            log.error("Error generating card image");
            return null;
        }
    }

    public byte[] generateBootsTemplateImageToBytes(Campaign campaign, BootsTemplate bootsTemplate) {
        log.info(String.format("Attempt to generate image for %s - %s", bootsTemplate.getName(), BootsTemplate.class));
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating boots, render only the boots—no body, mannequin, or person wearing them unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Boots",
                campaign.getTheme().getWantedThemes().toString(),
                bootsTemplate.getName(),
                bootsTemplate.getDescription(),
                tierToContext(bootsTemplate.getTier()),
                rarityToContext(bootsTemplate.getRarity()),
                getGeneralRules());
        String positivePrompt = geminiService.getPositivePromptForRuneware(promptForGemini);
        String negativePrompt = "This is a list of the themes that we DO NOT WANT to be part of the campaign: "
                + campaign.getTheme().getUnwantedThemes().toString();

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(positivePrompt, negativePrompt);

        if (response.getStatusCode().is2xxSuccessful()) {
            String imgUrl = response.getBody().data().getFirst().imageURL();
            try {
                byte[] imgBytes = imgUrlToBytes(imgUrl);
                return imgBytes;
            } catch (Exception e) {
                log.error("Could not convert img url to bytes - " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            log.error("Error generating card image");
            return null;
        }
    }

    public byte[] generateConsumableTemplateImageToBytes(Campaign campaign, ConsumableTemplate consumableTemplate) {
        log.info(String.format("Attempt to generate image for %s - %s", consumableTemplate.getName(), ConsumableTemplate.class));
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating a consumable, render only the consumable item—no background characters or additional objects unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Consumable",
                campaign.getTheme().getWantedThemes().toString(),
                consumableTemplate.getName(),
                consumableTemplate.getDescription(),
                tierToContext(consumableTemplate.getTier()),
                rarityToContext(consumableTemplate.getRarity()),
                getGeneralRules());
        String positivePrompt = geminiService.getPositivePromptForRuneware(promptForGemini);
        String negativePrompt = "This is a list of the themes that we DO NOT WANT to be part of the campaign: "
                + campaign.getTheme().getUnwantedThemes().toString();

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(positivePrompt, negativePrompt);

        if (response.getStatusCode().is2xxSuccessful()) {
            String imgUrl = response.getBody().data().getFirst().imageURL();
            try {
                byte[] imgBytes = imgUrlToBytes(imgUrl);
                return imgBytes;
            } catch (Exception e) {
                log.error("Could not convert img url to bytes - " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            log.error("Error generating card image");
            return null;
        }
    }

    public byte[] generateHelmetTemplateImageToBytes(Campaign campaign, HelmetTemplate helmetTemplate) {
        log.info(String.format("Attempt to generate image for %s - %s", helmetTemplate.getName(), HelmetTemplate.class));
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating a helmet, render only the helmet—no body, mannequin, or person wearing it unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Helmet",
                campaign.getTheme().getWantedThemes().toString(),
                helmetTemplate.getName(),
                helmetTemplate.getDescription(),
                tierToContext(helmetTemplate.getTier()),
                rarityToContext(helmetTemplate.getRarity()),
                getGeneralRules());
        String positivePrompt = geminiService.getPositivePromptForRuneware(promptForGemini);
        String negativePrompt = "This is a list of the themes that we DO NOT WANT to be part of the campaign: "
                + campaign.getTheme().getUnwantedThemes().toString();

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(positivePrompt, negativePrompt);

        if (response.getStatusCode().is2xxSuccessful()) {
            String imgUrl = response.getBody().data().getFirst().imageURL();
            try {
                byte[] imgBytes = imgUrlToBytes(imgUrl);
                return imgBytes;
            } catch (Exception e) {
                log.error("Could not convert img url to bytes - " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            log.error("Error generating card image");
            return null;
        }
    }

    public byte[] generateShieldTemplateImageToBytes(Campaign campaign, ShieldTemplate shieldTemplate) {
        log.info(String.format("Attempt to generate image for %s - %s", shieldTemplate.getName(), ShieldTemplate.class));
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating a shield, render only the shield—no body, mannequin, or person holding it unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Shield",
                campaign.getTheme().getWantedThemes().toString(),
                shieldTemplate.getName(),
                shieldTemplate.getDescription(),
                tierToContext(shieldTemplate.getTier()),
                rarityToContext(shieldTemplate.getRarity()),
                getGeneralRules());
        String positivePrompt = geminiService.getPositivePromptForRuneware(promptForGemini);
        String negativePrompt = "This is a list of the themes that we DO NOT WANT to be part of the campaign: "
                + campaign.getTheme().getUnwantedThemes().toString();

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(positivePrompt, negativePrompt);

        if (response.getStatusCode().is2xxSuccessful()) {
            String imgUrl = response.getBody().data().getFirst().imageURL();
            try {
                byte[] imgBytes = imgUrlToBytes(imgUrl);
                return imgBytes;
            } catch (Exception e) {
                log.error("Could not convert img url to bytes - " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            log.error("Error generating card image");
            return null;
        }
    }

    public byte[] generateSpellTemplateImageToBytes(Campaign campaign, SpellTemplate spellTemplate) {
        log.info(String.format("Attempt to generate image for %s - %s", spellTemplate.getName(), SpellTemplate.class));
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - The spell category is: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating a spell, render the magical effect, energy, or manifestation—no background characters or additional objects unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Spell",
                spellTemplate.getCategory(),
                campaign.getTheme().getWantedThemes().toString(),
                spellTemplate.getName(),
                spellTemplate.getDescription(),
                tierToContext(spellTemplate.getTier()),
                rarityToContext(spellTemplate.getRarity()),
                getGeneralRules());
        String positivePrompt = geminiService.getPositivePromptForRuneware(promptForGemini);
        String negativePrompt = "This is a list of the themes that we DO NOT WANT to be part of the campaign: "
                + campaign.getTheme().getUnwantedThemes().toString();

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(positivePrompt, negativePrompt);

        if (response.getStatusCode().is2xxSuccessful()) {
            String imgUrl = response.getBody().data().getFirst().imageURL();
            try {
                byte[] imgBytes = imgUrlToBytes(imgUrl);
                return imgBytes;
            } catch (Exception e) {
                log.error("Could not convert img url to bytes - " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            log.error("Error generating card image");
            return null;
        }
    }

    public byte[] generateWeaponTemplateImageToBytes(Campaign campaign, WeaponTemplate weaponTemplate) {
        log.info(String.format("Attempt to generate image for %s - %s", weaponTemplate.getName(), WeaponTemplate.class));
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - The weapon category is: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating a weapon, render only the weapon—no body, mannequin, or person holding it unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Weapon",
                weaponTemplate.getCategory(),
                campaign.getTheme().getWantedThemes().toString(),
                weaponTemplate.getName(),
                weaponTemplate.getDescription(),
                tierToContext(weaponTemplate.getTier()),
                rarityToContext(weaponTemplate.getRarity()),
                getGeneralRules());
        String positivePrompt = geminiService.getPositivePromptForRuneware(promptForGemini);
        String negativePrompt = "This is a list of the themes that we DO NOT WANT to be part of the campaign: "
                + campaign.getTheme().getUnwantedThemes().toString();

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(positivePrompt, negativePrompt);

        if (response.getStatusCode().is2xxSuccessful()) {
            String imgUrl = response.getBody().data().getFirst().imageURL();
            try {
                byte[] imgBytes = imgUrlToBytes(imgUrl);
                return imgBytes;
            } catch (Exception e) {
                log.error("Could not convert img url to bytes - " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            log.error("Error generating card image");
            return null;
        }
    }

}
