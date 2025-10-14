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
- Improved frontend flow after creating a campaign -> on successfull creation, we navigate to the campaign page
```
