package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CampaignService {
    GeminiService geminiService;
    CampaignRepository repository;
    RunwareService runwareService;

    public CampaignService(GeminiService geminiService, CampaignRepository repository, RunwareService runwareService) {
        this.geminiService = geminiService;
        this.repository = repository;
        this.runwareService = runwareService;
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

    public byte[] generateImageCoverForCampaign(Campaign campaign,
                                                String tier5Characters,
                                                String tier5Armors,
                                                String tier5Boots,
                                                String tier5Helmets,
                                                String tier5Shields,
                                                String tier5Weapons,
                                                String tier5Spells,
                                                String tier5Consumables) {
        String promptForImageGeneration = geminiService.getPositiveCampaignImageCoverPromptForRuneware(
                campaign,
                tier5Characters,
                tier5Armors,
                tier5Boots,
                tier5Helmets,
                tier5Shields,
                tier5Weapons,
                tier5Spells,
                tier5Consumables);
        byte[] generatedImageBytes = runwareService.generateCampaignCoverImageToBytes(promptForImageGeneration, campaign);
        campaign.setCoverImgBytes(generatedImageBytes);
        repository.save(campaign);
        return generatedImageBytes;
    }

    public byte[] generateCardBackImageForCampaign(Campaign campaign,
                                                   String tier5Characters,
                                                   String tier5Armors,
                                                   String tier5Boots,
                                                   String tier5Helmets,
                                                   String tier5Shields,
                                                   String tier5Weapons,
                                                   String tier5Spells,
                                                   String tier5Consumables) {
        String promptForImageGeneration = geminiService.getPositiveCampaignBackCardImagePromptForRuneware(
                campaign,
                tier5Characters,
                tier5Armors,
                tier5Boots,
                tier5Helmets,
                tier5Shields,
                tier5Weapons,
                tier5Spells,
                tier5Consumables);
        byte[] generatedImageBytes = runwareService.generateCampaignCardBackImageToBytes(promptForImageGeneration, campaign);
        campaign.setCardBackImgBytes(generatedImageBytes);
        repository.save(campaign);
        return generatedImageBytes;
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

    public Campaign getCampaignByIdAndUserId(String userId, Long campaignId) {
        return repository.findByUserIdAndId(userId, campaignId);
    }

    public byte[] getBackCardImgForCampaignAndUser(String userId, Long campaignId) {
        Campaign campaign = repository.getCampaignByUserIdAndId(userId, campaignId);
        return campaign.getCardBackImgBytes();
    }
}
