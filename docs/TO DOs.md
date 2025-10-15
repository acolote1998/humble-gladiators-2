# TO DOs - Humble Gladiators 2

- [ ] Make each character instance have an attribute imgbyte[] (whatever it is) battleScenario / battleGround. And when we get the character from the booster, we also generate its battle scenario, so each scenario and battle can feel unique.

- [ ] impement hero inventory, equippment / removing equippment function

- [ ] Implement back of cards generation on campaign creation

- [ ] Improve rendering of campaign list in frontend -> make the campaign itm list more visually attractive.

  - [ ] Remove unnecessary information from there

- [ ] Improve rendering of campaign component itself

- [ ] Improve AI accuracy for content generation :
  - [ ] Sometimes we are generating a weapon and it generates something that would fit more like an armor, and things like that. Needs to stick to the category better
  - [ ] Make sure the AI really does not promise in-game effects in names or descriptions
- [ ] Language content module

  - [ ] Make campaign have a language (an enum of available languages)
  - [ ] Make all items and characters have a name and description per language (example EN_NAME, EN_DESCRIPTION)
    - [ ] On booster opening, check the language of the campaign, and then try to get that name and description
          in that language. if the result is null, we need to send the name and description in english to
          Gemini, and generate the name and description in the desired language. Then add the translated
          name and description to the ItemTemplate or CharacterInstance, which will then be saved together with the
          rest. This way, we always
          translate on demand on booster opening, instead of generating ALL languages when all the content is created,
          this way we favour efficency and performance of the app)
  - [ ] When sending items and so to the frontend, the server would have to check in which language the campaign is
        setted, and get those values for the name and description fields. Possibly, we could check if all the fields exist
        in the language maybe? to avoid sending nulls? and if not, either send them in english or generate the missing
        through gemini

- [ ] Daily NPC Card Pack

  - [x] Users can open one npc card booster per day
  - [x] Each booster contains one random NPC
  - [x] When opening the pack, the obtained card are now DISCOVERED = true
  - [] Make an endpoint something like "getEnemyForToday" -> Find the best way to do it? How do we make the enemy available for only that day, and also possible to be retrieved some other days in the future? ( we can use similar logic to canTheUserOpenACharacterPack to check if there is an enemy updated today)
  - [x] Frontend implementation for pack enemy opening (MVI)
    - [ ] Handle error if pack was already opened for the day
    - [x] implementation of opening npc booster
  - [x] The second any booster gets opened, those card entities will be marked as "discovered"
  - [x] Each card in any given booster has a chance to belong to a certain tier
    - [x] Example: Tier 1 – 42%, Tier 2 – 25%, Tier 3 – 8%, Tier 4 – 5%, Tier 5 – 3%
  - [x] Each card also has a rarity percentage within its tier
    - [x] Example: Rarity 1 – 30%, Rarity 2 – 25%, Rarity 3 – 20%, Rarity 4 – 15%, Rarity 5 – 10%

- [ ] Daily Enemy Encounter
  - [ ] Users can face one enemy per day
  - [ ] The user opens an enemy booster and the enemy card is drawn from there ahd the battle starts straight
        away.
- [ ] Daily Merchant Encounter

  - [ ] Users can meet one merchant per day
  - [ ] The use opens a merchant booster and the merchant card is drawn from there. The user can keep the merchant
        for as long as they want, the merchant will stay there until al three item cards get bought, or until the
        user opens a new merchant booster
    - [ ] Each merchant offers three random items for sale

- [ ] Discovery system integration
- [ ] Logic for interface method implementation
- [ ] Balance formulas
  - [ ] gold reward
  - [ ] exp reward
  - [ ] const
  - [ ] cons
  - [ ] int
  - [ ] dex
  - [ ] str
  - [ ] spd
  - [ ] luck
  - [ ] hp
  - [ ] mp
  - [ ] height
  - [ ] weight
  - [ ] level
  - [ ] currentExp
  - [ ] expForNextLevel
- [ ] Inventory management service layer
- [ ] Equipment system service layer
- [ ] Combat Logic Service Layer
  - [ ] Turn-based combat flow implementation
  - [ ] Damage calculation logic
  - [ ] Battle state management
- [ ] Replay system / logic
- [ ] Achievement system
- [ ] Buying / Selling System
- [ ] Performance Optimization
- [ ] Testing & Documentation
- [ ] Deployment & Monitoring
