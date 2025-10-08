package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.RunwareImageGenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
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


    public String getBase64GeneratedImage() {
        String prompt = "Generate an image of a red dragon";
        String negativePrompt = "The dragon cannot have wings";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        HttpEntity<List<HashMap<String, Object>>> entity = new HttpEntity<>(getRequestBody(prompt, negativePrompt), headers);

        ResponseEntity<RunwareImageGenResponse> response = restTemplate.exchange(
                IMG_GEN_URL, HttpMethod.POST, entity, RunwareImageGenResponse.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody().data().getFirst().imageURL();
        } else return "Error generating card image";
    }
}
