import type { ConsumableType } from "../../types/consumablesTypes";
import { calculateTierAndRarityStars } from "../../util/calculateTierAndRarityStars";
import {
  cardCategoryImageClass,
  cardDescriptionClass,
  cardImageClass,
  cardNameClass,
  cardSizeClass,
  categoryAndNameClass,
  nameContainerClass,
  rarityClass,
  regularCardBottomStatsClass,
  regularCardBottomStatsFour,
  regularCardBottomStatsTwo,
  tierAndRarityClass,
  tierAndRarityContainer,
  tierClass,
  topStatsClass,
} from "./util/CardSizes";
export const ConsumableCard = ({
  category,
  description,
  discovered,
  name,
  rarity,
  tier,
  restoreHp,
  restoreMp,
  imgBase64,
  renderingFrom,
}: ConsumableType) => {
  //Toggle to see all information of the card
  // discovered = true;

  return (
    <div
      data-testid="consumable-card"
      className={[
        // Conditional classes
        renderingFrom == "BOOSTER" ? "rotate-y-180" : "",
        discovered && renderingFrom != "BOOSTER" ? "hover-zoom" : "",
        (discovered || renderingFrom == "BOOSTER") && tier === 5 && rarity === 5
          ? "consumable-tier-5-rarity-5"
          : discovered || renderingFrom == "BOOSTER"
            ? `consumable-tier-${tier}`
            : "",
        // Always-applied classes
        `${discovered && `rarity-${rarity}`} ${cardSizeClass}`,
      ]
        .filter(Boolean) // removes empty strings
        .join(" ")} // join with spaces
      style={{
        backgroundImage: `url('/templates/consumableCardTemplate.png')`,
      }}
    >
      {/* Top stats */}
      <div className={`${topStatsClass}`}></div>

      {/* Tier & rarity */}
      <div className={`${tierAndRarityClass}`}>
        <div className={`${tierAndRarityContainer}`}>
          {/* container for absolute children; give it a height so top:0 has meaning */}
          <span title="TIER" className={`${tierClass}`}>
            T {calculateTierAndRarityStars(tier)}
          </span>
          <span title="RARITY" className={`${rarityClass}`}>
            R {calculateTierAndRarityStars(rarity)}
          </span>
        </div>
      </div>

      {/* Category & name */}
      <div className={`${categoryAndNameClass}`}>
        {(discovered || renderingFrom == "BOOSTER") && imgBase64 ? (
          <img
            draggable={false}
            src={`data:image/jpeg;base64,${imgBase64}`}
            alt={category}
            className={`${cardImageClass}`}
          />
        ) : (
          <img
            draggable={false}
            src={`/categories/${category}.png`}
            alt={category}
            className={`${cardCategoryImageClass}`}
          />
        )}
        <div className={`${nameContainerClass}`}>
          <p
            title="NAME"
            className={`${cardNameClass} ${(discovered || renderingFrom == "BOOSTER") && name.length > 28 && "whitespace-nowrap animate-marquee"}`}
          >
            {discovered || renderingFrom == "BOOSTER" ? name : "?"}
          </p>
        </div>
        <p title="DESCRIPTION" className={`${cardDescriptionClass}`}>
          {discovered || renderingFrom == "BOOSTER" ? description : "?"}
        </p>
      </div>

      {/* Bottom stats */}
      <div className={`${regularCardBottomStatsClass}`}>
        <div className={`${regularCardBottomStatsTwo}`}>
          <p title="RESTORE HP">
            ❤️ {discovered || renderingFrom == "BOOSTER" ? restoreHp : "?"}
          </p>
        </div>
        <div className={`${regularCardBottomStatsFour}`}>
          <p title="RESTORE MP">
            🧉 {discovered || renderingFrom == "BOOSTER" ? restoreMp : "?"}
          </p>
        </div>
      </div>
    </div>
  );
};
