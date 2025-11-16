# TO DOs - Humble Gladiators 2

- [ ] When an action has chosen in battle, highlight the possible targets with maybe some sort of aura or so?

- [ ] Make it possible to deselect an action by clicking the card again maybe?

- [ ] Make cards clickable in none desktop resolution so they zoom up and show stats and so.

- [ ] Add validation length in frontend for when creating a campaign and a hero (names, descriptions)

- [ ] Add validation length in backend for when receiving invalid name, descriptions

- [ ] After receiving maybe 5 invalid requests? give the user a 5 minutes timeout
  - [ ] If invalid after again, ban user

- [ ] Add prompt curation -> if receiving invalid prompt (like rude, sexual, etc) in campaign, give a 5 minutes timeout to that user
  - [ ] If received again, ban user
  - [ ] before curating the prompt with an LLM, try to have a fixed array of things we should not pass through
        and make a static validation. only after that we can validate further through an LLM

- [ ] Add AntiBot model -> make 1 campaign have 1 antibot model

- [ ] Anti bot model will have userId as fields (FKs)

- [ ] Add AntiBot Service in backend to handle these validations

- [ ] in antibot service, when banning -> turn something like banned=true or whatever and assign the user's id  to the anti bot model

- [ ] AniBot
        id
        userId
        DateTime lastInvalidRequestTime
        amountOfInvalidRequests (if last invalid request was more than 5 minutes ago, reset to zero, otherwise increase)
        boolean banned
        DateTime bannedUntil 


- [ ] in the inventory -> currently using emojis and characters for experience and for gold -> replace with svgs

- [ ] rework formulas for item stats (example createTwentyFiveNewArmorTemplates in ArmorService)

- [ ] improve the 'NEW!" message when we discover a new card in a booster

- [ ] rework stat formulas when it comes to the influence that tiers and rarities have, together with their multipliers

- [ ] make luck have some effect in the game?

- [ ] create index page localhost.com/ that showcases what humble gladiators is -> we could render some card components with high rarity and so and showcase different contents of different campaigns and so

- [ ] figure out a way to be able to play more than one battle per day? at the moment once we play one, we always retrieve the same one in the front end, which is intended, that's fine, but if I wanna test more than one?
   
  - [ ] Implement some sort of "check intention" and log conditionally based on that. At the moment we are logging a lot of shit in console when an availability happens, and this happens ALL the time? -> check with chat gpt

  - [ ] make the turn action from backend respond with which card name was used so we can display in frontend, otherwise right now we do like 'used a "spell"' instead of 'used "fireball"' which sounds better

  - [ ] check which logs are worth it, right now for when any character performs any action we print like 4 or 5 logs... maybe just print the final one? or don't print if they succeed? only the negative one that could give u feedback in a potential runtime excepption?

- [ ] Implement level up system?

- [ ] Optimize campaign lobby booster availability checks by implementing caching with TTL to reduce expensive database queries (at the moment the check function does a lot of queries to the DB)

- [ ] Ensure character response DTOs obscure undiscovered data to prevent network inspection exploits (in the compendium for example)

- [ ] Improve battle consolidation algorithm in scheduled job:
  - Filter battles needing consolidation before processing
  - Only call consolidateBattle() on relevant battles to avoid unnecessary DB writes
  - Only save consolidated battles, no need to re-save battles that did not change

- [ ] Refactor BattleService by moving appropriate methods to BattleUtil

- [ ] Evaluate utility of creating BoosterUtil and similar helper classes for other services

- [ ] Investigate/Consider if it is worth it to create optimized character DTOs for battle objects to improve performance (instead of having the full CharacterInstanceDto everywhere)

- [ ] Integrate height/weight attributes into game mechanics

- [ ] Add prompt_log env var flag to enable logging of refined prompts for quality assessment and improvement

- [ ] Refactor CSS architecture by breaking down index.css into modular stylesheets

- [ ] Implement difficulty system with stat modifiers for easy/normal/hard campaigns

- [ ] Add booster env var flag for tier 4+ and rarity 4+ content only for test purposes

- [ ] Improve campaign creation UI by replacing comma-separated theme inputs with dedicated controls

- [ ] Create endpoint for generating random campaign ideas:
  - [ ] Auto-populate campaign name and themes via frontend integration
  - [ ] Backend to call Gemini API for structured JSON response: {name:string, wantedThemes:string[], unwantedThemes:string[]}

- [ ] Develop tutorial system to guide players through game mechanics and optimization strategies

- [ ] Enhance AI content generation accuracy:
  - [ ] Improve category adherence for generated items/characters. Needs to stick to the category better (avoid an armor being "Helmet of X" for example)
  - [ ] Ensure names and descriptions avoid promising in-game effects

- [ ] Implement multilingual content system:
  - [ ] Add language selection to campaign on creation
  - [ ] Make all items and characters have a name and description per language (example EN_NAME, EN_DESCRIPTION)
  - [ ] On booster opening, check the language of the campaign, and then try to get that name and description in that language. if the result is null, we need to send the name and description in english together with campaign data to Gemini, and generate the name and description in the desired language. Then add the translated name and description to the ItemTemplate or CharacterInstance, which will then be saved together with the rest. This way, we always translate on demand on booster opening, instead of generating ALL languages when all the content is created, this way we favour efficency and performance of the app
  - [ ] When sending items and so to the frontend, the server would have to check in which language the campaign is setted, and get those values for the name and description fields. Possibly, we could check if all the fields exist in the language maybe? to avoid sending nulls? and if not, either send them in english or generate the missing through gemini

- [ ] Daily Merchant Encounter system:
  - [ ] Users can meet one merchant per day
  - [ ] The user opens a merchant booster (basically an item booster but for the merchant) and then three merchant cards are drawn from there.
  - [ ] The user can buy the merchant cards
  - [ ] The user can sell cards to the merchant
  - [ ] Create scheduled job to invalidate merchants from previous days.. We cannot keep merchants for days, every day the merchant from the day before must disappear

- [ ] Game balance tuning:
  - [ ] Gold reward formulas
  - [ ] Experience reward formulas
  - [ ] Attribute scaling (CON, INT, DEX, STR, SPD, LUCK)
  - [ ] HP/MP progression
  - [ ] Height/weight influence in attacks
  - [ ] Level progression and experience requirements

- [ ] Battle replay system

- [ ] Achievement system implementation

- [ ] Buying/selling economy system

- [ ] Performance optimization

- [ ] Comprehensive testing and documentation

- [ ] Deployment and monitoring infrastructure

## Critical Design Issues Identified from Test Implementation

- [ ] **@CreatedDate/@UpdateTimestamp Testability Limitation**: Entities with `@CreatedDate` and `@UpdateTimestamp` cannot have timestamps controlled for testing scenarios. Tests must use native SQL queries to set `created_at` timestamps (see `BattleCleanupServiceIntegrationTest.createBattleForDateAndCampaign()`). This prevents clean testing of time-based logic (cleanup services, date-based queries, etc.). Consider using `@PrePersist`/`@PreUpdate` callbacks that can be overridden in tests, or provide a `Clock` bean for time control.

- [ ] **Battle Creation Restrictiveness**: `BattleService.isBattleAvailableForToday()` prevents multiple battles per campaign per day (even finished ones). The logic checks `getAnyBattleForTodayByCampaignAndUserId()` which finds ANY battle (ongoing or finished) for today. This is too restrictive for testing scenarios and forces tests to create separate campaigns for each battle. Consider if the rule should only check for ongoing battles, not all battles. Or provide a test-friendly way to bypass this check in test environments.

- [ ] **Tight Coupling Between Content Generation and Validation**: Tests require extensive, detailed mocking of `GeminiService` with very specific validation requirements (exact gold/exp formulas, category enums, flag values). Mock data must match internal validation logic exactly or services retry infinitely. Changes to validation require updating multiple test mocks across `GameServiceIntegrationTest`, `GameControllerIntegrationTest`, and `CharacterServiceIntegrationTest`. Consider extracting validation into a separate, testable component. Or provide a test mode that bypasses validation for integration tests.

- [ ] **BattleCleanupService Consolidation Logic Edge Cases**: The `consolidateBattle()` method calls `fullyRecoverBothTeams()` which expects winning/losing teams to have at least one character. If teams are empty, it throws `NoSuchElementException`. The consolidation logic tries to populate winning/losing teams from teamOne/teamTwo, but fails if those are also empty. This could cause scheduled job failures in production. Add null/empty checks in `consolidateBattle()` before calling `fullyRecoverBothTeams()`, or make `fullyRecoverBothTeams()` handle empty teams gracefully.