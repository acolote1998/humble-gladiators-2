# Backend Test Implementation Plan

## Objective

This document serves as a specification for an AI agent to implement a comprehensive test suite for the backend of this project. The plan outlines a structured, phased approach to ensure thorough test coverage of all controllers and services within the specified packages.

## Scope

This testing plan covers **backend only** and focuses on the following packages:

- booster
- character
- core
- item

## Phase 1: Unit Tests ✅ COMPLETED

This phase must be completed **in its entirety** before Phase 2 can begin. All unit tests for all listed packages must be implemented and verified before proceeding to integration tests.

**Status**: ✅ **COMPLETED** - All Phase 1 unit tests have been implemented and verified.

Tests must be written package by package in this **exact order**:

1. booster ✅
2. character ✅
3. core ✅
4. item ✅

For each package, implement unit tests for all controllers and services listed below.

### Package: booster

- [x] Controllers
  - [x] BoosterController
- [x] Services
  - [x] BoosterService

### Package: character

- [x] Controllers
  - [x] CharacterController
  - [x] InventoryController
- [x] Services
  - [x] CharacterService
  - [x] InventoryService

### Package: core

- [x] Controllers
  - [x] BattleController
  - [x] CampaignController
  - [x] GameController
  - [x] GeminiController
- [x] Services
  - [x] BattleService
  - [x] BattleCleanupService
  - [x] BattleUtil
  - [x] CampaignService
  - [x] GameService
  - [x] GeminiService
  - [x] RequirementService
  - [x] RunwareService

### Package: item

- [x] Controllers
  - [x] ArmorTemplateController
  - [x] BootsTemplateController
  - [x] ConsumableTemplateController
  - [x] HelmetTemplateController
  - [x] ShieldTemplateController
  - [x] SpellTemplateController
  - [x] WeaponTemplateController
- [x] Services
  - [x] ArmorService
  - [x] BootsService
  - [x] ConsumableService
  - [x] HelmetService
  - [x] ShieldService
  - [x] SpellService
  - [x] WeaponService

**IMPORTANT**: Phase 2 cannot begin until all unit tests for all packages listed above are complete and verified.

## Phase 2: Integration Tests

**This phase starts only after Phase 1 is 100% complete.**

### Overview

Integration tests verify that multiple components work together correctly. Unlike Phase 1 unit tests which mock all dependencies, Phase 2 integration tests should:

- Use **real Spring application context** (`@SpringBootTest`)
- Use **real repositories** with **H2 in-memory database**
- Test **end-to-end flows** through controllers → services → repositories
- Test **database interactions** and **transaction boundaries**
- Mock **only external APIs** (Gemini, Runware) using `@MockBean`
- Test **Spring Security integration** with real authentication/authorization
- Verify **data persistence** and **retrieval** from database

### Testing Patterns

**For Service Integration Tests:**
- Use `@SpringBootTest` with `@AutoConfigureTestDatabase(replace = Replace.NONE)` for H2 in-memory database
- Use `@Transactional` to rollback test data after each test
- Use real repositories (not mocked)
- Mock only external services (GeminiService, RunwareService)

**For Controller Integration Tests:**
- Use `@SpringBootTest` with `@AutoConfigureMockMvc`
- Use real services (not mocked)
- Use `@WithMockUser` or `@WithJwt` for security testing
- Test full HTTP request/response cycle

**Database Configuration:**
- Use **H2 in-memory database** for all integration tests (fast and isolated)   
- Test profile config is provided in `src/test/resources/application-test.yml` (already in repo). Activate with `@ActiveProfiles("test")`.
- Ensure H2 dependency is in `pom.xml` test scope:
  ```xml
  <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>test</scope>
  </dependency>
  ```
- The `application-test.yml` sets:
  - `spring.datasource.url=jdbc:h2:mem:testdb`
  - `spring.jpa.hibernate.ddl-auto=create-drop`
  - `spring.jpa.show-sql=false`
  - `spring.sql.init.mode=always`
  - Project feature flags for boosters, battles, inventory, content generation, balance, rarities/tiers, and stat modifiers
  - With this file, tests do not require a `.env`

**External API Mocking:**
- Use `@MockBean` for GeminiService and RunwareService
- Mock HTTP responses to avoid actual API calls during tests
- Mock all external API methods to prevent network calls

### Test Structure

Each integration test should:
1. Set up test data in database (via repositories or `@Sql` scripts)
2. Execute the test scenario (service method or HTTP request)
3. Verify the result (database state, response, side effects)
4. Clean up (automatic with `@Transactional` or manual cleanup)

Integration tests should follow the same package-by-package order as Phase 1:

1. booster
2. character
3. core
4. item

### Package: booster

**Services:**
- [ ] `BoosterServiceIntegrationTest`
  - Test `openItemBooster()` with real CharacterService and ItemService
  - Test `openCharacterBooster()` with real CharacterService
  - Test `isItemBoosterAvailableForToday()` with real database queries
  - Test `isCharacterBoosterAvailableForToday()` with real database queries     
  - Verify items/characters are persisted to database
  - Verify inventory updates are persisted
  - Test booster availability across day boundaries (midnight transitions)
  - Test booster opening failure scenarios (service exception mid-operation) - verify rollback
  - Test that if item generation fails, booster opening is rolled back
  - Test that partial inventory updates don't persist on failure
  - Test booster opening when battle is ongoing (concurrent state check)

**Controllers:**
- [ ] `BoosterControllerIntegrationTest`
  - Test `POST /api/campaign/{campaignId}/item-booster` with real services      
  - Test `POST /api/campaign/{campaignId}/character-booster` with real services 
  - Test `GET /api/campaign/{campaignId}/item-booster/available` with real database                                                                             
  - Test `GET /api/campaign/{campaignId}/character-booster/available` with real database                                                                        
  - Verify full HTTP request/response cycle
  - Test authentication/authorization with real security context
  - Test user isolation: User A cannot open User B's boosters (verify userId filtering)
  - Test exception handling: Verify DailyBoosterAlreadyOpened returns 409 Conflict
  - Test exception handling: Verify InvalidBooster returns 409 Conflict

### Package: character

**Services:**
- [ ] `CharacterServiceIntegrationTest`
  - Test `createHero()` - verify hero is persisted with all relationships (Stats, Inventory)                                                                    
  - Test `getHero()` - verify database query returns correct hero
  - Test `getHero()` - verify no LazyInitializationException when accessing inventory and relationships
  - Test `getDailyEnemy()` - verify database query and randomization
  - Test `getDailyEnemy()` - throws exception when no NPCs exist for campaign (verify database state)
  - Test `createTenNPCsOfDesiredTier()` - verify multiple NPCs are created and persisted                                                                        
  - Test `createTenNPCsOfDesiredTier()` - with invalid tier (should handle gracefully)
  - Test character creation with requirements validation
  - Test character updates (level up, stat changes) persist correctly
  - Test creating hero with invalid tier/rarity values (if applicable)
  - Test creating hero without required fields (name, stats, etc.) - verify constraint violations
  - Test hero creation with full inventory initialization (all item lists initialized)
  - Test character creation with requirement validation and persistence
  - Test character updates propagate to inventory correctly

- [ ] `InventoryServiceIntegrationTest`
  - Test `equipItem()` - verify item is marked as equipped and persisted        
  - Test `unequipItem()` - verify item is marked as unequipped and persisted    
  - Test inventory updates are reflected in database
  - Test inventory validation (equipment slots, requirements)
  - Test `equipItem()` when inventory is empty (edge case)
  - Test `unequipItem()` when no items are equipped (edge case)
  - Test inventory creation with null/empty lists

**Controllers:**
- [ ] `CharacterControllerIntegrationTest`
  - Test `POST /api/campaign/{campaignId}/character-instances/hero` - full flow with database                                                                   
  - Test `GET /api/campaign/{campaignId}/character-instances/hero` - verify database retrieval                                                                  
  - Test `GET /api/campaign/{campaignId}/character-instances` - verify list retrieval                                                                           
  - Test error handling with real database constraints
  - Test user isolation: User A cannot access User B's hero (should return 404)
  - Test user isolation: User A cannot retrieve User B's character instances
  - Test exception handling: Verify HeroDoesNotExist returns 404 Not Found
  - Test exception handling: Verify HeroAlreadyCreated returns 409 Conflict
  - Test getHero() throws exception when hero doesn't exist (verify database state)

- [ ] `InventoryControllerIntegrationTest`
  - Test `POST /api/campaign/{campaignId}/character-instances/{characterId}/equip/{itemId}` - full flow                                                         
  - Test `POST /api/campaign/{campaignId}/character-instances/{characterId}/unequip/{itemId}` - full flow                                                       
  - Verify equipment changes persist in database
  - Test validation with real database state
  - Test user isolation: User A cannot equip items from User B's inventory
  - Test user isolation: User A cannot access User B's character inventory

### Package: core

**Services:**
- [ ] `CampaignServiceIntegrationTest`
  - Test `createCampaign()` - verify campaign and theme are persisted
  - Test `createCampaign()` - without userId throws constraint violation
  - Test `createCampaign()` - without theme throws constraint violation
  - Test `getCampaignByIdAndUserId()` - verify database query
  - Test `getCampaignByIdAndUserId()` - with invalid userId + campaignId combination returns null
  - Test `getAllCampaignsByUserId()` - verify list retrieval
  - Test campaign state updates persist correctly
  - Test campaign deletion cascades correctly to all related entities:
    * Characters (Hero, NPCs)
    * Items (all templates and instances)
    * Battles
    * Boosters
    * Requirements
  - Test orphaned entities are cleaned up after cascade delete

- [ ] `BattleServiceIntegrationTest`
  - Test `createNewBattle()` - verify battle, teams, and turns are persisted    
  - Test `createNewBattle()` - with non-existent campaignId throws exception
  - Test `createNewBattle()` - with characters from different campaigns fails
  - Test `createNewBattle()` - when team lists are empty (should fail validation)
  - Test `performPhysicalAttack()` - verify turn is created and persisted       
  - Test `performPhysicalAttack()` - verify that if character save fails, turn creation is rolled back
  - Test `getUpdatedBattle()` - verify battle state updates persist
  - Test `getUpdatedBattle()` - verify properly loads all required relationships (no LazyInitializationException)
  - Test `getUpdatedBattle()` - with battle that has no turns yet
  - Test `getRewardForBattle()` - verify reward creation and persistence        
  - Test battle winner/loser determination with real database
  - Test battle state transitions:
    * CREATED → ONGOING
    * ONGOING → FINISHED (with winner/loser)
    * FINISHED → CONSOLIDATED (via cleanup service)
  - Test state consistency (ongoing=false when winners exist)
  - Test currentCharacterToPlay updates correctly during turns
  - Test battle cleanup with date-based queries
  - Test battle availability across midnight boundary
  - Test daily enemy generation on day boundaries
  - Test battle creation persists all relationships:
    * Battle → Campaign
    * Battle → Teams (CharacterInstances)
    * Battle → Turns (empty list)
    * Battle → CharacterSnapshots
  - Test all bidirectional relationships are correctly maintained
  - Test triggerNpcTurn() when it's player's turn (should not execute)

- [ ] `GameServiceIntegrationTest`
  - Test `startGame()` - verify full game creation flow
  - Test campaign creation with all related entities (items, characters, etc.)  
  - Test state transitions during game creation
  - Mock GeminiService and RunwareService with `@MockBean` but use real repositories and H2 database
  - Test that if GeminiService call fails mid-campaign-creation, database state is consistent (rollback)
  - Test rollback behavior when RunwareService fails
  - Test GeminiService timeout handling
  - Test GeminiService invalid response handling
  - Test RunwareService failure during image generation
  - Test partial failures (some items generated, others fail)
  - Verify graceful degradation when external services fail

- [ ] `BattleCleanupServiceIntegrationTest`
  - Test `cleanupOldBattles()` - verify database deletion based on date
  - Test cleanup only affects old battles (not current ones)
  - Verify orphaned relationships are handled
  - Test cleanup with empty battle list
  - Test cleanup preserves characters correctly (doesn't cascade-delete characters still in use)
  - Test cleanup with battles that have null teams
  - Test cleanup boundary conditions (exactly 1 day old vs. 1 day + 1 second)
  - Test cleanup handles leap years correctly

- [ ] `RequirementServiceIntegrationTest`
  - Test requirement mapping from DTOs
  - Test requirement validation with real database entities
  - Test requirement validation with complex requirement chains
  - Test requirement persistence with null/empty entries

**Controllers:**
- [ ] `BattleControllerIntegrationTest`
  - Test `POST /api/campaign/{campaignId}/battle` - full battle creation flow   
  - Test `POST /api/campaign/{campaignId}/battle/{battleId}/turn` - full turn execution                                                                         
  - Test `GET /api/campaign/{campaignId}/battle/{battleId}` - verify battle retrieval                                                                           
  - Test battle state transitions with real database
  - Test user isolation: User A cannot access User B's battle state
  - Test exception handling: Verify InvalidBattle returns 409 Conflict
  - Test exception handling: Verify InvalidTurn returns 409 Conflict
  - Test exception handling: Verify InvalidAttemptBattleOngoing returns 409 Conflict

- [ ] `CampaignControllerIntegrationTest`
  - Test `POST /api/campaign` - full campaign creation with database
  - Test `GET /api/campaign` - verify campaign list retrieval
  - Test `GET /api/campaign/{campaignId}` - verify single campaign retrieval    
  - Test campaign updates persist correctly
  - Test user isolation: User A cannot retrieve User B's campaign (should return 404)
  - Test user isolation: Campaign list only returns campaigns for authenticated user

- [ ] `GameControllerIntegrationTest`
  - Test `POST /api/game` - full game creation flow
  - Test `GET /api/game/{campaignId}/status` - verify state retrieval
  - Test game creation progress tracking

- [ ] `GeminiControllerIntegrationTest`
  - Test `GET /api/public/gemini/status` - verify service integration
  - Mock GeminiService but test full HTTP flow

### Package: item

**Services:**
- [ ] `ArmorServiceIntegrationTest`
  - Test `getAllArmorTemplatesForACampaignAndUser()` - verify database query returns only user's items
  - Test `getRandomArmorTemplateForItemBooster()` - verify randomization and query                                                                              
  - Test `getRandomArmorTemplateForItemBooster()` - when no items match criteria (edge case)
  - Test `instanceFromArmorTemplate()` - verify instance creation and persistence (all properties)
  - Test `saveArmor()` - verify template persistence
  - Test item requirement validation during instance creation

- [ ] `BootsServiceIntegrationTest`
  - Test `getAllBootsTemplatesForACampaignAndUser()` - verify database query returns only user's items
  - Test `getRandomBootsTemplateForItemBooster()` - verify randomization and query                                                                              
  - Test `getRandomBootsTemplateForItemBooster()` - when no items match criteria (edge case)
  - Test `instanceFromBootsTemplate()` - verify instance creation and persistence (all properties)
  - Test `saveBoots()` - verify template persistence
  - Test item requirement validation during instance creation

- [ ] `ConsumableServiceIntegrationTest`
  - Test `getAllConsumableTemplatesForACampaignAndUser()` - verify database query returns only user's items
  - Test `getRandomConsumableTemplateForItemBooster()` - verify randomization and query
  - Test `getRandomConsumableTemplateForItemBooster()` - when no items match criteria (edge case)
  - Test `instanceFromConsumableTemplate()` - verify instance creation and persistence (all properties)
  - Test `saveConsumable()` - verify template persistence
  - Test item requirement validation during instance creation

- [ ] `HelmetServiceIntegrationTest`
  - Test `getAllHelmetTemplatesForACampaignAndUser()` - verify database query returns only user's items
  - Test `getRandomHelmetTemplateForItemBooster()` - verify randomization and query                                                                             
  - Test `getRandomHelmetTemplateForItemBooster()` - when no items match criteria (edge case)
  - Test `instanceFromHelmetTemplate()` - verify instance creation and persistence (all properties)
  - Test `saveHelmet()` - verify template persistence
  - Test item requirement validation during instance creation

- [ ] `ShieldServiceIntegrationTest`
  - Test `getAllShieldTemplatesForACampaignAndUser()` - verify database query returns only user's items
  - Test `getRandomShieldTemplateForItemBooster()` - verify randomization and query                                                                             
  - Test `getRandomShieldTemplateForItemBooster()` - when no items match criteria (edge case)
  - Test `instanceFromShieldTemplate()` - verify instance creation and persistence (all properties)
  - Test `saveShield()` - verify template persistence
  - Test item requirement validation during instance creation

- [ ] `SpellServiceIntegrationTest`
  - Test `getAllSpellTemplatesForACampaignAndUser()` - verify database query returns only user's items
  - Test `getRandomSpellTemplateForItemBooster()` - verify randomization and query                                                                              
  - Test `getRandomSpellTemplateForItemBooster()` - when no items match criteria (edge case)
  - Test `instanceFromSpellTemplate()` - verify instance creation and persistence (all properties)
  - Test `saveSpell()` - verify template persistence
  - Test item requirement validation during instance creation

- [ ] `WeaponServiceIntegrationTest`
  - Test `getAllWeaponTemplatesForACampaignAndUser()` - verify database query returns only user's items
  - Test `getRandomWeaponTemplateForItemBooster()` - verify randomization and query                                                                             
  - Test `getRandomWeaponTemplateForItemBooster()` - when no items match criteria (edge case)
  - Test `instanceFromWeaponTemplate()` - verify instance creation and persistence (all properties)
  - Test `saveWeapon()` - verify template persistence
  - Test item requirement validation during instance creation

**Controllers:**
- [ ] `ArmorTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/armor-templates` - verify full flow with database                                                                      
  - Test authentication/authorization with real security
  - Test user isolation: User A cannot access User B's armor templates

- [ ] `BootsTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/boots-templates` - verify full flow with database                                                                      
  - Test authentication/authorization with real security
  - Test user isolation: User A cannot access User B's boots templates

- [ ] `ConsumableTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/consumable-templates` - verify full flow with database                                                                 
  - Test authentication/authorization with real security
  - Test user isolation: User A cannot access User B's consumable templates

- [ ] `HelmetTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/helmet-templates` - verify full flow with database                                                                     
  - Test authentication/authorization with real security
  - Test user isolation: User A cannot access User B's helmet templates

- [ ] `ShieldTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/shield-templates` - verify full flow with database                                                                     
  - Test authentication/authorization with real security
  - Test user isolation: User A cannot access User B's shield templates

- [ ] `SpellTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/spell-templates` - verify full flow with database                                                                      
  - Test authentication/authorization with real security
  - Test user isolation: User A cannot access User B's spell templates

- [ ] `WeaponTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/weapon-templates` - verify full flow with database                                                                     
  - Test authentication/authorization with real security
  - Test user isolation: User A cannot access User B's weapon templates

### Best Practices

1. **Database Setup:**
   - Use `@Sql` annotations for complex test data setup
   - Use `@Transactional` for automatic rollback (unless testing transactions)  
   - Use `@DirtiesContext` sparingly (only when needed)
   - Use `@Rollback(false)` when explicitly testing transaction rollback behavior

2. **Test Data:**
   - Create helper methods for common test entities (Campaign, Character, etc.) 
   - Use builders or factories for complex object creation (e.g., `CampaignTestDataBuilder`)
   - Ensure test data is isolated and doesn't interfere between tests
   - Create shared test utility classes:
     * `TestDataFactory` - For creating test entities
     * `SecurityTestUtils` - For creating JWT tokens for testing
     * `DatabaseTestUtils` - For database cleanup helpers
     * `DateTestUtils` - For manipulating dates in tests

3. **Assertions:**
   - Verify database state after operations (not just return values)
   - Check relationships are persisted correctly (OneToMany, ManyToMany)        
   - Verify cascading operations work correctly
   - Verify entity relationships are correctly initialized (no LazyInitializationException)
   - Use `EntityManager.flush()` to ensure database state is current before assertions

4. **Performance:**
   - Keep integration tests focused on integration points
   - Avoid testing things already covered in unit tests
   - Use `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)` for faster tests when possible
   - Monitor SQL queries in test logs to detect N+1 query problems
   - Verify ORDER BY clauses work correctly in H2

5. **External Dependencies:**
   - Always mock GeminiService and RunwareService
   - Consider using WireMock or MockServer for HTTP mocking if needed
   - Test error scenarios (API failures, timeouts)
   - Test partial failures (some items generated, others fail)
   - Verify graceful degradation when external services fail
   - Test retry logic if implemented

6. **Security:**
   - Test with real Spring Security context
   - Verify user isolation (users can only access their own data)
   - Test authorization rules with different user roles if applicable
   - Test that userId is correctly extracted from JWT
   - Test that repository queries filter by userId correctly
   - Test that users cannot modify other users' campaigns/characters/items

7. **Exception Handling:**
   - Test that exception responses return correct HTTP status codes
   - Test that exception messages are properly formatted
   - Verify GlobalExceptionHandler catches all exceptions correctly
   - Test database constraint violations (@NotNull, foreign keys, unique constraints)
   - Test entity validation annotations (Jakarta Validation)

8. **Lazy Loading & N+1 Queries:**
   - Enable SQL logging in test profile to detect N+1 queries
   - Test that DTOs properly initialize required lazy relationships
   - Test that controllers don't cause LazyInitializationException
   - Verify relationships are loaded within transaction boundaries

9. **Transaction Testing:**
   - Test partial failure scenarios (what happens if one operation succeeds but another fails?)
   - Test transaction rollback on exceptions
   - Test that failed operations don't leave database in inconsistent state
   - Use `@Transactional(isolation = Isolation.SERIALIZABLE)` for concurrent operation tests

10. **Edge Cases:**
    - Test with empty collections and null values
    - Test date/time edge cases (midnight boundaries, day transitions, leap years)
    - Test random data with fixed seeds for reproducibility
    - Test boundary conditions (exactly N days old vs. N+1 days old)

### Common Issues and Solutions

1. **Database Connection:**
   - Ensure H2 is in classpath: `com.h2database:h2` (test scope)
   - Use `src/test/resources/application-test.yml` (already provided) with `@ActiveProfiles("test")`
   - H2 database will be created fresh for each test run (in-memory)

2. **Transaction Rollback:**
   - Use `@Transactional` on test methods for automatic cleanup
   - If testing transaction behavior, use `@Rollback(false)` and manual cleanup
   - Test rollback scenarios explicitly (partial failures, exceptions)

3. **Entity Relationships:**
   - Ensure all required entities are persisted before testing relationships    
   - Use `EntityManager.flush()` to ensure database state is current
   - Watch for LazyInitializationException - initialize relationships within transaction boundaries
   - Use `@EntityGraph` or JOIN FETCH in queries when lazy relationships need to be loaded

4. **Date/Time Testing:**
   - Use fixed dates or `@MockBean` Clock for date-dependent tests
   - Test timezone handling if applicable
   - Test day boundary conditions (midnight transitions)
   - Test leap year scenarios

5. **Random Data:**
   - Seed random number generators for reproducible tests
   - Or use `@MockBean` for random-dependent services
   - Identify all random-dependent operations:
     * Booster opening (item/character selection)
     * Daily enemy selection
     * NPC creation (name/description generation)
     * Item/character template selection

6. **LazyInitializationException:**
   - Ensure lazy relationships are accessed within transaction boundaries
   - Use `@Transactional` on test methods when accessing lazy-loaded entities
   - Consider using `@EntityGraph` in repository queries for eager loading
   - Test DTOs to ensure they properly initialize required relationships

7. **User Isolation:**
   - Always test with multiple users to verify data isolation
   - Verify userId is correctly extracted from JWT token
   - Test that repository queries properly filter by userId
   - Test cross-user access attempts return 404/403

8. **Database Constraints:**
   - Test @NotNull constraint violations
   - Test foreign key constraint violations
   - Test unique constraint violations (if any exist)
   - Test data type violations (negative values, invalid ranges)

9. **N+1 Query Problems:**
   - Monitor SQL logs during test execution
   - Use JOIN FETCH or @EntityGraph to eagerly load required relationships
   - Verify queries don't cause excessive database round-trips

10. **Concurrent Operations:**
    - Test concurrent operations when applicable (battle turns, booster opening)
    - Use proper transaction isolation levels
    - Consider using `@Transactional(isolation = Isolation.SERIALIZABLE)` for race condition tests
