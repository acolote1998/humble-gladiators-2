package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.item.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private CampaignService campaignService;

    @Mock
    private ArmorService armorService;

    @Mock
    private BootsService bootsService;

    @Mock
    private ConsumableService consumableService;

    @Mock
    private HelmetService helmetService;

    @Mock
    private ShieldService shieldService;

    @Mock
    private SpellService spellService;

    @Mock
    private WeaponService weaponService;

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private GameService gameService;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;

    private Campaign campaign;

    @BeforeEach
    void setUp() {
        // Set @Value fields
        ReflectionTestUtils.setField(gameService, "GENERATE_ALL", false);
        ReflectionTestUtils.setField(gameService, "GENERATE_IMAGES", false);
        ReflectionTestUtils.setField(gameService, "GENERATE_CAMPAIGN_COVER", false);
        ReflectionTestUtils.setField(gameService, "GENERATE_CAMPAIGN_CARD_BACK", false);
        ReflectionTestUtils.setField(gameService, "GENERATE_NPCS", false);
        ReflectionTestUtils.setField(gameService, "GENERATE_ARMORS", false);
        ReflectionTestUtils.setField(gameService, "GENERATE_BOOTS", false);
        ReflectionTestUtils.setField(gameService, "GENERATE_CONSUMABLES", false);
        ReflectionTestUtils.setField(gameService, "GENERATE_HELMETS", false);
        ReflectionTestUtils.setField(gameService, "GENERATE_SHIELDS", false);
        ReflectionTestUtils.setField(gameService, "GENERATE_SPELLS", false);
        ReflectionTestUtils.setField(gameService, "GENERATE_WEAPONS", false);

        campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        campaign.setUserId(USER_ID);
        campaign.setName("Test Campaign");
        
        Theme theme = new Theme();
        theme.setWantedThemes(List.of("fantasy"));
        theme.setUnwantedThemes(Collections.emptyList());
        theme.setCampaign(campaign);
        campaign.setTheme(theme);
    }

    @Test
    void getCampaignService_ShouldReturnCampaignService() {
        // Act
        CampaignService result = gameService.getCampaignService();

        // Assert
        assertEquals(campaignService, result);
    }

    @Test
    void getShortReportOfAIGeneratedContent_ShouldCallAllServices() {
        // Arrange
        when(armorService.getShortAIGeneratedReport(CAMPAIGN_ID)).thenReturn(Collections.emptyMap());
        when(bootsService.getShortAIGeneratedReport(CAMPAIGN_ID)).thenReturn(Collections.emptyMap());
        when(consumableService.getShortAIGeneratedReport(CAMPAIGN_ID)).thenReturn(Collections.emptyMap());
        when(helmetService.getShortAIGeneratedReport(CAMPAIGN_ID)).thenReturn(Collections.emptyMap());
        when(shieldService.getShortAIGeneratedReport(CAMPAIGN_ID)).thenReturn(Collections.emptyMap());
        when(spellService.getShortAIGeneratedReport(CAMPAIGN_ID)).thenReturn(Collections.emptyMap());
        when(weaponService.getShortAIGeneratedReport(CAMPAIGN_ID)).thenReturn(Collections.emptyMap());
        when(characterService.getShortAIGeneratedReport(CAMPAIGN_ID)).thenReturn(Collections.emptyMap());

        // Act
        gameService.getShortReportOfAIGeneratedContent(campaign);

        // Assert
        verify(armorService).getShortAIGeneratedReport(CAMPAIGN_ID);
        verify(bootsService).getShortAIGeneratedReport(CAMPAIGN_ID);
        verify(consumableService).getShortAIGeneratedReport(CAMPAIGN_ID);
        verify(helmetService).getShortAIGeneratedReport(CAMPAIGN_ID);
        verify(shieldService).getShortAIGeneratedReport(CAMPAIGN_ID);
        verify(spellService).getShortAIGeneratedReport(CAMPAIGN_ID);
        verify(weaponService).getShortAIGeneratedReport(CAMPAIGN_ID);
        verify(characterService).getShortAIGeneratedReport(CAMPAIGN_ID);
    }

    @Test
    void startGame_ShouldCreateCampaign() throws InterruptedException {
        // Arrange
        GameCreationDtoRequest.ThemeDtoRequest themeRequest = 
                new GameCreationDtoRequest.ThemeDtoRequest(
                        List.of("fantasy"),
                        Collections.emptyList()
                );
        GameCreationDtoRequest request = new GameCreationDtoRequest("New Campaign", themeRequest);
        
        when(campaignService.createCampaign(request, USER_ID)).thenReturn(campaign);
        when(campaignService.save(any(Campaign.class))).thenReturn(campaign);

        // Act
        Campaign result = gameService.startGame(request, USER_ID);

        // Assert
        assertNotNull(result);
        verify(campaignService).createCampaign(request, USER_ID);
    }
}

