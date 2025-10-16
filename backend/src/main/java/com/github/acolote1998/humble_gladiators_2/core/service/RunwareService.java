package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
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

import static com.github.acolote1998.humble_gladiators_2.core.util.PromptAider.*;

@Service
@Slf4j
public class RunwareService {
    RestTemplate restTemplate = new RestTemplate();
    GeminiService geminiService;

    @Value("${RUNWARE_IMG_GEN_URL}")
    String IMG_GEN_URL;

    @Value("${RUNWARE_API_KEY}")
    String API_KEY;

    private final Integer cardImageWidth = 768;
    private final Integer cardImageHeight = 576;

    private final Integer campaignCoverImageWidth = 1344;
    private final Integer campaignCoverImageHeight = 896;

    private final Integer campaignCardBackImageWidth = 1024;
    private final Integer campaignCardBackImageHeight = 1408;

    @Autowired
    public RunwareService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    List<HashMap<String, Object>> getRequestBody(String prompt, String unwantedThemesContext, Integer width, Integer height) {
        HashMap<String, Object> requestBodyObject = new HashMap<>();
        requestBodyObject.put("taskType", "imageInference");
        requestBodyObject.put("taskUUID", UUID.randomUUID().toString());
        requestBodyObject.put("positivePrompt", prompt);
        requestBodyObject.put("negativePrompt", unwantedThemesContext);
        requestBodyObject.put("width", width); //768
        requestBodyObject.put("height", height); // x 576 for card images
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

    public ResponseEntity<RunwareImageGenResponse> sendRequestToImageGenerator(String prompt,
                                                                               String negativePrompt,
                                                                               Integer imgWidth,
                                                                               Integer imgHeight) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        HttpEntity<List<HashMap<String, Object>>> entityToSend =
                new HttpEntity<>(getRequestBody(prompt, negativePrompt, imgWidth, imgHeight), headers);

        return restTemplate.exchange(IMG_GEN_URL, HttpMethod.POST, entityToSend, RunwareImageGenResponse.class);
    }

    public byte[] generateArmorTemplateImageToBytes(Campaign campaign, ArmorTemplate armorTemplate) {
        log.info(String.format("Attempt to generate image for %s - %s", armorTemplate.getName(), ArmorTemplate.class));

        String positivePrompt = geminiService.getPositiveArmorPromptForRuneware(campaign, armorTemplate);
        String negativePrompt = BuildNegativePromptForRunware(campaign.getTheme().getUnwantedThemes().toString());

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(
                positivePrompt,
                negativePrompt,
                cardImageWidth,
                cardImageHeight
        );

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

        String positivePrompt = geminiService.getPositiveBootsPromptForRuneware(campaign, bootsTemplate);
        String negativePrompt = BuildNegativePromptForRunware(campaign.getTheme().getUnwantedThemes().toString());

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(
                positivePrompt,
                negativePrompt,
                cardImageWidth,
                cardImageHeight);

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

        String positivePrompt = geminiService.getPositiveConsumablesPromptForRuneware(campaign, consumableTemplate);
        String negativePrompt = BuildNegativePromptForRunware(campaign.getTheme().getUnwantedThemes().toString());

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(
                positivePrompt,
                negativePrompt,
                cardImageWidth,
                cardImageHeight);

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

        String positivePrompt = geminiService.getPositiveHelmetPromptForRuneware(campaign, helmetTemplate);
        String negativePrompt = BuildNegativePromptForRunware(campaign.getTheme().getUnwantedThemes().toString());

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(
                positivePrompt,
                negativePrompt,
                cardImageWidth,
                cardImageHeight);

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

        String positivePrompt = geminiService.getPositiveShieldPromptForRuneware(campaign, shieldTemplate);
        String negativePrompt = BuildNegativePromptForRunware(campaign.getTheme().getUnwantedThemes().toString());

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(
                positivePrompt,
                negativePrompt,
                cardImageWidth,
                cardImageHeight);

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

        String positivePrompt = geminiService.getPositiveSpellPromptForRuneware(campaign, spellTemplate);
        String negativePrompt = BuildNegativePromptForRunware(campaign.getTheme().getUnwantedThemes().toString());

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(
                positivePrompt,
                negativePrompt,
                cardImageWidth,
                cardImageHeight);

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

        String positivePrompt = geminiService.getPositiveWeaponPromptForRuneware(campaign, weaponTemplate);
        String negativePrompt = BuildNegativePromptForRunware(campaign.getTheme().getUnwantedThemes().toString());

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(
                positivePrompt,
                negativePrompt,
                cardImageWidth,
                cardImageHeight);

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

    public byte[] generateCharacterInstanceImageToBytes(Campaign campaign, CharacterInstance characterInstance) {
        log.info(String.format("Attempt to generate image for %s - %s", characterInstance.getName(), CharacterInstance.class));

        String positivePrompt = geminiService.getPositiveCharacterInstancePromptForRuneware(campaign, characterInstance);
        String negativePrompt = BuildNegativePromptForRunware(campaign.getTheme().getUnwantedThemes().toString());

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(
                positivePrompt,
                negativePrompt,
                cardImageWidth,
                cardImageHeight);

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

    public byte[] generateCampaignCoverImageToBytes(String positivePrompt, Campaign campaign) {
        log.info(String.format("Attempt to generate campaign cover image for ID %s - %s", campaign.getId(), campaign.getName()));

        String negativePrompt = BuildNegativePromptForRunware(campaign.getTheme().getUnwantedThemes().toString());

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(
                positivePrompt,
                negativePrompt,
                campaignCoverImageWidth,
                campaignCoverImageHeight);

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
            log.error("Error generating campaign cover image");
            return null;
        }
    }

    public byte[] generateCampaignCardBackImageToBytes(String positivePrompt, Campaign campaign) {
        log.info(String.format("Attempt to generate campaign card back image for ID %s - %s", campaign.getId(), campaign.getName()));

        positivePrompt = positivePrompt + " IMPORTANT INSTRUCTION: Ensure the generated image extends to the edges of the card, leaving no empty or transparent background.";

        String negativePrompt = BuildNegativePromptForRunware(campaign.getTheme().getUnwantedThemes().toString());

        ResponseEntity<RunwareImageGenResponse> response = sendRequestToImageGenerator(
                positivePrompt,
                negativePrompt,
                campaignCardBackImageWidth,
                campaignCardBackImageHeight);

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
            log.error("Error generating campaign back card image");
            return null;
        }
    }

}
