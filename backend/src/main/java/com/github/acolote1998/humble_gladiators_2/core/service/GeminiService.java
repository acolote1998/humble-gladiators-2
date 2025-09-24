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

    public List<DrawingAction> generateDrawingActionsTest(String imageToGenerate) throws JsonProcessingException {
        log.info("Starting List<DrawingAction> generation attempt");
        String prompt = String.format("""
                Generate a JSON array of drawing actions to create a %s image.
                
                HOW THE SYSTEM WORKS:
                - Each DrawingAction creates pixels that get added to a 800x800 canvas
                - Shapes are drawn in order: first action = background layer, last action = foreground layer
                - Later shapes can overlap and cover earlier ones (this creates depth and detail)
                - Each pixel has exact coordinates (x,y) and RGBA color values
                - The final image is built pixel-by-pixel from all the drawing actions
                
                QUALITY REQUIREMENTS:
                - Generate 60 to 100 drawing actions for a detailed, recognizable image
                - Generating less than 60 actions will be consider a total failure
                - Use color variations and shading (different tones of the same color family)
                - Overlap shapes intentionally to create depth and detail
                - Layer shapes on top of each other for enhanced visual effects
                - Create detailed designs suitable for 800x800 pixel canvas
                
                REQUIRED JSON FORMAT:
                Return ONLY a valid JSON array in this exact format (no explanations, no markdown, no code blocks):
                
                EXAMPLE JSON STRUCTURES:
                [
                    {"drawingMethod": 0, "initialX": 50, "initialY": 50, "red": 255, "green": 255, "blue": 0, "alpha": 255, "size": 20, "width": 0, "height": 0, "radius": 0},
                    {"drawingMethod": 1, "initialX": 100, "initialY": 100, "red": 0, "green": 0, "blue": 255, "alpha": 255, "size": 0, "width": 30, "height": 15, "radius": 0},
                    {"drawingMethod": 4, "initialX": 200, "initialY": 200, "red": 255, "green": 0, "blue": 0, "alpha": 255, "size": 0, "width": 0, "height": 0, "radius": 25},
                    {"drawingMethod": 12, "initialX": 300, "initialY": 300, "red": 0, "green": 255, "blue": 0, "alpha": 255, "size": 0, "width": 40, "height": 25, "radius": 0},
                    {"drawingMethod": 14, "initialX": 400, "initialY": 400, "red": 255, "green": 255, "blue": 255, "alpha": 255, "size": 10, "width": 30, "height": 20, "radius": 0},
                    {"drawingMethod": 15, "initialX": 500, "initialY": 500, "red": 255, "green": 255, "blue": 0, "alpha": 255, "size": 5, "width": 0, "height": 0, "radius": 20},
                    {"drawingMethod": 16, "initialX": 100, "initialY": 400, "red": 200, "green": 100, "blue": 50, "alpha": 150, "size": 25, "width": 0, "height": 0, "radius": 0}
                ]
                
                CRITICAL: 
                - Must be valid JSON (proper brackets, commas, quotes)
                - Must include ALL required fields for each object
                - Must be complete (not cut off mid-generation)
                - No markdown formatting, no code blocks, no explanations
                
                Drawing Methods (what each creates):
                - 0: SQUARE - Filled square (size x size pixels)
                - 1: RECTANGLE - Filled rectangle (width x height pixels)
                - 2: HORIZONTAL_LINE - Horizontal line (width pixels long, 1 pixel tall)
                - 3: VERTICAL_LINE - Vertical line (height pixels long, 1 pixel wide)
                - 4: CIRCLE - Filled circle (radius determines size)
                - 5: HOLLOW_SQUARE - Square outline only (size x size pixels, empty inside)
                - 6: DOT - Single pixel point
                - 7: TRIANGLE_UP - Triangle pointing upward (size determines height)
                - 8: TRIANGLE_DOWN - Triangle pointing downward (size determines height)
                - 9: TRIANGLE_LEFT - Triangle pointing left (size determines width)
                - 10: TRIANGLE_RIGHT - Triangle pointing right (size determines width)
                - 11: DIAMOND - Diamond/rhombus shape (size determines overall size)
                - 12: ELLIPSE - Filled ellipse/oval (width x height pixels)
                - 13: ARC - Curved arc segment (radius, start angle, end angle)
                - 14: CURVED_LINE - Smooth curved line (start/end points + curve control)
                - 15: STAR - Multi-pointed star (radius, number of points)
                - 16: GRADIENT_SQUARE - Square with color gradient (size, start/end colors)
                
                PARAMETER DETAILS:
                - Color values (red, green, blue, alpha): 0-255
                - Coordinates (initialX, initialY): 0-599 (800x800 canvas)
                
                METHOD-SPECIFIC PARAMETERS:
                - SQUARE (0): size = square side length (e.g., 20 for 20x20 square)
                - RECTANGLE (1): width = width, height = height (e.g., width=30, height=15)
                - HORIZONTAL_LINE (2): width = line length, height = 0 (e.g., width=50)
                - VERTICAL_LINE (3): height = line length, width = 0 (e.g., height=40)
                - CIRCLE (4): radius = circle radius, size/width/height = 0 (e.g., radius=25)
                - HOLLOW_SQUARE (5): size = square side length (e.g., size=20)
                - DOT (6): only initialX, initialY matter, all others = 0
                - TRIANGLES (7-10): size = triangle size/height (e.g., size=15)
                - DIAMOND (11): size = diamond size (e.g., size=20)
                - ELLIPSE (12): width = ellipse width, height = ellipse height (e.g., width=30, height=20)
                - ARC (13): radius = arc radius, size = start angle (0-360), width = end angle (0-360)
                - CURVED_LINE (14): width = end X offset, height = end Y offset, size = curve height
                - STAR (15): radius = star size, size = number of points (5-8), width/height = 0
                - GRADIENT_SQUARE (16): size = square size, red/green = start color, blue/alpha = end color
                
                DETAILED USAGE GUIDE:
                
                BASIC SHAPES:
                - SQUARES (0): Body parts, armor plates, building blocks (size: 15-40)
                - RECTANGLES (1): Limbs, weapons, architectural elements (width: 20-50, height: 10-30)
                - CIRCLES (4): Simple heads, eyes, wheels (radius: 8-25)
                - ELLIPSES (12): Better for faces, bodies, organic shapes (width: 25-45, height: 15-35)
                
                LINES AND CURVES:
                - HORIZONTAL_LINES (2): Hair, borders, ground lines (width: 20-60)
                - VERTICAL_LINES (3): Hair, borders, structural elements (height: 20-60)
                - CURVED_LINES (14): Smiles, eyebrows, organic curves (width/height: 15-40, size: 5-15)
                - ARCS (13): Decorative curves, smiles, crescent shapes (radius: 10-30, angles: 0-360)
                
                DETAILS AND EFFECTS:
                - TRIANGLES (7-10): Spikes, wings, arrowheads, mountain peaks (size: 10-25)
                - DIAMONDS (11): Gems, decorative elements, geometric patterns (size: 8-20)
                - STARS (15): Magical effects, decorative elements, sparkles (radius: 10-25, points: 5-8)
                - HOLLOW_SQUARES (5): Frames, windows, decorative borders (size: 15-35)
                - GRADIENT_SQUARES (16): Depth, shading, realistic surfaces (size: 15-40, use different colors)
                - DOTS (6): Small details, stars, texture points, pupils (coordinates only)
                
                COLOR GUIDANCE:
                - Use similar colors for related parts (e.g., different shades of blue for armor)
                - Use contrasting colors for details (e.g., gold on blue armor)
                - Use alpha values 200-255 for solid colors, 100-200 for semi-transparent effects
                
                COMPOSITION STRATEGY:
                Think like a digital artist building an image layer by layer:
                
                1. MAIN STRUCTURE: Start with large shapes for main body parts (torso, head, main components)
                2. SECONDARY PARTS: Add medium shapes for limbs, features, accessories
                3. DETAILS: Add small shapes for facial features, textures, decorative elements
                4. HIGHLIGHTS: Add final touches with dots, small shapes, and contrasting colors
                5. FINAL TOUCHES: Add any decorative elements, sparkles, or accent details
                
                VISUAL COMPOSITION RULES:
                - Center important elements around coordinates 400,400 (canvas center)
                - Use larger shapes (30-50 pixels) for main body parts
                - Use medium shapes (15-25 pixels) for features and limbs
                - Use small shapes (5-15 pixels) for details and textures
                - Create depth by layering: body → features → details → highlights
                - Use color gradients: darker colors for shadows, lighter for highlights
                - Overlap shapes intentionally: armor over body, details over surfaces
                - Keep background transparent - only draw the object itself
                
                CREATIVE GUIDELINES:
                - Create detailed, complex objects that take advantage of the 800x800 pixel canvas
                - Focus on creating rich textures, intricate patterns, and layered details
                - Use color variations to create realistic depth, shadows, highlights, and material effects
                - Think about the object's anatomy, materials, textures, and character in detail
                - Create intricate designs with many layers and fine details
                """, imageToGenerate, imageToGenerate, imageToGenerate);

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
