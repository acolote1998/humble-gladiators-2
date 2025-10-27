# TO DOs - Humble Gladiators 2

- [ ] figure out a way to be able to play more than one battle per day? at the moment once we play one, we always retrieve the same one in the front end, which is intended, that's fine, but if I wanna test more than one?

- [ ] Battle
  - [ ] In the turn model, we return dealed damage, healed amount but not mp recovered? we have to include that, since certain consumables
  can recover MP
  - [ ] subsequently, in the frontend battle finished we have simulateTurn(), after adding the MP to the turn action in the backedn we weill have to update this function too

 - [ ] implement in character inventory route to show battle stats? previous battles? for potential replays as well?
   
  - [ ] Implement some sort of "check intention" and log conditionally based on that. At the moment we are logging a lot of shit in console when an availability happens, and this happens ALL the time? -> check with chat gpt

  - [ ] check which logs are worth it, right now for when any character performs any action we print like 4 or 5 logs... maybe just print the final one? or don't print if they succeed? only the negative one that could give u feedback in a potential runtime excepption?

- [ ] Implement level up system?

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