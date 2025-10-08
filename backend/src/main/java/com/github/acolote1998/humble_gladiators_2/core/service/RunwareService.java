package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.RunwareImageGenResponse;
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

    public byte[] getGeneratedImageBytes() {
        String prompt = "Generate an image of a red dragon";
        String negativePrompt = "The dragon cannot have a tail";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        HttpEntity<List<HashMap<String, Object>>> entity =
                new HttpEntity<>(getRequestBody(prompt, negativePrompt), headers);

        ResponseEntity<RunwareImageGenResponse> response =
                restTemplate.exchange(IMG_GEN_URL, HttpMethod.POST, entity, RunwareImageGenResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String imgUrl = response.getBody().data().getFirst().imageURL();
            return imgUrlToBytes(imgUrl);
        } else {
            log.error("Error generating card image");
            return null;
        }
    }

}
