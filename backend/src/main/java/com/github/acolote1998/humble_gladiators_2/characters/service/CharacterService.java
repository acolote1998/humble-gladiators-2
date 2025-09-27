package com.github.acolote1998.humble_gladiators_2.characters.service;

import com.github.acolote1998.humble_gladiators_2.characters.repository.CharacterInstanceRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CharacterService {
    GeminiService geminiService;
    CharacterInstanceRepository characterInstanceRepository;

    public CharacterService(GeminiService geminiService, CharacterInstanceRepository characterInstanceRepository) {
        this.geminiService = geminiService;
        this.characterInstanceRepository = characterInstanceRepository;
    }
}
