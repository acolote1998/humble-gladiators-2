# Implementation Order for Humble Gladiators 2

## Recent Architectural Improvements ✅ COMPLETED

- **Level-Stats Consolidation**: Moved level functionality into Stats class for better cohesion
- **JPA Optimization**: Added proper annotations and configurations throughout
- **Action Model Enhancement**: Improved with Lombok and nullability handling
- **Battle System Refinement**: Enhanced with proper relationships and team management
- **Complete AI Integration**: Full Gemini API integration with content and image generation
- **Service Layer Architecture**: Complete item services with AI-powered template generation
- **Game Orchestration**: GameService with comprehensive campaign creation workflow
- **Image Generation System**: Advanced pixel-based rendering with 17 drawing methods
- **Documentation Updates**: All model documentation reflects current implementation

## Phase 1: Core Foundation

1. **User Model & Authentication**

   - User entity with email, preferredLanguage
   - Basic authentication setup
   - User registration/login endpoints

2. **Campaign System** ✅ COMPLETED

   - Campaign entity (id, userId, name, themes) ✅ COMPLETED
   - Theme entity (@Embeddable) ✅ COMPLETED
   - Campaign creation endpoint
   - Campaign management (CRUD operations)

3. **Requirements System** ✅ COMPLETED
   - Requirements entity (flexible key-value) ✅ COMPLETED
   - RequirementEntry entity ✅ COMPLETED
   - Requirement validation logic

## Phase 2: Item System

4. **AbstractItem Base** ✅ COMPLETED

   - AbstractItem entity (name, description, rarity, tier, etc.) ✅ COMPLETED
   - Quantity and equipped fields ✅ COMPLETED
   - Campaign relationship ✅ COMPLETED
   - Requirement relationship ✅ COMPLETED
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

12. **Service Layer Architecture** ✅ COMPLETED

    - [x] GameService with campaign creation orchestration
    - [x] All item services (ArmorService, BootsService, ConsumableService, HelmetService, ShieldService, SpellService, WeaponService)
    - [x] CampaignService with campaign management
    - [x] RequirementService with validation logic
    - [x] GeminiService with AI content generation
    - [x] ImageGeneratorService with rendering capabilities
    - [x] Complete repository layer for all entities

13. **Combat Logic** 🔄 IN PROGRESS
    - Turn-based combat flow (structure ready)
    - Damage calculation (Action model ready)
    - Status effects and buffs/debuffs (StateType enum ready)
    - Service layer implementation needed

## Phase 5: AI Integration 🔄 IN PROGRESS

14. **AI Content Generation** 🔄 IN PROGRESS

    - [x] Campaign theme-based content generation
    - [x] Template generation for all item types (Armor, Boots, Consumable, Helmet, Shield, Spell, Weapon)
    - [ ] Character generation for Hero and NPCs
    - [x] Content validation and error handling
    - [x] JSON response parsing and DTO mapping
    - [x] Integration with Requirement system

15. **Gemini AI Integration** ✅ COMPLETED

    - [x] GeminiService with full API connectivity
    - [x] GeminiController with status endpoint
    - [x] Error handling and retry logic
    - [x] API key management and configuration

16. **Image Generation System** ✅ COMPLETED
    - [x] DrawingAction model with 17 drawing methods
    - [x] ImageGeneratorService with pixel-based rendering
    - [x] Support for complex shapes and gradients
    - [x] Image scaling and optimization
    - [x] Color validation and coordinate bounds checking

## Phase 6: Discovery System

17. **Discovery Logic**
    - Item discovery tracking
    - Discovery UI/API endpoints
    - Achievement system integration

## Phase 7: Advanced Features

18. **Battle Replay System**

    - Turn reconstruction
    - Battle history storage
    - Replay viewing functionality

19. **Trading System**
    - Buy/sell functionality (using templates)
    - Market/auction system
    - Gold management

## Phase 8: Polish & Optimization

20. **Performance Optimization**

    - Database indexing
    - Query optimization
    - Caching strategies

21. **Testing & Documentation**

    - Unit tests
    - Integration tests
    - API documentation

22. **Deployment & Monitoring**
    - Production deployment
    - Monitoring setup
    - Error tracking
