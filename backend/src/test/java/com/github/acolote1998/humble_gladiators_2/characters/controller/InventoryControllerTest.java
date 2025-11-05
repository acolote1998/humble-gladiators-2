package com.github.acolote1998.humble_gladiators_2.characters.controller;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.item.instances.ArmorInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.BootsInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.HelmetInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.ShieldInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.WeaponInstance;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
import com.github.acolote1998.humble_gladiators_2.item.templates.BootsTemplate;
import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;
import com.github.acolote1998.humble_gladiators_2.item.templates.ShieldTemplate;
import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.acolote1998.humble_gladiators_2.config.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InventoryController.class, excludeAutoConfiguration = OAuth2ResourceServerAutoConfiguration.class)
@Import(TestSecurityConfig.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CharacterService characterService;

    @MockitoBean
    private CampaignService campaignService;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;
    private static final Long ITEM_ID = 100L;

    private Jwt jwt;
    private CharacterInstance hero;
    private Campaign campaign;

    @BeforeEach
    void setUp() {
        jwt = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .claim("sub", USER_ID)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        campaign.setUserId(USER_ID);

        hero = new CharacterInstance();
        hero.setCampaign(campaign);
        hero.setStats(new Stats());
        hero.setInventory(createBlankInventory());
    }

    private Inventory createBlankInventory() {
        Inventory inventory = new Inventory();
        inventory.setArmors(new java.util.ArrayList<>());
        inventory.setBoots(new java.util.ArrayList<>());
        inventory.setConsumables(new java.util.ArrayList<>());
        inventory.setHelmets(new java.util.ArrayList<>());
        inventory.setShields(new java.util.ArrayList<>());
        inventory.setSpells(new java.util.ArrayList<>());
        inventory.setWeapons(new java.util.ArrayList<>());
        inventory.setGold(0);
        return inventory;
    }

    private ArmorInstance createArmorInstance() {
        ArmorInstance armor = new ArmorInstance();
        armor.setId(ITEM_ID);
        armor.setCampaign(campaign);
        ArmorTemplate template = new ArmorTemplate();
        template.setCategory(com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory.PLATE);
        armor.setTemplate(template);
        return armor;
    }

    private BootsInstance createBootsInstance() {
        BootsInstance boots = new BootsInstance();
        boots.setId(ITEM_ID);
        boots.setCampaign(campaign);
        BootsTemplate template = new BootsTemplate();
        template.setCategory(com.github.acolote1998.humble_gladiators_2.item.enums.BootsCategory.COMBAT_BOOTS);
        boots.setTemplate(template);
        return boots;
    }

    private HelmetInstance createHelmetInstance() {
        HelmetInstance helmet = new HelmetInstance();
        helmet.setId(ITEM_ID);
        helmet.setCampaign(campaign);
        HelmetTemplate template = new HelmetTemplate();
        template.setCategory(com.github.acolote1998.humble_gladiators_2.item.enums.HelmetCategory.HELMET);
        helmet.setTemplate(template);
        return helmet;
    }

    private ShieldInstance createShieldInstance() {
        ShieldInstance shield = new ShieldInstance();
        shield.setId(ITEM_ID);
        shield.setCampaign(campaign);
        ShieldTemplate template = new ShieldTemplate();
        template.setCategory(com.github.acolote1998.humble_gladiators_2.item.enums.ShieldCategory.SHIELD);
        shield.setTemplate(template);
        return shield;
    }

    private WeaponInstance createWeaponInstance() {
        WeaponInstance weapon = new WeaponInstance();
        weapon.setId(ITEM_ID);
        weapon.setCampaign(campaign);
        WeaponTemplate template = new WeaponTemplate();
        template.setCategory(com.github.acolote1998.humble_gladiators_2.item.enums.WeaponCategory.SWORD);
        weapon.setTemplate(template);
        return weapon;
    }

    @Test
    @WithMockUser
    void equipArmorToHero_ShouldReturnArmorInstance() throws Exception {
        // Arrange
        ArmorInstance armor = createArmorInstance();

        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        when(characterService.equipArmor(hero, ITEM_ID, USER_ID)).thenReturn(armor);

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/equip/armor/{itemId}",
                        CAMPAIGN_ID, ITEM_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void unequipArmorsFromHero_ShouldReturnSuccess() throws Exception {
        // Arrange
        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        doNothing().when(characterService).unequipArmors(hero, USER_ID);

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/unequip/armor", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.campaignId").value(CAMPAIGN_ID));
    }

    @Test
    @WithMockUser
    void equipBootsToHero_ShouldReturnBootsInstance() throws Exception {
        // Arrange
        BootsInstance boots = createBootsInstance();

        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        when(characterService.equipBoots(hero, ITEM_ID, USER_ID)).thenReturn(boots);

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/equip/boots/{itemId}",
                        CAMPAIGN_ID, ITEM_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void unequipBootsFromHero_ShouldReturnSuccess() throws Exception {
        // Arrange
        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        doNothing().when(characterService).unequipBoots(hero, USER_ID);

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/unequip/boots", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.campaignId").value(CAMPAIGN_ID));
    }

    @Test
    @WithMockUser
    void equipHelmetToHero_ShouldReturnHelmetInstance() throws Exception {
        // Arrange
        HelmetInstance helmet = createHelmetInstance();

        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        when(characterService.equipHelmet(hero, ITEM_ID, USER_ID)).thenReturn(helmet);

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/equip/helmet/{itemId}",
                        CAMPAIGN_ID, ITEM_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void unequipHelmetsFromHero_ShouldReturnSuccess() throws Exception {
        // Arrange
        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        doNothing().when(characterService).unequipHelmets(hero, USER_ID);

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/unequip/helmet", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.campaignId").value(CAMPAIGN_ID));
    }

    @Test
    @WithMockUser
    void equipShieldToHero_ShouldReturnShieldInstance() throws Exception {
        // Arrange
        ShieldInstance shield = createShieldInstance();

        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        when(characterService.equipShield(hero, ITEM_ID, USER_ID)).thenReturn(shield);

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/equip/shield/{itemId}",
                        CAMPAIGN_ID, ITEM_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void unequipShieldsFromHero_ShouldReturnSuccess() throws Exception {
        // Arrange
        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        doNothing().when(characterService).unequipShields(hero, USER_ID);

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/unequip/shield", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.campaignId").value(CAMPAIGN_ID));
    }

    @Test
    @WithMockUser
    void equipWeaponToHero_ShouldReturnWeaponInstance() throws Exception {
        // Arrange
        WeaponInstance weapon = createWeaponInstance();

        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        when(characterService.equipWeapon(hero, ITEM_ID, USER_ID)).thenReturn(weapon);

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/equip/weapon/{itemId}",
                        CAMPAIGN_ID, ITEM_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void unequipWeaponsFromHero_ShouldReturnSuccess() throws Exception {
        // Arrange
        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        doNothing().when(characterService).unequipWeapons(hero, USER_ID);

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/unequip/weapon", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.campaignId").value(CAMPAIGN_ID));
    }

    @Test
    @WithMockUser
    void equipArmorToHero_WhenItemNotFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        when(characterService.equipArmor(hero, ITEM_ID, USER_ID))
                .thenThrow(new NoSuchElementException("Item not found"));

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/equip/armor/{itemId}",
                        CAMPAIGN_ID, ITEM_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not equip item: item not found."));
    }

    @Test
    @WithMockUser
    void handleItemNotFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        when(characterService.equipBoots(hero, ITEM_ID, USER_ID))
                .thenThrow(new NoSuchElementException("Item not found"));

        // Act & Assert
        mockMvc.perform(patch("/api/campaign/{campaignId}/character-instances/hero/equip/boots/{itemId}",
                        CAMPAIGN_ID, ITEM_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not equip item: item not found."));
    }
}

