package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.RunwareImageGenResponse;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
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
                    "Simple, low-tier craftsmanship — made of common materials, showing signs of wear or basic construction.";
            case 2 -> "Moderate-tier equipment — reliable design with some ornamentation or improved materials.";
            case 3 ->
                    "Well-crafted, balanced tier — detailed workmanship, durable, and battle-ready with distinct style.";
            case 4 ->
                    "High-tier craftsmanship — ornate, polished, and made with superior materials or faint magical glow.";
            case 5 ->
                    "Legendary-tier artifact — masterpiece of design, glowing with enchantment, radiating immense power and prestige.";
            default -> "";
        };
    }

    public String rarityToContext(Integer rarity) {
        if (rarity == null) return "";
        return switch (rarity) {
            case 1 -> "Common rarity — plain and utilitarian, lacking decoration or special properties.";
            case 2 -> "Uncommon rarity — slightly refined, showing minor magical traits or uncommon materials.";
            case 3 -> "Rare rarity — detailed, elegant, with unique characteristics and notable craftsmanship.";
            case 4 ->
                    "Epic rarity — visually impressive, glowing or enchanted, crafted by master artisans or imbued with strong magic.";
            case 5 ->
                    "Legendary rarity — one-of-a-kind artifact, awe-inspiring, exuding mythic energy and grand design.";
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

}
