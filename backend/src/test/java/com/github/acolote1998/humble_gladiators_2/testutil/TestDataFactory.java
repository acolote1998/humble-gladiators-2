package com.github.acolote1998.humble_gladiators_2.testutil;

import com.github.acolote1998.humble_gladiators_2.characters.dto.CreateHeroRequestDto;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterCategory;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.characters.repository.CharacterInstanceRepository;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.characters.service.InventoryService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.item.enums.*;
import com.github.acolote1998.humble_gladiators_2.item.repository.*;
import com.github.acolote1998.humble_gladiators_2.item.templates.*;
import jakarta.persistence.EntityManager;

import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {}

    public static Campaign newCampaign(String userId, String name) {
        Campaign c = new Campaign();
        c.setUserId(userId);
        c.setName(name);
        Theme t = new Theme();
        t.setWantedThemes(List.of("test"));
        t.setUnwantedThemes(List.of());
        c.setTheme(t);
        t.setCampaign(c);
        return c;
    }

    public static Campaign persistCampaign(CampaignRepository repository, String userId, String name) {
        Campaign c = newCampaign(userId, name);
        return repository.save(c);
    }

    public static CharacterInstance createHero(CharacterService characterService, Campaign campaign, String userId, String heroName) {
        return characterService.createHero(campaign, userId, new CreateHeroRequestDto(heroName));
    }

    public static CharacterInstance createTestNPC(CharacterInstanceRepository repository, EntityManager entityManager, Campaign campaign, String userId) {
        CharacterInstance npc = new CharacterInstance();
        npc.setUserId(userId);
        npc.setCampaign(campaign);
        npc.setName("Test NPC");
        npc.setDescription("Test NPC Description");
        npc.setCharacterType(CharacterType.NPC);
        npc.setCategory(CharacterCategory.HUMANOID);
        npc.setTier(1);
        npc.setRarity(1);
        npc.setGoldReward(10);
        npc.setExpReward(20);
        npc.setDiscovered(false);
        
        Stats stats = new Stats();
        stats.setConstitution(14);
        stats.setIntelligence(14);
        stats.setStrength(14);
        stats.setSpeed(14);
        stats.setLuck(14);
        stats.setMaxHp(70);
        stats.setCurrentHp(70);
        stats.setMaxMp(140);
        stats.setCurrentMp(140);
        stats.setHeight(170);
        stats.setWeight(70);
        stats.setLevel(1);
        stats.setCurrentExp(0);
        stats.setExpForNextLevel(100);
        npc.setStats(stats);
        
        npc.setInventory(InventoryService.createBlankInventory());
        return repository.save(npc);
    }

    public static WeaponTemplate createTestWeaponTemplate(WeaponTemplateRepository repository, EntityManager entityManager, Campaign campaign, String userId) {
        WeaponTemplate weapon = new WeaponTemplate();
        weapon.setCampaign(campaign);
        weapon.setUserId(userId);
        weapon.setName("Test Weapon");
        weapon.setDescription("Test Weapon Description");
        weapon.setRarity(1);
        weapon.setTier(1);
        weapon.setValue(10);
        weapon.setQuantity(1);
        weapon.setDiscovered(false);
        weapon.setEquipped(false);
        weapon.setCategory(WeaponCategory.SWORD);
        weapon.setPhysicalDamage(10);
        weapon.setMagicalDamage(0);
        weapon.setRequirement(null); // SKIP_REQUIREMENTS=true
        return repository.save(weapon);
    }

    public static ArmorTemplate createTestArmorTemplate(ArmorTemplateRepository repository, EntityManager entityManager, Campaign campaign, String userId) {
        ArmorTemplate armor = new ArmorTemplate();
        armor.setCampaign(campaign);
        armor.setUserId(userId);
        armor.setName("Test Armor");
        armor.setDescription("Test Armor Description");
        armor.setRarity(1);
        armor.setTier(1);
        armor.setValue(10);
        armor.setQuantity(1);
        armor.setDiscovered(false);
        armor.setEquipped(false);
        armor.setCategory(ArmorCategory.PLATE);
        armor.setPhysicalDefense(5);
        armor.setMagicalDefense(0);
        armor.setRequirement(null);
        return repository.save(armor);
    }

    public static BootsTemplate createTestBootsTemplate(BootsTemplateRepository repository, EntityManager entityManager, Campaign campaign, String userId) {
        BootsTemplate boots = new BootsTemplate();
        boots.setCampaign(campaign);
        boots.setUserId(userId);
        boots.setName("Test Boots");
        boots.setDescription("Test Boots Description");
        boots.setRarity(1);
        boots.setTier(1);
        boots.setValue(10);
        boots.setQuantity(1);
        boots.setDiscovered(false);
        boots.setEquipped(false);
        boots.setCategory(BootsCategory.BOOTS);
        boots.setPhysicalDefense(3);
        boots.setMagicalDefense(0);
        boots.setRequirement(null);
        return repository.save(boots);
    }

    public static HelmetTemplate createTestHelmetTemplate(HelmetTemplateRepository repository, EntityManager entityManager, Campaign campaign, String userId) {
        HelmetTemplate helmet = new HelmetTemplate();
        helmet.setCampaign(campaign);
        helmet.setUserId(userId);
        helmet.setName("Test Helmet");
        helmet.setDescription("Test Helmet Description");
        helmet.setRarity(1);
        helmet.setTier(1);
        helmet.setValue(10);
        helmet.setQuantity(1);
        helmet.setDiscovered(false);
        helmet.setEquipped(false);
        helmet.setCategory(HelmetCategory.HELMET);
        helmet.setPhysicalDefense(3);
        helmet.setMagicalDefense(0);
        helmet.setRequirement(null);
        return repository.save(helmet);
    }

    public static ShieldTemplate createTestShieldTemplate(ShieldTemplateRepository repository, EntityManager entityManager, Campaign campaign, String userId) {
        ShieldTemplate shield = new ShieldTemplate();
        shield.setCampaign(campaign);
        shield.setUserId(userId);
        shield.setName("Test Shield");
        shield.setDescription("Test Shield Description");
        shield.setRarity(1);
        shield.setTier(1);
        shield.setValue(10);
        shield.setQuantity(1);
        shield.setDiscovered(false);
        shield.setEquipped(false);
        shield.setCategory(ShieldCategory.SHIELD);
        shield.setPhysicalDefense(5);
        shield.setMagicalDefense(0);
        shield.setRequirement(null);
        return repository.save(shield);
    }

    public static SpellTemplate createTestSpellTemplate(SpellTemplateRepository repository, EntityManager entityManager, Campaign campaign, String userId) {
        SpellTemplate spell = new SpellTemplate();
        spell.setCampaign(campaign);
        spell.setUserId(userId);
        spell.setName("Test Spell");
        spell.setDescription("Test Spell Description");
        spell.setRarity(1);
        spell.setTier(1);
        spell.setValue(10);
        spell.setQuantity(1);
        spell.setDiscovered(false);
        spell.setEquipped(false);
        spell.setCategory(SpellCategory.FIRE_SPELL);
        spell.setPhysicalDamage(0);
        spell.setMagicalDamage(10);
        spell.setRestoreHp(0);
        spell.setMpCost(5);
        spell.setRequirement(null);
        return repository.save(spell);
    }

    public static ConsumableTemplate createTestConsumableTemplate(ConsumableTemplateRepository repository, EntityManager entityManager, Campaign campaign, String userId) {
        ConsumableTemplate consumable = new ConsumableTemplate();
        consumable.setCampaign(campaign);
        consumable.setUserId(userId);
        consumable.setName("Test Consumable");
        consumable.setDescription("Test Consumable Description");
        consumable.setRarity(1);
        consumable.setTier(1);
        consumable.setValue(10);
        consumable.setQuantity(1);
        consumable.setDiscovered(false);
        consumable.setEquipped(false);
        consumable.setCategory(ConsumablesCategory.FOOD);
        consumable.setRestoreHp(10);
        consumable.setRestoreMp(5);
        consumable.setRequirement(null);
        return repository.save(consumable);
    }

    public static void createAllTestItemTemplates(EntityManager entityManager, Campaign campaign, String userId,
                                                  WeaponTemplateRepository weaponRepo,
                                                  ArmorTemplateRepository armorRepo,
                                                  BootsTemplateRepository bootsRepo,
                                                  HelmetTemplateRepository helmetRepo,
                                                  ShieldTemplateRepository shieldRepo,
                                                  SpellTemplateRepository spellRepo,
                                                  ConsumableTemplateRepository consumableRepo) {
        createTestWeaponTemplate(weaponRepo, entityManager, campaign, userId);
        createTestArmorTemplate(armorRepo, entityManager, campaign, userId);
        createTestBootsTemplate(bootsRepo, entityManager, campaign, userId);
        createTestHelmetTemplate(helmetRepo, entityManager, campaign, userId);
        createTestShieldTemplate(shieldRepo, entityManager, campaign, userId);
        createTestSpellTemplate(spellRepo, entityManager, campaign, userId);
        createTestConsumableTemplate(consumableRepo, entityManager, campaign, userId);
    }
}


