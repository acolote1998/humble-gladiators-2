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

    public String sendTestPrompt() {

        String prompt = "This is just a status check. If you are receiving this, answer with a flat string being 'Online: Gemini Controller is up'.";

        // Prepare the request body according to Gemini API specification
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Construct the full URL with API key
        String fullUrl = URL + "?key=" + apiKey;

        try {
            ResponseEntity<GeminiResponseDto> response = restTemplate.exchange(fullUrl, HttpMethod.POST, entity, GeminiResponseDto.class);
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

    public List<DrawingAction> generateDrawingActionsTest(String imageToGenerate, Integer width, Integer height) throws JsonProcessingException {
        log.info("Starting List<DrawingAction> generation attempt");
        String prompt = String.format("""
                Generate a JSON array of drawing actions to create a MINIMALISTIC %s image.
                
                SYSTEM OVERVIEW:
                - Canvas: %dx%d pixels with transparent background
                - Each action creates pixels that are layered (first = background, last = foreground)
                - Later shapes overlap earlier ones to create depth and detail
                - Final image is built pixel-by-pixel from all actions
                
                REQUIREMENTS:
                - Create a CLEAR, RECOGNIZABLE version of the %s
                - Use enough shapes to make the object easily identifiable
                - Generate 40-80 drawing actions for good detail
                - Use color variations to create depth and character
                - Center main elements around coordinates %d,%d
                
                JSON FORMAT:
                Return ONLY a valid JSON array (no explanations, no markdown):
                Example:
                ```json
                [
                    {"drawingMethod": 0, "initialX": 50, "initialY": 50, "red": 255, "green": 255, "blue": 0, "alpha": 255, "size": 20, "width": 0, "height": 0, "radius": 0},
                    {"drawingMethod": 12, "initialX": 300, "initialY": 300, "red": 0, "green": 255, "blue": 0, "alpha": 255, "size": 0, "width": 40, "height": 25, "radius": 0}
                ]
                ```
                
                DRAWING METHODS:
                0: SQUARE (size) - Solid filled square
                1: RECTANGLE (width, height) - Solid filled rectangle
                2: HORIZONTAL_LINE (width) - Single pixel thick horizontal line
                3: VERTICAL_LINE (height) - Single pixel thick vertical line
                4: CIRCLE (radius) - Solid filled circle
                5: HOLLOW_SQUARE (size) - Square outline only (empty inside)
                6: DOT (coordinates only) - Single pixel point
                7: TRIANGLE_UP (size) - Solid triangle pointing upward
                8: TRIANGLE_DOWN (size) - Solid triangle pointing downward
                9: TRIANGLE_LEFT (size) - Solid triangle pointing left
                10: TRIANGLE_RIGHT (size) - Solid triangle pointing right
                11: DIAMOND (size) - Solid diamond/rhombus shape
                12: ELLIPSE (width, height) - Solid filled oval/ellipse
                13: ARC (radius, start angle, end angle) - Curved arc segment
                14: CURVED_LINE (width, height, curve height) - Smooth curved line
                15: STAR (radius, points) - Multi-pointed star shape
                16: GRADIENT_SQUARE (size, start color, end color) - Square with color gradient
                
                PARAMETERS:
                - Colors: 0-255 (red, green, blue, alpha)
                - Coordinates: 0-%d (initialX, initialY)
                - Set unused parameters to 0
                
                METHOD-SPECIFIC PARAMETERS:
                - SQUARE (0): size = side length (e.g., size=20 creates 20x20 square)
                - RECTANGLE (1): width = width, height = height (e.g., width=30, height=15)
                - HORIZONTAL_LINE (2): width = line length, height = 0 (e.g., width=50)
                - VERTICAL_LINE (3): height = line length, width = 0 (e.g., height=40)
                - CIRCLE (4): radius = circle radius, size/width/height = 0 (e.g., radius=25)
                - HOLLOW_SQUARE (5): size = side length (e.g., size=20)
                - DOT (6): only initialX, initialY matter, all others = 0
                - TRIANGLES (7-10): size = triangle size/height (e.g., size=15)
                - DIAMOND (11): size = diamond size (e.g., size=20)
                - ELLIPSE (12): width = ellipse width, height = ellipse height (e.g., width=30, height=20)
                - ARC (13): radius = arc radius, size = start angle (0-360), width = end angle (0-360)
                - CURVED_LINE (14): width = end X offset, height = end Y offset, size = curve height
                - STAR (15): radius = star size, size = number of points (5-8), width/height = 0
                - GRADIENT_SQUARE (16): size = square size, red/green = start color, blue/alpha = end color
                
                DESIGN APPROACH:
                - Create a clear, recognizable version of the %s
                - Use shapes creatively to build a coherent object
                - Add enough detail to make it identifiable (not just basic shapes)
                - Use layering and overlapping for depth
                - Center main elements around coordinates %d,%d
                """, imageToGenerate, width, height, width / 2, height / 2, width - 1, width, height, width / 2, height / 2);

        // Prepare the request body according to Gemini API specification
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Construct the full URL with API key
        String fullUrl = URL + "?key=" + apiKey;

        try {
            ResponseEntity<GeminiResponseDto> response = restTemplate.exchange(fullUrl, HttpMethod.POST, entity, GeminiResponseDto.class);
            String resultText = Objects.requireNonNull(response.getBody())
                    .candidates().get(0)
                    .content().parts().get(0)
                    .text();
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
