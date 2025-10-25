```
## Before Week 42

**Project Setup & Infrastructure:**
- Spring Boot backend with PostgreSQL database setup
- React frontend with TypeScript, TanStack Router, and TailwindCSS
- Docker containerization for database
- Clerk authentication integration with OAuth2/JWT security
- Full CORS configuration for frontend-backend communication

**Core Game Architecture:**
- Complete data model with entities for campaigns, battles, turns, themes, and requirements
- Character system with abstract character classes, instances, and snapshots
- Comprehensive inventory system with item templates and instances
- Full item system covering 7 categories: weapons, armor, helmets, boots, shields, spells, consumables
- Booster pack system for item generation

**AI Integration:**
- Gemini AI service for content generation (characters, items, descriptions)
- Runware service for AI image generation
- PromptAider utility class for managing AI prompts
- Campaign cover image generation functionality

**Backend API:**
- RESTful controllers for all game entities (characters, items, campaigns, boosters)
- Service layer with business logic for all major features
- Repository pattern with JPA for data persistence
- Authentication-aware endpoints with user context

**Frontend Features:**
- Campaign management (create, list, view campaigns)
- Character creation and management interface
- Item template cards for all equipment types
- Campaign creation progress tracking
- Responsive UI components with TailwindCSS
- React Query for efficient data fetching and caching

**Authentication & Security:**
- Clerk-based user authentication
- JWT token validation on backend
- User-scoped data access and security
- Protected routes and components

Week 42 (13/10 to 17/10)

Monday 13/10

- Had the interview
- Had the passion project introduction presentation
- Integrated the campaign cover image to the project
- Improved architecture of the runware + gemini services (moved certain prompts / functions to a new util class called Prompt Aider)

Tuesday 14/10
- Improved frontend flow after creating a campaign -> on successfull creation, we navigate to the campaign page
- Improved order in the compendium -> Now cards that have an image will appear first
- Improved the organisation in the compendium -> Now all categories are contained withing a <detail> component
- Since now the campaign can have an img cover, I updated the default campaign in data.sql to contain one
- Implemented rarity / tier chance caculation when opening a booster
- Implemented a flag to activate/deactivate the real tier/rarity randomness
- Implemented character booster in the backend
- Implemented character booster in the frontend

Wednesday 15/10
- Integrated item booster opening MVI
- Integrated character booster opening MVI
- Improving validation & logging for invalid booster opening attempts
- Implemented validation for booster opening and hero existence in frontend
- Improved the loading phase of booster opening a bit
- Created validation layer during objects generation process to make sure all generated content is valid and will never crash the campaign on runtime
- Improved seeded data in example campaign to be more coherent
- Simple styling improvement to hero creation screen
- Simple styling improvement to session info component
- Flagging when a card is NEW when opening an item or character booster
- Making cards glow according to their tier / rarity

Thursday 16/10
- Integrated validation of requirements to avoid runtime crashes
- Adding zoom to discovered cards on hover
- Adding holo to discovered cards on hover
- Adding getDailyEnemy endpoint + logic
- Card back image generation
- Card back component
- Adding card flipping when opening booster
- Fixing visual bug related to the card back -> had to redesign component & rendring flow due to a race condition
- Improving className of all card components in frontend

Week 43

Saturday 18/10
- Added defense equipment system for hero items
- Added defense equipment system for NPCs
- Added weapon equippable for hero
- Added weapon equippable for npcs

Sunday 19/10
- Added combat logic methods in CharacterInstance (attack, heal, use spells)

Monday 20/10
- Logic to create a new battle between two characters
- Created http folder in backend to be able to test things better
- Refined logic to create a new battle
- Players can physically attack each other inside a battle

Tuesday 21/10
- Refined a lot the validation logic to be able to have a robust before turn-processing validation
- Adding logic to check if consumables are valid before using them
- Adding logic to check if spells are valid before using them
- Adding logic to use consumables in the battle
- Adding logic tu use spells in the battle
- Adding NPC auto-play turn logic
- Winning battle logic

Wednesday 22/10
- Added endpoint to check if there are ongoing battles for today
- Not possible to start a new battle when there is one ongoing
- Not possible to open an item booster when there is a battle ongoing
- Make sure we cannot open character boosters when there is a battle ongoing
- not possible to equip items when there is a battle ongoing
- verifying all these validations were implemented properly
- adding validation to avoid processing turns with empty teams
- validation to avoid allowing attacks, spells and consumables when the battle is not valid
- scheduled job to automatically consolidate old battles that might have been peending from previous days

Thursday 23/10
- Improving physical damage and magical damage display in character cards
- Changing order of certain stats in character cards
- Reworking conditional rendering of stats in character card to have different views depending on if we render it from compendium, from a booster or from a battle
- Refactoring ToDos.md into more structured way
- Created inventory route
- Rendered hero stats in inventory route
- Rendered hero inventory in inventory route
- Rendered equipped items in inventory route
- Implemented function to equip items from the inventory route
- Fixed visual bug of card auras showing where they should not
- Fixed visual bug showing auras in non discovered cards in compendium caused by bad logic in ternaries in all card components
- Fixed visual bug when mapping collections in react due to children sometimes sharing the same key -> it was resulting in duplicates. Solved it by adding the ID to the component in the key
- Making inventory and compendium item lists in a grid of 5 items instead of 3

Friday 24/10
- Unequip item endpoints
- Unequip MVI
- Battle route creation
- Fixed visual bugs respegint glowing, auras and css classes of the cards

Saturday 25/10
- Creation of battle possible
- Rendering battle content
- Battle creation + battle update flow during first turns is there
```
