package com.github.acolote1998.humble_gladiators_2.characters.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.repository.CharacterInstanceRepository;
import com.github.acolote1998.humble_gladiators_2.core.dto.CharacterFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CharacterService {
    GeminiService geminiService;
    CharacterInstanceRepository characterInstanceRepository;

    public CharacterService(GeminiService geminiService, CharacterInstanceRepository characterInstanceRepository) {
        this.geminiService = geminiService;
        this.characterInstanceRepository = characterInstanceRepository;
    }

    public List<CharacterInstance> createTenNPCsTierOne(Campaign campaign) {
        List<CharacterInstance> existingCharactersForContext = characterInstanceRepository.findAll();
        List<CharacterFromGeminiDto> generatedDtos = geminiService.generateTenNpcsTierOne(campaign, existingCharactersForContext);
        List<CharacterInstance> test = new ArrayList<>();
        return test;
    }
}
