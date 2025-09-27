package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.repository.HelmetTemplateRepository;

public class HelmetService {
    GeminiService geminiService;
    HelmetTemplateRepository helmetTemplateRepository;

    public HelmetService(GeminiService geminiService, HelmetTemplateRepository helmetTemplateRepository) {
        this.geminiService = geminiService;
        this.helmetTemplateRepository = helmetTemplateRepository;
    }
}
