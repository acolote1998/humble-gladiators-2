package com.github.acolote1998.humble_gladiators_2.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.acolote1998.humble_gladiators_2.core.dto.GeminiResponseDto;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.DrawingAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class GeminiService {
    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    ObjectMapper mapper;

    // Gemini API endpoint for content generation
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";


    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public GeminiService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    HttpEntity<Map<String, Object>> produceEntity(String prompt) {
        // Prepare the request body according to Gemini API specification
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    String getFullUrl() {
        // Construct the full URL with API key
        return URL + "?key=" + apiKey;
    }

    String callGemini(String prompt) {
        try {
            ResponseEntity<GeminiResponseDto> response = restTemplate.exchange(getFullUrl(), HttpMethod.POST, produceEntity(prompt), GeminiResponseDto.class);
            String resultText = Objects.requireNonNull(response.getBody())
                    .candidates().get(0)
                    .content().parts().get(0)
                    .text();
            log.info(resultText);
            return resultText;
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Error: Failed to communicate with Gemini API - " + e.getMessage();
        }
    }

    public String sendTestPrompt() {
        String prompt = "This is just a status check. If you are receiving this, answer with a flat string being 'Online: Gemini Controller is up'.";
        return callGemini(prompt);
    }

    public List<DrawingAction> generateDrawingActionsTest(String imageToGenerate, Integer width, Integer height) throws JsonProcessingException {
        log.info("Starting List<DrawingAction> generation attempt");
        String prompt = String.format("""
                Return ONLY a valid JSON array (no explanations, no markdown).
                Each object in the array must include ALL fields exactly as defined, even if unused:\s
                { "drawingMethod": int, "initialX": int, "initialY": int, "red": int, "green": int, "blue": int, "alpha": int, "size": int, "width": int, "height": int, "radius": int }
                
                TASK:
                Generate a JSON array of drawing actions to create a MINIMALISTIC %s image.
                
                SYSTEM OVERVIEW:
                - Canvas: %dx%d pixels with transparent background
                - Actions are layered in order: first = background, last = foreground
                - Shapes are pixel-filled based on the rules below
                - Generate between 40 and 80 drawing actions
                - Use color variations for depth and recognizability
                - Center the main figure around coordinates %d,%d
                - All color values must be integers in range 0–255
                - All unused parameters must be 0
                
                DRAWING METHODS (exact behavior from rendering engine):
                
                0: SQUARE — solid square, top-left at (initialX, initialY), size = side length \s
                1: RECTANGLE — solid rectangle, top-left at (initialX, initialY), width × height \s
                2: HORIZONTAL_LINE — line starting at (initialX, initialY), length = width \s
                3: VERTICAL_LINE — line starting at (initialX, initialY), length = height \s
                4: CIRCLE — solid circle, center at (initialX, initialY), radius \s
                5: HOLLOW_SQUARE — outline only, top-left at (initialX, initialY), size = side length \s
                6: DOT — single pixel at (initialX, initialY) \s
                7: TRIANGLE_UP — apex at (initialX, initialY), height = size \s
                8: TRIANGLE_DOWN — apex at (initialX, initialY), height = size \s
                9: TRIANGLE_LEFT — tip at (initialX, initialY), width = size \s
                10: TRIANGLE_RIGHT — tip at (initialX, initialY), width = size \s
                11: DIAMOND — centered at (initialX, initialY), size = diagonal length \s
                12: ELLIPSE — centered at (initialX, initialY), width × height \s
                13: ARC — centered at (initialX, initialY), radius, size = start angle (degrees), width = end angle (degrees) \s
                14: CURVED_LINE — start at (initialX, initialY), width = end X offset, height = end Y offset, size = curve height \s
                15: STAR — centered at (initialX, initialY), radius = outer radius, size = number of points (5–8) \s
                16: GRADIENT_SQUARE — solid gradient square, top-left at (initialX, initialY), size = side length \s
                    - red = start R, green = start G \s
                    - blue = end R, alpha = end G \s
                    - blue channel is fixed at 128, alpha is fixed at 255 during rendering
                
                DESIGN APPROACH:
                - Build the %s using overlapping shapes
                - Ensure it is minimalistic but clearly recognizable
                - Use depth layering for clarity
                - Keep the drawing centered around %d,%d
                
                """, imageToGenerate, width, height, width / 2, height / 2, width - 1, width, height, width / 2, height / 2);

        try {
            String resultText = callGemini(prompt);
            resultText = resultText.replaceAll("`", "").replaceAll("json", "");
            List<DrawingAction> resultList = mapper.readValue(resultText, new TypeReference<List<DrawingAction>>() {
            });
            log.info("Success. List<DrawingAction> generated " + resultList.size() + " actions");
            return resultList;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}
