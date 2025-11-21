package com.github.acolote1998.humble_gladiators_2.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserModerationService {
    GeminiService geminiService;

    @Autowired
    public UserModerationService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }
}
