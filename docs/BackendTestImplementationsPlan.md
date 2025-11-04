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
- Configure in `src/test/resources/application-test.properties`:
  ```properties
  spring.datasource.url=jdbc:h2:mem:testdb
  spring.datasource.driverClassName=org.h2.Driver
  spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
  spring.jpa.hibernate.ddl-auto=create-drop
  spring.jpa.show-sql=false
  ```
- Ensure H2 dependency is in `pom.xml` test scope:
  ```xml
  <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>test</scope>
  </dependency>
  ```
- Use `@ActiveProfiles("test")` on test classes to activate test profile

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

**Controllers:**
- [ ] `BoosterControllerIntegrationTest`
  - Test `POST /api/campaign/{campaignId}/item-booster` with real services
  - Test `POST /api/campaign/{campaignId}/character-booster` with real services
  - Test `GET /api/campaign/{campaignId}/item-booster/available` with real database
  - Test `GET /api/campaign/{campaignId}/character-booster/available` with real database
  - Verify full HTTP request/response cycle
  - Test authentication/authorization with real security context

### Package: character

**Services:**
- [ ] `CharacterServiceIntegrationTest`
  - Test `createHero()` - verify hero is persisted with all relationships (Stats, Inventory)
  - Test `getHero()` - verify database query returns correct hero
  - Test `getDailyEnemy()` - verify database query and randomization
  - Test `createTenNPCsOfDesiredTier()` - verify multiple NPCs are created and persisted
  - Test character creation with requirements validation
  - Test character updates (level up, stat changes) persist correctly

- [ ] `InventoryServiceIntegrationTest`
  - Test `equipItem()` - verify item is marked as equipped and persisted
  - Test `unequipItem()` - verify item is marked as unequipped and persisted
  - Test inventory updates are reflected in database
  - Test inventory validation (equipment slots, requirements)

**Controllers:**
- [ ] `CharacterControllerIntegrationTest`
  - Test `POST /api/campaign/{campaignId}/character-instances/hero` - full flow with database
  - Test `GET /api/campaign/{campaignId}/character-instances/hero` - verify database retrieval
  - Test `GET /api/campaign/{campaignId}/character-instances` - verify list retrieval
  - Test error handling with real database constraints

- [ ] `InventoryControllerIntegrationTest`
  - Test `POST /api/campaign/{campaignId}/character-instances/{characterId}/equip/{itemId}` - full flow
  - Test `POST /api/campaign/{campaignId}/character-instances/{characterId}/unequip/{itemId}` - full flow
  - Verify equipment changes persist in database
  - Test validation with real database state

### Package: core

**Services:**
- [ ] `CampaignServiceIntegrationTest`
  - Test `createCampaign()` - verify campaign and theme are persisted
  - Test `getCampaignByIdAndUserId()` - verify database query
  - Test `getAllCampaignsByUserId()` - verify list retrieval
  - Test campaign state updates persist correctly
  - Test campaign deletion cascades correctly

- [ ] `BattleServiceIntegrationTest`
  - Test `createNewBattle()` - verify battle, teams, and turns are persisted
  - Test `performPhysicalAttack()` - verify turn is created and persisted
  - Test `getUpdatedBattle()` - verify battle state updates persist
  - Test `getRewardForBattle()` - verify reward creation and persistence
  - Test battle winner/loser determination with real database
  - Test battle cleanup with date-based queries

- [ ] `GameServiceIntegrationTest`
  - Test `startGame()` - verify full game creation flow
  - Test campaign creation with all related entities (items, characters, etc.)
  - Test state transitions during game creation
  - Mock GeminiService and RunwareService with `@MockBean` but use real repositories and H2 database

- [ ] `BattleCleanupServiceIntegrationTest`
  - Test `cleanupOldBattles()` - verify database deletion based on date
  - Test cleanup only affects old battles (not current ones)
  - Verify orphaned relationships are handled

- [ ] `RequirementServiceIntegrationTest`
  - Test requirement mapping from DTOs
  - Test requirement validation with real database entities

**Controllers:**
- [ ] `BattleControllerIntegrationTest`
  - Test `POST /api/campaign/{campaignId}/battle` - full battle creation flow
  - Test `POST /api/campaign/{campaignId}/battle/{battleId}/turn` - full turn execution
  - Test `GET /api/campaign/{campaignId}/battle/{battleId}` - verify battle retrieval
  - Test battle state transitions with real database

- [ ] `CampaignControllerIntegrationTest`
  - Test `POST /api/campaign` - full campaign creation with database
  - Test `GET /api/campaign` - verify campaign list retrieval
  - Test `GET /api/campaign/{campaignId}` - verify single campaign retrieval
  - Test campaign updates persist correctly

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
  - Test `getAllArmorTemplatesForACampaignAndUser()` - verify database query
  - Test `getRandomArmorTemplateForItemBooster()` - verify randomization and query
  - Test `instanceFromArmorTemplate()` - verify instance creation and persistence
  - Test `saveArmor()` - verify template persistence

- [ ] `BootsServiceIntegrationTest`
  - Test `getAllBootsTemplatesForACampaignAndUser()` - verify database query
  - Test `getRandomBootsTemplateForItemBooster()` - verify randomization and query
  - Test `instanceFromBootsTemplate()` - verify instance creation and persistence
  - Test `saveBoots()` - verify template persistence

- [ ] `ConsumableServiceIntegrationTest`
  - Test `getAllConsumableTemplatesForACampaignAndUser()` - verify database query
  - Test `getRandomConsumableTemplateForItemBooster()` - verify randomization and query
  - Test `instanceFromConsumableTemplate()` - verify instance creation and persistence
  - Test `saveConsumable()` - verify template persistence

- [ ] `HelmetServiceIntegrationTest`
  - Test `getAllHelmetTemplatesForACampaignAndUser()` - verify database query
  - Test `getRandomHelmetTemplateForItemBooster()` - verify randomization and query
  - Test `instanceFromHelmetTemplate()` - verify instance creation and persistence
  - Test `saveHelmet()` - verify template persistence

- [ ] `ShieldServiceIntegrationTest`
  - Test `getAllShieldTemplatesForACampaignAndUser()` - verify database query
  - Test `getRandomShieldTemplateForItemBooster()` - verify randomization and query
  - Test `instanceFromShieldTemplate()` - verify instance creation and persistence
  - Test `saveShield()` - verify template persistence

- [ ] `SpellServiceIntegrationTest`
  - Test `getAllSpellTemplatesForACampaignAndUser()` - verify database query
  - Test `getRandomSpellTemplateForItemBooster()` - verify randomization and query
  - Test `instanceFromSpellTemplate()` - verify instance creation and persistence
  - Test `saveSpell()` - verify template persistence

- [ ] `WeaponServiceIntegrationTest`
  - Test `getAllWeaponTemplatesForACampaignAndUser()` - verify database query
  - Test `getRandomWeaponTemplateForItemBooster()` - verify randomization and query
  - Test `instanceFromWeaponTemplate()` - verify instance creation and persistence
  - Test `saveWeapon()` - verify template persistence

**Controllers:**
- [ ] `ArmorTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/armor-templates` - verify full flow with database
  - Test authentication/authorization with real security

- [ ] `BootsTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/boots-templates` - verify full flow with database
  - Test authentication/authorization with real security

- [ ] `ConsumableTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/consumable-templates` - verify full flow with database
  - Test authentication/authorization with real security

- [ ] `HelmetTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/helmet-templates` - verify full flow with database
  - Test authentication/authorization with real security

- [ ] `ShieldTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/shield-templates` - verify full flow with database
  - Test authentication/authorization with real security

- [ ] `SpellTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/spell-templates` - verify full flow with database
  - Test authentication/authorization with real security

- [ ] `WeaponTemplateControllerIntegrationTest`
  - Test `GET /api/campaign/{campaignId}/weapon-templates` - verify full flow with database
  - Test authentication/authorization with real security

### Best Practices

1. **Database Setup:**
   - Use `@Sql` annotations for complex test data setup
   - Use `@Transactional` for automatic rollback (unless testing transactions)
   - Use `@DirtiesContext` sparingly (only when needed)

2. **Test Data:**
   - Create helper methods for common test entities (Campaign, Character, etc.)
   - Use builders or factories for complex object creation
   - Ensure test data is isolated and doesn't interfere between tests

3. **Assertions:**
   - Verify database state after operations (not just return values)
   - Check relationships are persisted correctly (OneToMany, ManyToMany)
   - Verify cascading operations work correctly

4. **Performance:**
   - Keep integration tests focused on integration points
   - Avoid testing things already covered in unit tests
   - Use `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)` for faster tests when possible

5. **External Dependencies:**
   - Always mock GeminiService and RunwareService
   - Consider using WireMock or MockServer for HTTP mocking if needed
   - Test error scenarios (API failures, timeouts)

6. **Security:**
   - Test with real Spring Security context
   - Verify user isolation (users can only access their own data)
   - Test authorization rules with different user roles if applicable

### Common Issues and Solutions

1. **Database Connection:**
   - Ensure H2 is in classpath: `com.h2database:h2` (test scope)
   - Create `src/test/resources/application-test.properties` with H2 configuration
   - Use `@ActiveProfiles("test")` on all integration test classes
   - H2 database will be created fresh for each test run (in-memory)

2. **Transaction Rollback:**
   - Use `@Transactional` on test methods for automatic cleanup
   - If testing transaction behavior, use `@Rollback(false)` and manual cleanup

3. **Entity Relationships:**
   - Ensure all required entities are persisted before testing relationships
   - Use `EntityManager.flush()` to ensure database state is current

4. **Date/Time Testing:**
   - Use fixed dates or `@MockBean` Clock for date-dependent tests
   - Test timezone handling if applicable

5. **Random Data:**
   - Seed random number generators for reproducible tests
   - Or use `@MockBean` for random-dependent services
