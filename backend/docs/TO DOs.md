# TO DOs - Humble Gladiators 2

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
-  [X] Offload Gemini by generating more things on the server and less through ai
    - [X] Stats
    - [X] Gold reward
    - [X] Exp Reward
    - [X] Discovered
    - [X] refactor mapStatsFromCharacterFromGeminiDto
- [X] Hero Creation backend
    - [ ] Make a hero creating button for the frontend
    - [X] Add validation in backend to never allow more than one hero created by campaign
    - [X] Log Hero data in frontend
    - [X] Make a hero fetcher for displaying the hero in the frontend
- [ ] Card System
    - [ ] Daily Card Pack
        - [X] Users can open one item card booster per day
        - [X] Each booster contains three random items (Armor, Boots, Consumables, Helmets, Shields, Spells, Weapons)
        - [X] When opening the pack, the obtained cards are now DISCOVERED = true
        - [X] When opening the pack, we convert the ItemTemplate to ItemInstance
        - [X] When opening the pack, we add the ItemInstance to the hero's inventory
        - [ ] Frontend implementation for pack opening
            - [ ] Handle error if pack was already opened for the day
            - [X] implementation of opening item booster
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
    - [ ] Card Tiers and Rarity
        - [ ] Each card in any given booster has a chance to belong to a certain tier
            - [ ] Example: Tier 1 – 42%, Tier 2 – 25%, Tier 3 – 8%, Tier 4 – 5%, Tier 5 – 3%
        - [ ] Each card also has a rarity percentage within its tier
            - [ ] Example: Tier 1 – 30%, Tier 2 – 25%, Tier 3 – 20%, Tier 4 – 15%, Tier 5 – 10%
    - The second any booster gets opened, those card entities will be marked as "discovered"

- [ ] Discovery system integration
- [ ] ItemTemplate to ItemInstance methods
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
- [ ] Hero Creation
- [ ] Discovery Logic
    - [ ] Item discovery tracking
    - [ ] Discovery API endpoints
    - [ ] Achievement system
- [ ] Buying / Selling System
- [ ] Performance Optimization
- [ ] Testing & Documentation
- [ ] Deployment & Monitoring