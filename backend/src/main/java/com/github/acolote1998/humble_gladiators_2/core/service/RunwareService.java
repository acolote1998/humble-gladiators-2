package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.RunwareImageGenResponse;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
@Slf4j
public class RunwareService {
    RestTemplate restTemplate = new RestTemplate();

    @Value("${RUNWARE_IMG_GEN_URL}")
    String IMG_GEN_URL;

    @Value("${RUNWARE_API_KEY}")
    String API_KEY;

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

    byte[] imgUrlToBytes(String imgUrl) {
        try (InputStream in = new URL(imgUrl).openStream()) {
            byte[] imageBytes = in.readAllBytes();
            log.info("Image bytes successfully processed");
            return imageBytes;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error processing image to bytes");
            return null;
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

    public byte[] generateArmorTemplateImageToBytes(Campaign campaign, ArmorTemplate armorTemplate) {
        log.info(String.format("Attempt to generate image for %s - %s", armorTemplate.getName(), ArmorTemplate.class));
        String positivePrompt = String.format("""
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
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
                        """,
                "Armor",
                campaign.getTheme().getWantedThemes().toString(),
                armorTemplate.getName(),
                armorTemplate.getDescription());
        String negativePrompt = "This is a list of the themes that we DO NOT WANT to be part of the campaign: "
                + campaign.getTheme().getUnwantedThemes().toString();

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(positivePrompt, negativePrompt);

        if (response.getStatusCode().is2xxSuccessful()) {
            String imgUrl = response.getBody().data().getFirst().imageURL();
            byte[] imgBytes = imgUrlToBytes(imgUrl);
            return imgBytes;
        } else {
            log.error("Error generating card image");
            return null;
        }
    }

}
