package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignService {
    GeminiService geminiService;
    CampaignRepository repository;

    public CampaignService(GeminiService geminiService, CampaignRepository repository) {
        this.geminiService = geminiService;
        this.repository = repository;
    }

    Campaign save(Campaign campaign) {
        return repository.save(campaign);
    }

    public Campaign createCampaign(GameCreationDtoRequest newCampaignDto) {
        Campaign newCampaign = new Campaign();
        List<String> wantedThemes = newCampaignDto.theme().wantedThemes();
        List<String> unwantedThemes = newCampaignDto.theme().unwantedThemes();
        Theme campaignTheme = new Theme();
        campaignTheme.setCampaign(newCampaign);
        campaignTheme.setUnwantedThemes(unwantedThemes);
        campaignTheme.setWantedThemes(wantedThemes);
        newCampaign.setTheme(campaignTheme);
        newCampaign.setName(newCampaignDto.campaignName());
        newCampaign = save(newCampaign);
        save(newCampaign);
        return newCampaign;
    }

}
