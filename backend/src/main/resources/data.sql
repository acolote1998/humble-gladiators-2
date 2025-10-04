-- Sample data for Humble Gladiators 2
-- This file is automatically executed by Spring Boot on startup

-- Insert Theme
INSERT INTO theme DEFAULT
VALUES;

-- Insert Theme wanted themes (ElementCollection)
INSERT INTO theme_wanted_themes (theme_id, wanted_themes)
VALUES (1, 'Medieval Fantasy');
INSERT INTO theme_wanted_themes (theme_id, wanted_themes)
VALUES (1, 'Knights and Dragons');

-- Insert Theme unwanted themes (ElementCollection)  
INSERT INTO theme_unwanted_themes (theme_id, unwanted_themes)
VALUES (1, 'Modern Technology');
INSERT INTO theme_unwanted_themes (theme_id, unwanted_themes)
VALUES (1, 'Sci-Fi');

-- Insert Campaign
INSERT INTO campaign (user_id, name, theme_id, campaign_creation_state, created_at, updated_at)
VALUES ('user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 'Sample Medieval Campaign', 1, 'GAME_CREATED', NOW(), NOW());

-- Insert Requirements for items
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());
INSERT INTO requirements (campaign_id, created_at, updated_at)
VALUES (1, NOW(), NOW());

-- Insert Inventories for characters
INSERT INTO inventory (gold, created_at, updated_at)
VALUES (100, NOW(), NOW());
INSERT INTO inventory (gold, created_at, updated_at)
VALUES (150, NOW(), NOW());

-- Insert Character Instances (2 NPCs)
INSERT INTO character_instance (user_id, campaign_id, name, description, character_type, category, discovered, tier,
                                rarity, gold_reward, exp_reward, inventory_id, constitution, intelligence, strength,
                                speed, luck, max_hp, current_hp, max_mp, current_mp, height, weight, level, current_exp,
                                exp_for_next_level, created_at, updated_at)
VALUES ('user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 'Sir Gareth the Bold', 'A brave knight wielding a mighty sword',
        'NPC', 'HUMANOID', false, 2, 3, 0, 0, 1, 18, 14, 20, 16, 15, 0, 0, 0, 0, 180, 85, 5, 0, 0, NOW(), NOW());

INSERT INTO character_instance (user_id, campaign_id, name, description, character_type, category, discovered, tier,
                                rarity, gold_reward, exp_reward, inventory_id, constitution, intelligence, strength,
                                speed, luck, max_hp, current_hp, max_mp, current_mp, height, weight, level, current_exp,
                                exp_for_next_level, created_at, updated_at)
VALUES ('user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 'Mystic Elara', 'A powerful mage with ancient knowledge', 'NPC',
        'HUMANOID', false, 3, 4, 0, 0, 2, 16, 22, 12, 18, 17, 0, 0, 0, 0, 165, 60, 7, 0, 0, NOW(), NOW());

-- Insert Armor Templates (2)
INSERT INTO armor_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                            campaign_id, requirement_id, category, physical_defense, magical_defense, created_at,
                            updated_at)
VALUES ('Iron Chainmail', 'Sturdy chainmail armor forged from iron', 2, 2, 840, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 1, 'MAIL', 0, 0, NOW(), NOW());

INSERT INTO armor_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                            campaign_id, requirement_id, category, physical_defense, magical_defense, created_at,
                            updated_at)
VALUES ('Dragon Scale Plate', 'Legendary armor crafted from dragon scales', 5, 4, 1990, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 2, 'PLATE', 0, 0, NOW(), NOW());

-- Insert Boots Templates (2)
INSERT INTO boots_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                            campaign_id, requirement_id, category, physical_defense, magical_defense, created_at,
                            updated_at)
VALUES ('Leather Boots', 'Simple leather boots for basic protection', 1, 1, 320, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 3, 'BOOTS', 0, 0, NOW(), NOW());

INSERT INTO boots_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                            campaign_id, requirement_id, category, physical_defense, magical_defense, created_at,
                            updated_at)
VALUES ('Boots of Swiftness', 'Enchanted boots that enhance movement speed', 4, 3, 1210, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 4, 'COMBAT_BOOTS', 0, 0, NOW(), NOW());

-- Insert Consumable Templates (2)
INSERT INTO consumable_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                                 campaign_id, requirement_id, category, restore_hp, restore_mp, created_at, updated_at)
VALUES ('Health Potion', 'A red potion that restores health', 1, 1, 26, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 5, 'MEDICINE', 0, 0, NOW(), NOW());

INSERT INTO consumable_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                                 campaign_id, requirement_id, category, restore_hp, restore_mp, created_at, updated_at)
VALUES ('Mana Elixir', 'A blue elixir that restores magical energy', 3, 2, 65, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 6, 'DRINK', 0, 0, NOW(), NOW());

-- Insert Helmet Templates (2)
INSERT INTO helmet_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                             campaign_id, requirement_id, category, physical_defense, magical_defense, created_at,
                             updated_at)
VALUES ('Iron Helm', 'A basic iron helmet for head protection', 2, 2, 720, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 7, 'HELMET', 0, 0, NOW(), NOW());

INSERT INTO helmet_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                             campaign_id, requirement_id, category, physical_defense, magical_defense, created_at,
                             updated_at)
VALUES ('Crown of Wisdom', 'A magical crown that enhances intelligence', 5, 3, 1640, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 8, 'DECORATIVE_HELMET', 0, 0, NOW(), NOW());

-- Insert Shield Templates (2)
INSERT INTO shield_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                             campaign_id, requirement_id, category, physical_defense, magical_defense, created_at,
                             updated_at)
VALUES ('Wooden Shield', 'A simple wooden shield for basic defense', 1, 1, 400, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 9, 'SHIELD', 0, 0, NOW(), NOW());

INSERT INTO shield_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                             campaign_id, requirement_id, category, physical_defense, magical_defense, created_at,
                             updated_at)
VALUES ('Tower Shield of Fortitude', 'A massive shield providing excellent protection', 4, 4, 1600, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 10, 'SHIELD', 0, 0, NOW(), NOW());

-- Insert Spell Templates (2)
INSERT INTO spell_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                            campaign_id, requirement_id, category, physical_damage, magical_damage, restore_hp,
                            created_at, updated_at)
VALUES ('Fireball', 'A blazing sphere of fire that damages enemies', 2, 2, 720, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 11, 'FIRE_SPELL', 0, 1, 0, NOW(), NOW());

INSERT INTO spell_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                            campaign_id, requirement_id, category, physical_damage, magical_damage, restore_hp,
                            created_at, updated_at)
VALUES ('Healing Light', 'A divine spell that restores health to allies', 3, 3, 1080, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 12, 'HEALING_SPELL', 0, 0, 1, NOW(), NOW());

-- Insert Weapon Templates (2)
INSERT INTO weapon_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                             campaign_id, requirement_id, category, physical_damage, magical_damage, created_at,
                             updated_at)
VALUES ('Iron Sword', 'A well-crafted iron sword with a sharp edge', 2, 2, 720, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 13, 'SWORD', 1, 0, NOW(), NOW());

INSERT INTO weapon_template (name, description, rarity, tier, value, discovered, quantity, equipped, user_id,
                             campaign_id, requirement_id, category, physical_damage, magical_damage, created_at,
                             updated_at)
VALUES ('Staff of Arcane Power', 'A magical staff imbued with mystical energy', 4, 3, 1320, false, 0, false,
        'user_33LC7WwFVRN0mkH7ZAfWCbFOBFl', 1, 14, 'STAFF', 0, 1, NOW(), NOW());