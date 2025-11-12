import type { ConsumableType } from "../../types/consumablesTypes";
import { calculateTierAndRarityStars } from "../../util/calculateTierAndRarityStars";
import {
  cardSizeClass,
  rarityClass,
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
      <div className="flex flex-col items-center mt-6">
        {(discovered || renderingFrom == "BOOSTER") && imgBase64 ? (
          <img
            draggable={false}
            src={`data:image/jpeg;base64,${imgBase64}`}
            alt={category}
            className="w-66 h-48.5"
          />
        ) : (
          <img
            draggable={false}
            src={`/categories/${category}.png`}
            alt={category}
            className="w-65.5 h-auto"
          />
        )}
        <div className="w-67.75 overflow-x-hidden">
          <p
            title="NAME"
            className={`text-lg mt-8 text-center ${(discovered || renderingFrom == "BOOSTER") && name.length > 28 && "whitespace-nowrap animate-marquee"}`}
          >
            {discovered || renderingFrom == "BOOSTER" ? name : "?"}
          </p>
        </div>
        <p
          title="DESCRIPTION"
          className="text-sm opacity-80 text-center p-1 mt-0.5 px-7"
        >
          {discovered || renderingFrom == "BOOSTER" ? description : "?"}
        </p>
      </div>

      {/* Bottom stats */}
      <div className="grid grid-cols-5 absolute bottom-14 text-sm">
        {/* <div className="absolute left-7 w-15">
          <p>🛡️ {(discovered || renderingFrom=="BOOSTER") ? physicalDefense : "?"}</p>
        </div> */}
        <div className="absolute left-24 w-15">
          <p title="RESTORE HP">
            ❤️ {discovered || renderingFrom == "BOOSTER" ? restoreHp : "?"}
          </p>
        </div>
        {/* <div className="absolute left-41.5 w-15">
          <p>✨ {(discovered || renderingFrom=="BOOSTER") ? magicalDefense : "?"}</p>
        </div> */}
        <div className="absolute left-59 w-15">
          <p title="RESTORE MP">
            🧉 {discovered || renderingFrom == "BOOSTER" ? restoreMp : "?"}
          </p>
        </div>
      </div>
    </div>
  );
};
