package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {

    @Mock
    private GeminiService geminiService;

    @Mock
    private CampaignRepository repository;

    @Mock
    private RunwareService runwareService;

    @InjectMocks
    private CampaignService campaignService;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;

    private Campaign campaign;

    @BeforeEach
    void setUp() {
        campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        campaign.setUserId(USER_ID);
        campaign.setName("Test Campaign");
        
        Theme theme = new Theme();
        theme.setWantedThemes(List.of("fantasy", "medieval"));
        theme.setUnwantedThemes(List.of("sci-fi"));
        theme.setCampaign(campaign);
        campaign.setTheme(theme);
    }

    @Test
    void createCampaign_ShouldCreateCampaignWithTheme() {
        // Arrange
        GameCreationDtoRequest.ThemeDtoRequest themeRequest = 
                new GameCreationDtoRequest.ThemeDtoRequest(
                        List.of("fantasy", "medieval"),
                        List.of("sci-fi")
                );
        GameCreationDtoRequest request = new GameCreationDtoRequest("New Campaign", themeRequest);
        
        when(repository.save(any(Campaign.class))).thenAnswer(invocation -> {
            Campaign savedCampaign = invocation.getArgument(0);
            savedCampaign.setId(CAMPAIGN_ID);
            return savedCampaign;
        });

        // Act
        Campaign result = campaignService.createCampaign(request, USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals("New Campaign", result.getName());
        assertEquals(USER_ID, result.getUserId());
        assertNotNull(result.getTheme());
        assertEquals(List.of("fantasy", "medieval"), result.getTheme().getWantedThemes());
        assertEquals(List.of("sci-fi"), result.getTheme().getUnwantedThemes());
        verify(repository, times(2)).save(any(Campaign.class));
    }

    @Test
    void generateImageCoverForCampaign_ShouldGenerateAndSaveImage() {
        // Arrange
        byte[] imageBytes = new byte[]{1, 2, 3};
        String prompt = "test prompt";
        
        when(geminiService.getPositiveCampaignImageCoverPromptForRuneware(
                eq(campaign), anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString())).thenReturn(prompt);
        when(runwareService.generateCampaignCoverImageToBytes(eq(prompt), eq(campaign))).thenReturn(imageBytes);
        when(repository.save(campaign)).thenReturn(campaign);

        // Act
        byte[] result = campaignService.generateImageCoverForCampaign(
                campaign, "chars", "armors", "boots", "helmets", "shields", "weapons", "spells", "consumables");

        // Assert
        assertArrayEquals(imageBytes, result);
        assertArrayEquals(imageBytes, campaign.getCoverImgBytes());
        verify(repository).save(campaign);
    }

    @Test
    void generateCardBackImageForCampaign_ShouldGenerateAndSaveImage() {
        // Arrange
        byte[] imageBytes = new byte[]{1, 2, 3};
        String prompt = "test prompt";
        
        when(geminiService.getPositiveCampaignBackCardImagePromptForRuneware(
                eq(campaign), anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString())).thenReturn(prompt);
        when(runwareService.generateCampaignCardBackImageToBytes(eq(prompt), eq(campaign))).thenReturn(imageBytes);
        when(repository.save(campaign)).thenReturn(campaign);

        // Act
        byte[] result = campaignService.generateCardBackImageForCampaign(
                campaign, "chars", "armors", "boots", "helmets", "shields", "weapons", "spells", "consumables");

        // Assert
        assertArrayEquals(imageBytes, result);
        assertArrayEquals(imageBytes, campaign.getCardBackImgBytes());
        verify(repository).save(campaign);
    }

    @Test
    void getCampaignBeingCreatedByUserId_WhenCampaignExists_ShouldReturnCampaign() {
        // Arrange
        campaign.setCampaignCreationState(CampaignCreationStateType.STARTING_NEW_CAMPAIGN);
        Campaign createdCampaign = new Campaign();
        createdCampaign.setCampaignCreationState(CampaignCreationStateType.GAME_CREATED);
        
        when(repository.getCampaignsByUserId(USER_ID)).thenReturn(List.of(campaign, createdCampaign));

        // Act
        Campaign result = campaignService.getCampaignBeingCreatedByUserId(USER_ID);

        // Assert
        assertEquals(campaign, result);
    }

    @Test
    void getCampaignBeingCreatedByUserId_WhenNoCampaignExists_ShouldReturnNull() {
        // Arrange
        when(repository.getCampaignsByUserId(USER_ID)).thenReturn(Collections.emptyList());

        // Act
        Campaign result = campaignService.getCampaignBeingCreatedByUserId(USER_ID);

        // Assert
        assertNull(result);
    }

    @Test
    void getAllCampainsForAUser_ShouldReturnListOfCampaigns() {
        // Arrange
        List<Campaign> campaigns = List.of(campaign);
        when(repository.findAllByUserId(USER_ID)).thenReturn(campaigns);

        // Act
        List<Campaign> result = campaignService.getAllCampainsForAUser(USER_ID);

        // Assert
        assertEquals(campaigns, result);
    }

    @Test
    void getCampaignByIdAndUserId_ShouldReturnCampaign() {
        // Arrange
        when(repository.findByUserIdAndId(USER_ID, CAMPAIGN_ID)).thenReturn(campaign);

        // Act
        Campaign result = campaignService.getCampaignByIdAndUserId(USER_ID, CAMPAIGN_ID);

        // Assert
        assertEquals(campaign, result);
    }

    @Test
    void getBackCardImgForCampaignAndUser_ShouldReturnImageBytes() {
        // Arrange
        byte[] imageBytes = new byte[]{1, 2, 3};
        campaign.setCardBackImgBytes(imageBytes);
        when(repository.getCampaignByUserIdAndId(USER_ID, CAMPAIGN_ID)).thenReturn(campaign);

        // Act
        byte[] result = campaignService.getBackCardImgForCampaignAndUser(USER_ID, CAMPAIGN_ID);

        // Assert
        assertArrayEquals(imageBytes, result);
    }
}


