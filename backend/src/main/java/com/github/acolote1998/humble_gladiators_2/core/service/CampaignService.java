package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.item.service.ArmorService;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {
    GeminiService geminiService;
    CampaignRepository repository;
    ArmorService armorService;

    public CampaignService(GeminiService geminiService, CampaignRepository repository, ArmorService armorService) {
        this.geminiService = geminiService;
        this.repository = repository;
        this.armorService = armorService;
    }

    Campaign save(Campaign campaign) {
        return repository.save(campaign);
    }

    public Campaign createCampaign(Theme campaignTheme) {
        Campaign newCampaign = new Campaign();
        newCampaign.setTheme(campaignTheme);
        newCampaign = save(newCampaign);
        save(newCampaign);
        armorService.createTwentyFiveNewArmorTemplates(newCampaign);
        return newCampaign;
    }

}
