package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
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

    public Campaign createCampaign(GameCreationDtoRequest newCampaignDto, String userId) {
        Campaign newCampaign = new Campaign();
        List<String> wantedThemes = newCampaignDto.theme().wantedThemes();
        List<String> unwantedThemes = newCampaignDto.theme().unwantedThemes();
        Theme campaignTheme = new Theme();
        campaignTheme.setCampaign(newCampaign);
        campaignTheme.setUnwantedThemes(unwantedThemes);
        campaignTheme.setWantedThemes(wantedThemes);
        newCampaign.setTheme(campaignTheme);
        newCampaign.setUserId(userId);
        newCampaign.setName(newCampaignDto.campaignName());
        newCampaign = save(newCampaign);
        save(newCampaign);
        return newCampaign;
    }

    public Campaign getCampaignBeingCreatedByUserId(String userId) {
        List<Campaign> possibleCampaigns = repository.getCampaignsByUserId(userId);
        Campaign campaignBeingCreated = possibleCampaigns
                .stream()
                .filter(campaign -> !campaign.getCampaignCreationState()
                        .equals(CampaignCreationStateType.GAME_CREATED))
                .findFirst()
                .orElse(null);
        return campaignBeingCreated;
    }

    public List<Campaign> getAllCampainsForAUser(String userId) {
        return repository.findAllByUserId(userId);
    }
}
