# Implementation Order for Humble Gladiators 2

## Recent Architectural Improvements ✅ COMPLETED

- **Level-Stats Consolidation**: Moved level functionality into Stats class for better cohesion
- **JPA Optimization**: Added proper annotations and configurations throughout
- **Action Model Enhancement**: Improved with Lombok and nullability handling
- **Battle System Refinement**: Enhanced with proper relationships and team management
- **Documentation Updates**: All model documentation reflects current implementation

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

## Phase 3: Character System ✅ COMPLETED

8. **Character Model** ✅ COMPLETED

   - AbstractCharacter base class with Stats and Inventory
   - CharacterInstance extends AbstractCharacter implements Discoverable
   - CharacterSnapshot extends AbstractCharacter for battle state
   - CharacterType enum (PLAYER/NPC)
   - Proper JPA annotations and relationships

9. **Level System** ✅ COMPLETED

   - Level functionality consolidated into Stats class
   - Level, currentExp, expForNextLevel fields in Stats
   - Better cohesion between level and stat calculations
   - Single source of truth for character attributes

10. **Inventory System** ✅ COMPLETED
    - Inventory entity with separate lists for each item type
    - Proper @OneToMany relationships with @JoinColumn
    - CascadeType.PERSIST for item management
    - Gold field for currency tracking

## Phase 4: Game Mechanics

11. **Battle System** ✅ COMPLETED

    - Battle entity with proper JPA configuration
    - Turn entity with embedded Action relationship
    - Action entity with Lombok annotations and nullability
    - CharacterSnapshot for battle state preservation
    - Proper team management (starting teams, winning/losing teams)
    - Battle reconstruction capability through turn replay

12. **Combat Logic** 🔄 IN PROGRESS
    - Turn-based combat flow (structure ready)
    - Damage calculation (Action model ready)
    - Status effects and buffs/debuffs (StateType enum ready)
    - Service layer implementation needed

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
