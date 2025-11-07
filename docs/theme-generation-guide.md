# Frontend Color Guide

This document describes the current color tokens defined in `frontend/src/index.css`, how they map onto UI elements, and guidance for generating new palettes. All tokens listed here belong to the existing light theme. Names that contain `dark` or `darker` (for example `--page-container-bg-darkest`, `--page-container-bg-darkerer`, `--dark-text`) refer to lighter/darker tints within this palette and are **not** related to a night theme.

## Product Context

Humble Gladiators 2 is a turn-based card game for tabletop-style adventures. Players:
- Create and theme campaigns with custom lore and booster packs.
- Forge heroes, equip gear, and manage inventories.
- Fight daily battles, earn rewards, and unlock items through card-based boosters.

The UI mixes “campaign management” surfaces (lists of quests, stats, forms) with dynamic combat visuals and collectible-card motifs. Themes should balance readability for dense data tables with flashes of heroic, fantastical energy suitable for gladiator battles and mystical boosters.

## Surface & Structural Tokens

| Variable | Default | Primary Usage |
| --- | --- | --- |
| `--site-bg` | `#b1d0da` | Browser `body` background. |
| `--page-container-bg` | `#e8f3f8` | Base card/background for `PageContainer`, campaign cards, general panels. |
| `--page-container-bg-darker` | `#cbe0eb` | Secondary surface: headings, NavBar background, stat headers. |
| `--page-container-bg-darkerer` | `#6c9cb0` | Dense surface: progress bars, highlighted headers (e.g. “Battle Finished”). |
| `--page-container-bg-darkest` | `#2b4c59` | Deep accent for hovers (NavBar hover background). |
| `--page-container-border` | `#98c2d1` | Card/container borders across campaign and battle UIs. |
| `--highlight-color` | `#fff89a` | Attention call-outs (e.g. “Close Battle” button, discovery ribbons). |
| `--highlight-color-border` | `#c7b600` | Border to pair with `--highlight-color`. |

### Text Tokens

| Variable | Default | Notes |
| --- | --- | --- |
| `--light-text` | `#ffffff` | Used on darker surfaces such as `--page-container-bg-darkerer`. |
| `--dark-text` | `#0a1316` | Primary copy color for light surfaces. Also reused via action tokens. |

## Interactive & Feedback Tokens

| Variable | Default | Components |
| --- | --- | --- |
| `--information-color` | `#9ed5ff` | Booster card actions, discovery progress bars. |
| `--information-color-border` | `#1e65a9` | Border for the above. |
| `--action-positive-bg` | `#7af2d1` | Positive CTA background (campaign creation button, hero creation hover, battle callouts). |
| `--action-positive-foreground` | `var(--dark-text)` | Text color when `--action-positive-bg` is used as a surface. |
| `--battle-victory-bg` | `var(--action-positive-bg)` | Victory badge in battle rewards. |
| `--battle-victory-foreground` | `var(--action-positive-foreground)` | Text color for victory badge. |
| `--battle-defeat-bg` | `#f59aa0` | Defeat badge in battle rewards; keeps compatibility with previous `--unavailable-color`. |
| `--battle-defeat-foreground` | `var(--light-text)` | Text color for defeat badge. |
| `--unavailable-color-border` | `#c73642` | Border for defeat/unavailable states (campaign still generating). |

> Legacy aliases: `--creation-color` now references `--action-positive-bg`, and `--unavailable-color` references `--battle-defeat-bg`. Existing code that still points to the legacy tokens will continue to function.

## Component Mapping

- **NavBar (`components/campaigns/NavBar.tsx`)**  
  Uses the `--page-container-bg-*` ladder for structure and `--action-positive-bg` for hover accents.

- **Campaign list & details (`CampaignInfo`, `routes/campaign/index.tsx`)**  
  Rely on `--page-container-bg`, `--page-container-bg-darker`, and `--highlight-color` to differentiate readiness states. Hover feedback for disabled cards retains `--battle-defeat-bg`.

- **Campaign creation flow (`CreateCampaign`, `RedirectCreateHeroButton`, `routes/campaign/$id/createHero.tsx`)**  
  Primary CTA surfaces now come from `--action-positive-bg`/`--action-positive-foreground`, allowing the hero creation journey to diverge from battle hues.

- **Battle UI (`BattleExecuting.tsx`, `BattleCheckAndCreation.tsx`, `RewardsTable.tsx`)**  
  - Tactical instructions and “Start Battle” prompts use the action token pair.  
  - Victory/defeat banners leverage the dedicated battle tokens, freeing future palettes to make combat feedback distinct from campaign creation.

- **Booster flows (`ItemsBooster.tsx`, `CharacterBooster.tsx`, discovery widgets)**  
  Continue to use the information tokens for clarity and progression cues.

## Palette Design Guidance

When generating a new set of values (manually or via an LLM):

1. **Establish Base Contrast**
   - `--dark-text` on `--page-container-bg` should maintain WCAG AA contrast (≥4.5:1).  
   - `--light-text` must contrast against `--page-container-bg-darkerer` and `--battle-defeat-bg`.

2. **Derive Surface Ladder**
   - Start with a neutral/light primary (`--page-container-bg`) then step towards richer tones for `--page-container-bg-darker`, `--page-container-bg-darkerer`, and `--page-container-bg-darkest`.  
   - Aim for consistent hue while increasing chroma/value so hover states read as intentional depth rather than separate colors.

3. **Action & Battle Tokens**
   - Pick `--action-positive-bg` as a saturated, optimistic hue distinct from the neutral surfaces. Confirm `--action-positive-foreground` contrasts ≥4.5:1.  
   - Derive `--battle-victory-*` from the action colors only if you want the narratives tied together; otherwise, assign unique values to signal combat success.  
   - `--battle-defeat-bg` should communicate caution but remain lighter than `--unavailable-color-border` to keep borders legible.

4. **Information Tokens**
   - `--information-color` acts as a progress/completion state; keep it within 20–30° hue distance from `--information-color-border` for cohesive gradients.  
   - Ensure `--information-color` is visually distinct from `--action-positive-bg` so boosters and CTAs do not blur together.

5. **Highlight Tokens**
   - `--highlight-color` should pop against both neutral surfaces and action colors. Adjust `--highlight-color-border` to be the same hue with lowered lightness for definition.

6. **Testing Checklist**
   - Preview NavBar hover, campaign creation hover, victory/defeat banners, and booster buttons.  
   - Validate that inactive states (`--battle-defeat-bg`) do not clash with `--action-positive-bg` when adjacent.  
   - Check that `--light-text`/`--dark-text` choices remain readable on every surface.

## Prompting an LLM for New Values

When asking an LLM to suggest a palette, include the constraints above and provide an anchor color (e.g., desired brand hue for `--action-positive-bg`). Sample prompt structure:

```
Generate hex colors for the Humble Gladiators 2 UI. 
- Base surface should be a cool, desaturated blue.
- Provide progressively richer tints for darker surfaces.
- Supply an energetic accent for --action-positive-bg with sufficient contrast for dark text.
- Give a distinct caution tone for --battle-defeat-bg that still supports white text.
- Keep information tones in a lighter sky-blue family.
Return the updated values using the “Expected Output Format” shown below (a plain code block listing each variable on its own line).
```

The model can then output replacements for the tokens listed in this document while respecting structural relationships.

### Expected Output Format

Always respond with a fenced code block (` ``` `) containing one line per variable in the following order. Values may be hex codes or `var(...)` references as appropriate. The snippet below shows **only the required ordering and syntax**—treat every value in it as a placeholder. When producing a new theme, you **must replace every placeholder value with colors that fit the requested aesthetic** (you may use `var(...)` references only when you intentionally link tokens).

Example format (placeholders to be replaced):
```
  --page-container-bg: #e8f3f8;
  --page-container-bg-darker: #cbe0eb;
  --page-container-bg-darkerer: #6c9cb0;
  --page-container-bg-darkest: #2b4c59;
  --page-container-border: #98c2d1;
  --light-text: #ffffff;
  --dark-text: #0a1316;
  --site-bg: #b1d0da;

  --action-positive-bg: #7af2d1;
  --action-positive-foreground: var(--dark-text);
  --battle-victory-bg: var(--action-positive-bg);
  --battle-victory-foreground: var(--action-positive-foreground);
  --battle-defeat-bg: #f59aa0;
  --battle-defeat-foreground: var(--light-text);
  --creation-color: var(--action-positive-bg);
  --unavailable-color: var(--battle-defeat-bg);
  --unavailable-color-border: #c73642;
  --highlight-color: #fff89a;
  --highlight-color-border: #c7b600;
  --information-color: #9ed5ff;
  --information-color-border: #1e65a9;
```

If the theme calls for reusing another token (e.g., `--battle-victory-bg: var(--action-positive-bg);`), declare it explicitly; otherwise provide a fresh hex value. Any response that simply echoes the placeholders above should be considered incorrect.

---

Keep this guide alongside `index.css` updates so future contributors (human or AI) can evolve the palette without breaking visual coherence.

