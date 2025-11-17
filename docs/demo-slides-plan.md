# Humble Gladiators 2 – Demo Slide Plan

## Audience & Goals
- Interview with Erik (manager) and Fredrik (frontend dev); highlight frontend decision-making, UX polish, and ability to own a complex feature set.
- Show practical progress rather than perfection: emphasize working slices, structured roadmap, and collaborative touchpoints.
- Use slides as prompts for a guided walkthrough; jump into the live app whenever showing interactions adds clarity.

## Gameplay Loop Snapshot
- Player creates an AI-assisted campaign with theme constraints → backend spins up bespoke NPCs, gear templates, encounters.
- Forge a hero, then discover content through boosters, collection management, and equipment loadouts.
- Engage in turn-based battles, review stats (win rate, discovery progress), iterate with new boosters/dailies.

## Frontend / Tooling Highlights
- React + TypeScript + Vite app bootstrapped for fast feedback and typed safety.
- Routing with `@tanstack/react-router` for file-based routes and data-aware navigation.
- Data fetching/mutations with `@tanstack/react-query` hooks (`useCampaigns`, `useCharacters`, etc.) layered with Clerk-authenticated API calls.
- Authentication via Clerk components (signed-in/out states, token retrieval) controlling gated flows.
- Styling: Tailwind-style utility classes combined with CSS custom properties for theming (`PageContainer`, responsive grids, animated CTAs).
- Test readiness: Playwright e2e harness (`frontend/tests/e2e`) to cover campaign flows once critical paths stabilize.

## Slide Outline & Notes

### 1. Title & Role Fit
- **Purpose:** Establish who you are, what HG2 is, and why it’s a relevant frontend project.
- **Talking points:** SALT context, scope of your contributions (frontend lead), interview expectations.
- **Visuals:** Logo from `frontend/public/humble_gladiators_logo.png`.

### 2. Vision & Player Fantasy
- **Purpose:** Anchor the audience in the experience you’re building.
- **Talking points:** “MTG meets roguelite campaigns”, AI-driven variability, focus on replayability.
- **Visuals:** Screenshot of campaign cover art (base64 from `campaign.coverImgBase64` renders).

### 3. Gameplay Loop Overview
- **Purpose:** Show the end-to-end loop you’ll demo.
- **Talking points:** Campaign creation → hero forge → boosters → deck/equipment → battles → stats/dailies.
- **Visuals:** Simple loop diagram; annotate which steps are already implemented vs WIP.
- **Demo cue:** Quick teaser GIF or mention “will demo steps 1–4 live”.

### 4. Frontend Architecture
- **Purpose:** Prove intentional technical choices.
- **Talking points:** Vite + TS, TanStack Router for nested routes (`/campaign`, `/campaign/$id/*`), React Query hooks (show `useGetAllCampaignsForAUser` snippet), Clerk integration for auth gating.
- **Visuals:** Folder structure screenshot or diagram; snippet of `useGetCampaignByIdForAUser`.

### 5. Campaign Dashboard UX
- **Purpose:** Highlight the `CampaignsRoute` list experience.
- **Talking points:** Signed-in guard, CTA for `/campaign/create`, loading states with `<Loader />`, responsive grid layout, `CampaignInfo` cards.
- **Visuals:** Screenshot of “Your Campaigns” grid.
- **Demo cue:** Start live demo here; show creating a campaign.

### 6. Campaign Creation Flow
- **Purpose:** Showcase form handling and async feedback.
- **Talking points:** Controlled inputs, theme parsing, disabled states, progress polling via `useGetCreationCampaignState` (1.5s refetch), optimistic UX copy.
- **Visuals:** Form screenshot plus `CreationProgressBar`.
- **Demo cue:** Actually submit a campaign; point out the live poller.

### 7. Hero Onboarding & Navigation
- **Purpose:** Emphasize conditional routing and stateful UX (`RedirectCreateHeroButton` → `/campaign/$id/createHero`).
- **Talking points:** Clerk-auth guard, idempotent hero creation (404 detection), navigation via TanStack router, reuse of `PageContainer`.
- **Visuals:** Hero creation modal screenshot.
- **Demo cue:** Create hero live; show success auto-redirect.

### 8. Collection, Boosters, and Stats
- **Purpose:** Underscore component depth and data visualization.
- **Talking points:** Booster openings (`components/boosters/*`), card templates (`public/templates/*`), `CampaignStats` hooking into multiple categories and win-rate computations.
- **Visuals:** Grid of `DiscoveredItemInfo` tiles; card art.
- **Demo cue:** Open a booster or scroll through stats; discuss state management.

### 9. Battle Preview & Future UX
- **Purpose:** Briefly cover current battle UI state and upcoming polish.
- **Talking points:** `routes/campaign/$id/battle.tsx` WIP, planned animations, turn log, multi-device considerations.
- **Visuals:** Wireframe or placeholder view; mention test hooks.
- **Demo cue:** Optional short look if stable; otherwise static mock.

### 10. Roadmap & Collaboration Hooks
- **Purpose:** Show you prioritize and iterate.
- **Talking points:** Near-term (responsive tweaks, battle polish, global state refactor), mid-term (multiplayer lobby, merchant UI), collaboration points (design handoff, backend contracts).
- **Visuals:** Simple table (Now / Next / Later).

### 11. Q&A / Discussion
- **Purpose:** Invite feedback and technical deep-dives.
- **Talking points:** Prepared prompts: “Interested in TanStack Router adoption?”, “Curious about test strategy?”, “Need to see backend integration details?”
- **Visuals:** Minimal—thank you slide with contact info.

## Live Demo Script (reference)
- Start signed-out to show Clerk CTA → sign in (or use stored session) demonstrating conditional nav.
- Navigate to `/campaign` → create a campaign → highlight progress bar updates.
- Jump into newly created campaign detail view: cover art + stats gating, show hero creation redirect if hero absent.
- After hero creation, tour the campaign dashboard sections (boosters, inventory, stats, battle entry).
- Close with roadmap slide and open Q&A.

## Prep Checklist
- Refresh seeded data so booster openings have interesting pulls.
- Capture fresh screenshots/GIFs for slides 2, 5, 6, 7, 8.
- Rehearse demo path twice to confirm Clerk session + API readiness.

