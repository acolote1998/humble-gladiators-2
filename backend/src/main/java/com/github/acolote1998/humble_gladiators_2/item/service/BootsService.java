package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.repository.BootsTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.BootsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BootsService {
    private BootsTemplateRepository bootsTemplateRepository;
    private GeminiService geminiService;

    public List<BootsTemplate> createTwentyFiveNewBootsTemplates(Campaign campaign) {
    }

    public BootsService(BootsTemplateRepository bootsTemplateRepository, GeminiService geminiService) {
        this.bootsTemplateRepository = bootsTemplateRepository;
        this.geminiService = geminiService;
    }
}
