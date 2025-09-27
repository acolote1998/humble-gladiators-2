package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.repository.WeaponTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeaponService {
    GeminiService geminiService;
    WeaponTemplateRepository weaponTemplateRepository;

    public WeaponService(GeminiService geminiService, WeaponTemplateRepository weaponTemplateRepository) {
        this.geminiService = geminiService;
        this.weaponTemplateRepository = weaponTemplateRepository;
    }
}
