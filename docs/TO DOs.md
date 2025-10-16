# TO DOs - Humble Gladiators 2

- [ ] fix bug -> cards have "auras" during booster opening, the should not have the css class?
- [ ] Write prompt_log flag to be able to LOG the "refined" prompts (and maybe all prompts?) to be able to assess prompt quality and potential improvments
- [ ] Organice css better. break down index.css into smaller css classes
- [ ] add difficulty? maybe an easy campaign will calculate the hero's modifiers and items with a plus in its formulas, and
      a normal campaign no modification, and a hard campaign will increase the enemies stats + decrease the potential obtainable stats for the hero?
- [ ] add a flag that makes all boosters give tier 4+ and rarity 4+ content only?
- [ ] Make each character instance have an attribute imgbyte[] (whatever it is) battleScenario / battleGround. And when we get the character from the booster, we also generate its battle scenario, so each scenario and battle can feel unique.
- [ ] improve UI for campaign creation to avoid having comma separated wantedThemes and unwantedThemes

- [ ] create an endpoint to generate interesting campaign name, and themes and call it from the frontend with a button [Get Random Campaign Idea]

  - [ ] it should auto fill up the campaign name and the campaign themes UI intstantly.
  - [ ] the backend can request from gemini a JSON of {name:string, wantedThemes:string[], unwantedThemes:string[]}

- [ ] create a "tutorial"? of how things work? and how to get better results?

- [ ] impement hero inventory, equippment / removing equippment function

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
