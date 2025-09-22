# Implementation Order for Humble Gladiators 2

## Phase 1: Core Foundation

1. **User Model & Authentication**

   - User entity with email, preferredLanguage
   - Basic authentication setup
   - User registration/login endpoints

2. **Campaign System**

   - Campaign entity (id, userId, name, themes)
   - Campaign creation endpoint
   - Campaign management (CRUD operations)

3. **Requirements System**
   - Requirements entity (flexible key-value)
   - RequirementEntry entity
   - Requirement validation logic

## Phase 2: Item System

4. **AbstractItem Base**

   - AbstractItem entity (name, description, rarity, tier, etc.)
   - Quantity and equipped fields
   - Discovery system integration

5. **Item Templates** ✅ COMPLETED

   - Template classes for AI-generated items
   - WeaponTemplate, SpellTemplate, ConsumableTemplate
   - Equipment templates (HelmetTemplate, ShieldTemplate, ArmorTemplate, BootsTemplate)
   - Simple data storage extending AbstractItem (no interfaces)

6. **Item Instances** ✅ COMPLETED

   - Instance classes for player-owned items
   - WeaponInstance, SpellInstance, ConsumableInstance
   - Equipment instances (HelmetInstance, ShieldInstance, ArmorInstance, BootsInstance)
   - Extend AbstractItem with interfaces (Tradeable, Equipable, etc.)
   - Reference templates via ManyToOne relationship

7. **Template vs Instance Architecture** ✅ COMPLETED
   - Separate packages for templates and instances
   - Fixed JPA inheritance conflicts using composition
   - Both template and instance tables in database

## Phase 3: Character System

8. **Character Model**

   - Character entity (stats, level, inventory, etc.)
   - CharacterType enum (PLAYER/NPC)
   - Character-Campaign relationships

9. **Level System**

   - Level entity (experience, level progression)
   - Level-up logic and stat increases

10. **Inventory System**
    - Inventory entity (List<ItemInstance>)
    - Inventory management (add/remove items)
    - Equipment system (equip/unequip items)

## Phase 4: Game Mechanics

11. **Battle System**

    - Battle entity (teams, turns, state)
    - Turn entity (actions, character tracking)
    - Action entity (damage, healing, effects)

12. **Combat Logic**
    - Turn-based combat flow
    - Damage calculation
    - Status effects and buffs/debuffs

## Phase 5: AI Integration

13. **AI Content Generation**
    - Campaign theme-based content generation
    - Template generation (Item/Spell/Enemy templates)
    - Content validation and storage

## Phase 6: Discovery System

14. **Discovery Logic**
    - Item discovery tracking
    - Discovery UI/API endpoints
    - Achievement system integration

## Phase 7: Advanced Features

15. **Battle Replay System**

    - Turn reconstruction
    - Battle history storage
    - Replay viewing functionality

16. **Trading System**
    - Buy/sell functionality (using templates)
    - Market/auction system
    - Gold management

## Phase 8: Polish & Optimization

17. **Performance Optimization**

    - Database indexing
    - Query optimization
    - Caching strategies

18. **Testing & Documentation**

    - Unit tests
    - Integration tests
    - API documentation

19. **Deployment & Monitoring**
    - Production deployment
    - Monitoring setup
    - Error tracking
