package com.github.acolote1998.humble_gladiators_2.characters.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/campaign")
public class CharacterController {

    List<FullCharacterResponseDto> getAllFullCharactersForACampaign(@RequestParam Long campaignId) {

    }
}