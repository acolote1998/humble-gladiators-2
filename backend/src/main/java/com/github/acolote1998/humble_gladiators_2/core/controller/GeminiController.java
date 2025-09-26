package com.github.acolote1998.humble_gladiators_2.core.controller;

import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    /**
     * Status check endpoint to verify Gemini API connectivity
     * Sends a test prompt to Gemini and returns the response
     *
     * @return ResponseEntity containing the Gemini API response
     */
    @GetMapping("/status")
    public ResponseEntity<String> status() throws InterruptedException {
        String response = geminiService.sendTestPrompt();

        // Return the response from Gemini
        return ResponseEntity.ok(response);
    }
}
