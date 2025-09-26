package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import org.springframework.stereotype.Service;

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

    public Campaign createCampaign(Theme campaignTheme) throws InterruptedException {
        Campaign newCampaign = new Campaign();
        newCampaign.setTheme(campaignTheme);
        newCampaign = save(newCampaign);

        //TESTING
        geminiService.generateListOfArmors(newCampaign);
        //TESTING

        save(newCampaign);
        return newCampaign;
    }

}
