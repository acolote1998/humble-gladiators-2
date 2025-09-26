package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.repository.ConsumableTemplateRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsumableService {
    private ConsumableTemplateRepository consumableTemplateRepository;
    private GeminiService geminiService;

    public ConsumableService(ConsumableTemplateRepository consumableTemplateRepository, GeminiService geminiService) {
        this.consumableTemplateRepository = consumableTemplateRepository;
        this.geminiService = geminiService;
    }
}
