package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.GeminiResponseDto;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.DrawingAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // Gemini API endpoint for content generation
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";


    private final RestTemplate restTemplate = new RestTemplate();

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

    //    public List<DrawingAction> generateDrawingActionsTest() {
    public String generateDrawingActionsTest() {

        String prompt = """
                Context: You need to generate a List<DrawingAction> that will be used to generate a buffered image in a Java program.
                The List<DrawingAction> generated should after execution generate dragon in RPG style.
                
                IMPORTANT: You must ONLY return the List<DrawingAction> creation code. Do NOT create your own DrawingAction class or any other classes.
                
                DrawingAction Constructor:
                new DrawingAction(int drawingMethod, int initialX, int initialY, int red, int green, int blue, int alpha, int size, int width, int height, int radius)
                
                Drawing Methods:
                - 0: SQUARE (uses size parameter)
                - 1: RECTANGLE (uses width and height parameters)
                - 2: HORIZONTAL_LINE (uses width parameter for length)
                - 3: VERTICAL_LINE (uses height parameter for length)
                - 4: CIRCLE (uses radius parameter)
                - 5: HOLLOW_SQUARE (uses size parameter)
                - 6: DOT (single pixel, uses initialX and initialY only)
                - 7: TRIANGLE_UP (uses size parameter)
                - 8: TRIANGLE_DOWN (uses size parameter)
                - 9: TRIANGLE_LEFT (uses size parameter)
                - 10: TRIANGLE_RIGHT (uses size parameter)
                - 11: DIAMOND (uses size parameter)
                
                Color Values: All color values (red, green, blue, alpha) must be between 0-255
                Canvas Size: Assume a 100x100 pixel canvas
                
                Methods that you can use:
                //DRAWING METHOD "0" - SQUARE
                    public static List<Pixel> drawSquare(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        for (int dx = 0; dx < action.size; dx++) {
                            for (int dy = 0; dy < action.size; dy++) {
                                pixelsToDraw.add(new Pixel(action.initialX + dx, action.initialY + dy, action.red, action.green, action.blue, action.alpha));
                            }
                        }
                        return pixelsToDraw;
                    }
                
                    //DRAWING METHOD "1" - RECTANGLE
                    public static List<Pixel> drawRectangle(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        int width = action.getWidth();
                        int height = action.getHeight();
                        for (int dx = 0; dx < width; dx++) {
                            for (int dy = 0; dy < height; dy++) {
                                pixelsToDraw.add(new Pixel(
                                        action.getInitialX() + dx,
                                        action.getInitialY() + dy,
                                        action.getRed(),
                                        action.getGreen(),
                                        action.getBlue(),
                                        action.getAlpha()
                                ));
                            }
                        }
                        return pixelsToDraw;
                    }
                
                    //DRAWING METHOD "2" - HORIZONTAL LINE
                    public static List<Pixel> drawHorizontalLine(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        int length = action.getWidth(); // width field used as line length
                        for (int dx = 0; dx < length; dx++) {
                            pixelsToDraw.add(new Pixel(
                                    action.getInitialX() + dx,
                                    action.getInitialY(),
                                    action.getRed(),
                                    action.getGreen(),
                                    action.getBlue(),
                                    action.getAlpha()
                            ));
                        }
                        return pixelsToDraw;
                    }
                
                    //DRAWING METHOD "3" - VERTICAL LINE
                    public static List<Pixel> drawVerticalLine(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        int length = action.getHeight(); // use height as line length
                
                        for (int dy = 0; dy < length; dy++) {
                            pixelsToDraw.add(new Pixel(
                                    action.getInitialX(),
                                    action.getInitialY() + dy,
                                    action.getRed(),
                                    action.getGreen(),
                                    action.getBlue(),
                                    action.getAlpha()
                            ));
                        }
                
                        return pixelsToDraw;
                    }
                
                    //DRAWING METHOD "4" - CIRCLE
                    public static List<Pixel> drawCircle(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        int radius = action.getRadius();
                        int cx = action.getInitialX();
                        int cy = action.getInitialY();
                
                        for (int dx = -radius; dx <= radius; dx++) {
                            for (int dy = -radius; dy <= radius; dy++) {
                                if (dx * dx + dy * dy <= radius * radius) {
                                    pixelsToDraw.add(new Pixel(
                                            cx + dx,
                                            cy + dy,
                                            action.getRed(),
                                            action.getGreen(),
                                            action.getBlue(),
                                            action.getAlpha()
                                    ));
                                }
                            }
                        }
                        return pixelsToDraw;
                    }
                
                    //DRAWING METHOD "5" - HOLLOW SQUARE
                    public static List<Pixel> drawHollowSquare(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        int size = action.getSize();
                        for (int i = 0; i < size; i++) {
                            // Top and bottom edges
                            pixelsToDraw.add(new Pixel(action.getInitialX() + i, action.getInitialY(), action.getRed(), action.getGreen(), action.getBlue(), action.getAlpha()));
                            pixelsToDraw.add(new Pixel(action.getInitialX() + i, action.getInitialY() + size - 1, action.getRed(), action.getGreen(), action.getBlue(), action.getAlpha()));
                            // Left and right edges
                            pixelsToDraw.add(new Pixel(action.getInitialX(), action.getInitialY() + i, action.getRed(), action.getGreen(), action.getBlue(), action.getAlpha()));
                            pixelsToDraw.add(new Pixel(action.getInitialX() + size - 1, action.getInitialY() + i, action.getRed(), action.getGreen(), action.getBlue(), action.getAlpha()));
                        }
                        return pixelsToDraw;
                    }
                
                    //DRAWING METHOD "6" - DOT (single pixel)
                    public static List<Pixel> drawDot(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        pixelsToDraw.add(new Pixel(
                                action.getInitialX(),
                                action.getInitialY(),
                                action.getRed(),
                                action.getGreen(),
                                action.getBlue(),
                                action.getAlpha()
                        ));
                        return pixelsToDraw;
                    }
                
                    //DRAWING METHOD "7" - TRIANGLE UP
                    public static List<Pixel> drawTriangleUp(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        int size = action.getSize();
                        int centerX = action.getInitialX();
                        int centerY = action.getInitialY();
                        
                        for (int dy = 0; dy < size; dy++) {
                            int width = dy + 1;
                            int startX = centerX - dy / 2;
                            for (int dx = 0; dx < width; dx++) {
                                pixelsToDraw.add(new Pixel(
                                        startX + dx,
                                        centerY + dy,
                                        action.getRed(),
                                        action.getGreen(),
                                        action.getBlue(),
                                        action.getAlpha()
                                ));
                            }
                        }
                        return pixelsToDraw;
                    }
                
                    //DRAWING METHOD "8" - TRIANGLE DOWN
                    public static List<Pixel> drawTriangleDown(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        int size = action.getSize();
                        int centerX = action.getInitialX();
                        int centerY = action.getInitialY();
                        
                        for (int dy = 0; dy < size; dy++) {
                            int width = size - dy;
                            int startX = centerX - (size - dy - 1) / 2;
                            for (int dx = 0; dx < width; dx++) {
                                pixelsToDraw.add(new Pixel(
                                        startX + dx,
                                        centerY + dy,
                                        action.getRed(),
                                        action.getGreen(),
                                        action.getBlue(),
                                        action.getAlpha()
                                ));
                            }
                        }
                        return pixelsToDraw;
                    }
                
                    //DRAWING METHOD "9" - TRIANGLE LEFT
                    public static List<Pixel> drawTriangleLeft(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        int size = action.getSize();
                        int centerX = action.getInitialX();
                        int centerY = action.getInitialY();
                        
                        for (int dx = 0; dx < size; dx++) {
                            int height = dx + 1;
                            int startY = centerY - dx / 2;
                            for (int dy = 0; dy < height; dy++) {
                                pixelsToDraw.add(new Pixel(
                                        centerX + dx,
                                        startY + dy,
                                        action.getRed(),
                                        action.getGreen(),
                                        action.getBlue(),
                                        action.getAlpha()
                                ));
                            }
                        }
                        return pixelsToDraw;
                    }
                
                    //DRAWING METHOD "10" - TRIANGLE RIGHT
                    public static List<Pixel> drawTriangleRight(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        int size = action.getSize();
                        int centerX = action.getInitialX();
                        int centerY = action.getInitialY();
                        
                        for (int dx = 0; dx < size; dx++) {
                            int height = size - dx;
                            int startY = centerY - (size - dx - 1) / 2;
                            for (int dy = 0; dy < height; dy++) {
                                pixelsToDraw.add(new Pixel(
                                        centerX + dx,
                                        startY + dy,
                                        action.getRed(),
                                        action.getGreen(),
                                        action.getBlue(),
                                        action.getAlpha()
                                ));
                            }
                        }
                        return pixelsToDraw;
                    }
                
                    //DRAWING METHOD "11" - DIAMOND
                    public static List<Pixel> drawDiamond(DrawingAction action) {
                        List<Pixel> pixelsToDraw = new ArrayList<>();
                        int size = action.getSize();
                        int centerX = action.getInitialX();
                        int centerY = action.getInitialY();
                        
                        // Draw diamond using two triangles (up and down)
                        for (int dy = 0; dy < size; dy++) {
                            int width = dy + 1;
                            int startX = centerX - dy / 2;
                            for (int dx = 0; dx < width; dx++) {
                                pixelsToDraw.add(new Pixel(
                                        startX + dx,
                                        centerY - dy,
                                        action.getRed(),
                                        action.getGreen(),
                                        action.getBlue(),
                                        action.getAlpha()
                                ));
                            }
                        }
                        
                        for (int dy = 1; dy < size; dy++) {
                            int width = size - dy;
                            int startX = centerX - (size - dy - 1) / 2;
                            for (int dx = 0; dx < width; dx++) {
                                pixelsToDraw.add(new Pixel(
                                        startX + dx,
                                        centerY + dy,
                                        action.getRed(),
                                        action.getGreen(),
                                        action.getBlue(),
                                        action.getAlpha()
                                ));
                            }
                        }
                        return pixelsToDraw;
                    }
                
                Example of how the methods will be executed:
                
                    public static void GenerateImage(List<Pixel> pixels, String outputPath, Integer width, Integer height) {
                        try {
                
                            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                
                            // Set pixels from the list
                            for (Pixel p : pixels) {
                                if (isValidCoordinate(p.getX(), p.getY(), width, height)) {
                                    if (isValidColorValue(p.getAlpha()) &&
                                            isValidColorValue(p.getRed()) &&
                                            isValidColorValue(p.getGreen()) &&
                                            isValidColorValue(p.getBlue())) {
                                        int pixelValue = (p.getAlpha() << 24) | (p.getRed() << 16) | (p.getGreen() << 8) | p.getBlue();
                                        image.setRGB(p.getX(), p.getY(), pixelValue);
                                    } else {
                                        log.warn("Ignoring pixel " + p.toString() + ", corrupted color code.");
                                    }
                                } else {
                                    log.warn("Ignoring pixel " + p.toString() + ", coordinate out of canvas");
                                }
                            }
                
                            // Save image
                            ImageIO.write(image, "png", new File(outputPath));
                            log.info("Image successfully created");
                
                        } catch (Exception e) {
                            log.error("Error creating image: " + e.getMessage());
                        }
                    }
                
                    public static List<Pixel> generatePixelsForImage(List<DrawingAction> actions) {
                        List<Pixel> resultPixels = new ArrayList<>();
                        actions.forEach(drawingAction -> {
                            switch (drawingAction.getDrawingMethod()) {
                                //SQUARE
                                case 0: {
                                    resultPixels.addAll(DrawingAction.drawSquare(drawingAction));
                                    break;
                                }
                                //RECTANGLE
                                case 1: {
                                    resultPixels.addAll(DrawingAction.drawRectangle(drawingAction));
                                    break;
                                }
                                //HORIZONTAL LINE
                                case 2: {
                                    resultPixels.addAll(DrawingAction.drawHorizontalLine(drawingAction));
                                    break;
                                }
                                //VERTICAL LINE
                                case 3: {
                                    resultPixels.addAll(DrawingAction.drawVerticalLine(drawingAction));
                                    break;
                                }
                                //CIRCLE
                                case 4: {
                                    resultPixels.addAll(DrawingAction.drawCircle(drawingAction));
                                    break;
                                }
                                //HOLLOW SQUARE
                                case 5: {
                                    resultPixels.addAll(DrawingAction.drawHollowSquare(drawingAction));
                                    break;
                                }
                                //DOT
                                case 6: {
                                    resultPixels.addAll(DrawingAction.drawDot(drawingAction));
                                    break;
                                }
                                //TRIANGLE UP
                                case 7: {
                                    resultPixels.addAll(DrawingAction.drawTriangleUp(drawingAction));
                                    break;
                                }
                                //TRIANGLE DOWN
                                case 8: {
                                    resultPixels.addAll(DrawingAction.drawTriangleDown(drawingAction));
                                    break;
                                }
                                //TRIANGLE LEFT
                                case 9: {
                                    resultPixels.addAll(DrawingAction.drawTriangleLeft(drawingAction));
                                    break;
                                }
                                //TRIANGLE RIGHT
                                case 10: {
                                    resultPixels.addAll(DrawingAction.drawTriangleRight(drawingAction));
                                    break;
                                }
                                //DIAMOND
                                case 11: {
                                    resultPixels.addAll(DrawingAction.drawDiamond(drawingAction));
                                    break;
                                }
                                default: {
                                    log.error("Invalid Drawing Method: " + drawingAction.getDrawingMethod());
                                    break;
                                }
                            }
                        });
                        return resultPixels;
                    }
                
                REQUIRED OUTPUT FORMAT:
                Return ONLY this format - no explanations, no additional code:
                
                ```
                List<DrawingAction> actions = new ArrayList<>();
                actions.add(new DrawingAction(4, 50, 50, 139, 69, 19, 255, 0, 0, 0, 25)); // Dragon body (brown circle)
                actions.add(new DrawingAction(7, 50, 25, 139, 69, 19, 255, 8, 0, 0, 0)); // Dragon head (triangle up)
                actions.add(new DrawingAction(6, 45, 30, 255, 0, 0, 255, 0, 0, 0, 0)); // Left eye (red dot)
                actions.add(new DrawingAction(6, 55, 30, 255, 0, 0, 255, 0, 0, 0, 0)); // Right eye (red dot)
                actions.add(new DrawingAction(11, 30, 40, 139, 69, 19, 255, 6, 0, 0, 0)); // Dragon scale (diamond)
                actions.add(new DrawingAction(11, 70, 40, 139, 69, 19, 255, 6, 0, 0, 0)); // Dragon scale (diamond)
                actions.add(new DrawingAction(8, 50, 75, 139, 69, 19, 255, 6, 0, 0, 0)); // Dragon tail (triangle down)
                ```
                
                Instructions:
                1. Create a List<DrawingAction> for a dragon in RPG style
                2. Use the exact constructor format shown above
                3. Use appropriate drawing methods (0-11)
                4. Set unused parameters to 0
                5. Return ONLY the List creation code, nothing else
                6. Do NOT include any class definitions, imports, or explanations
                7. Do NOT use setters - use the constructor only
                8. Make sure coordinates fit within 100x100 canvas
                """;

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
            throw e;
        }
    }

}
