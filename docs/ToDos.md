# TO DOs - Humble Gladiators 2

- [ ] Battle
  - [ ] Battle turn processing system
  - [ ] Able to cast spells
  - [ ] Able to use consumables
  - [ ] Able to physically attack
  
- [ ] Battle in the future:
  - [ ] Enable turn processing based on the character snapshot. At the moment we rely on live data of the characters to process a battle, 
  but that not might be ideal for the future?
  - IDEA: At the moment, we trigger the enemy's turn when we to a getBattle, but we currently have the character's executed turn as a response from either a spell,
  or a consumable or an attack. So I am thinking to "simulate" a waiting for the enemys turn, it could be that when we have success of an action of the hero, we can render the
  battle accordingly, with the result of the hero's turn, and probably add a onSuccess in the ation call, wait let's say 2, or 3 seconds? and then invalidate the getBattle,
  so we would get an updated battle that would trigger the enemy's turn (and render an updated battle with the enemy's turn already applied?)

- [ ] Make placeholders in inventory for the slots when the user does not have any certain type of item

- [ ] Modify the card components so that if they are rendered from the inventory they look smaller and they show normal size only on hover?

- [ ] Optimize campaign lobby booster availability checks by implementing caching with TTL to reduce expensive database queries (at the moment the check function does a lot of queries to the DB)

- [ ] Ensure character response DTOs obscure undiscovered data to prevent network inspection exploits (in the compendium for example)

- [ ] Improve battle consolidation algorithm in scheduled job:
  - Filter battles needing consolidation before processing
  - Only call consolidateBattle() on relevant battles to avoid unnecessary DB writes
  - Only save consolidated battles, no need to re-save battles that did not change

- [ ] Refactor BattleService by moving appropriate methods to BattleUtil

- [ ] Evaluate utility of creating BoosterUtil and similar helper classes for other services

- [ ] Investigate/Consider if it is worth it to create optimized character DTOs for battle objects to improve performance (instead of having the full CharacterInstanceDto everywhere)

- [ ] Do we have a bug? Check? Happened once (I think) that the enemy was supposed to play the first turn in battle, but it did not happen, but the player could not act either.

- [ ] Implement item drop system from defeated enemies

- [ ] Integrate height/weight attributes into game mechanics

- [ ] Add prompt_log env var flag to enable logging of refined prompts for quality assessment and improvement

- [ ] Refactor CSS architecture by breaking down index.css into modular stylesheets

- [ ] Implement difficulty system with stat modifiers for easy/normal/hard campaigns

- [ ] Add booster env var flag for tier 4+ and rarity 4+ content only for test purposes

- [ ] Make CharacterInstance have also a field called battleScenarioImage that generates when the character is opened ina booster. This provides unique battle scenarios for each character instance with custom background images

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
  - [ ] Item drop feature?
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