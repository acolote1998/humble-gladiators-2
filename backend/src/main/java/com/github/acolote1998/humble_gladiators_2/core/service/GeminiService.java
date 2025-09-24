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
                The List<DrawingAction> generated should after execution generate a smiley face emoji.
                
                Model:
                public class DrawingAction {
                    private int drawingMethod;
                    private int initialX;
                    private int initialY;
                    private int red;
                    private int green;
                    private int blue;
                    private int alpha;
                    private int size;       // For square or hollow square
                    private int width;      // For rectangle
                    private int height;     // For rectangle
                    private int radius;     // For circle
                }
                
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
                
                    //DRAWING METHOD "5" - CIRCLE
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
                                default: {
                                    log.error("Invalid Drawing Method: " + drawingAction.getDrawingMethod());
                                    break;
                                }
                            }
                        });
                        return resultPixels;
                    }
                
                Example of desired output:
                    '
                            List<DrawingAction> actions = new ArrayList<>();
                            actions.add(new DrawingAction(5, 12, 12, 255, 0, 0, 255, 4, 4, 6, 4));
                            actions.add(new DrawingAction(3, 12, 12, 0, 255, 0, 255, 4, 4, 6, 4));
                '
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
