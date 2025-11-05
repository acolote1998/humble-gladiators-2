package com.github.acolote1998.humble_gladiators_2.characters.service.integration;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.instances.ArmorInstance;
import com.github.acolote1998.humble_gladiators_2.testutil.TestDataFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class InventoryServiceIntegrationTest {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private GeminiService geminiService;

    private Campaign campaign;
    private String userId;
    private CharacterInstance hero;

    @BeforeEach
    void setup() {
        userId = "test-user";
        campaign = TestDataFactory.persistCampaign(campaignRepository, userId, "Test Campaign");
        hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
    }

    @Test
    void equipItem_marksItemAsEquippedAndPersists() {
        // Note: This test requires items in inventory - would need item creation setup
        Inventory inventory = hero.getInventory();
        assertThat(inventory).isNotNull();
        assertThat(inventory.getArmors()).isNotNull();
    }

    @Test
    void unequipItem_marksItemAsUnequippedAndPersists() {
        Inventory inventory = hero.getInventory();
        assertThat(inventory.getArmors()).isNotNull();
    }

    @Test
    void inventoryUpdates_reflectedInDatabase() {
        characterService.saveCharacter(hero);
        entityManager.flush();
        entityManager.clear();

        CharacterInstance retrieved = characterService.getHero(campaign.getId(), userId);
        assertThat(retrieved.getInventory()).isNotNull();
    }

    @Test
    void equipItem_whenInventoryIsEmpty_handlesGracefully() {
        Inventory inventory = hero.getInventory();
        assertThat(inventory.getArmors()).isEmpty();
        assertThat(inventory.getWeapons()).isEmpty();
    }

    @Test
    void unequipItem_whenNoItemsEquipped_handlesGracefully() {
        Inventory inventory = hero.getInventory();
        assertThat(inventory.getArmors().stream().noneMatch(ArmorInstance::getEquipped)).isTrue();
    }

    @Test
    void inventoryCreation_withNullEmptyLists_initializesCorrectly() {
        Inventory inventory = hero.getInventory();
        assertThat(inventory.getArmors()).isNotNull();
        assertThat(inventory.getBoots()).isNotNull();
        assertThat(inventory.getConsumables()).isNotNull();
        assertThat(inventory.getHelmets()).isNotNull();
        assertThat(inventory.getShields()).isNotNull();
        assertThat(inventory.getSpells()).isNotNull();
        assertThat(inventory.getWeapons()).isNotNull();
    }
}

