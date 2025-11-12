import type { CharacterInstanceType } from "../../types/characterTypes";
import { calculateTierAndRarityStars } from "../../util/calculateTierAndRarityStars";
import {
  bottomStatOneClass,
  bottomStatsClass,
  bottomStatsFiveClass,
  bottomStatsFourClass,
  bottomStatsThreeClass,
  bottomStatsTwoClass,
  cardCategoryImageClass,
  cardDescriptionClass,
  cardImageClass,
  cardNameClass,
  cardSizeClass,
  categoryAndNameClass,
  nameAndDescriptionContainerClass,
  rarityClass,
  tierAndRarityClass,
  tierAndRarityContainer,
  tierClass,
  topStatFour,
  topStatOne,
  topStatsClass,
  topStatThree,
  topStatTwo,
} from "./util/CardSizes";
export const CharacterCard = ({
  category,
  description,
  discovered,
  name,
  rarity,
  stats,
  tier,
  imgBase64,
  renderingFrom,
}: CharacterInstanceType) => {
  //Toggle to see all information of the card
  //   discovered = true;

  const getPhysicalDamageStat = () => {
    if (renderingFrom == "COMPENDIUM" || renderingFrom == "BOOSTER") {
      return stats.strength;
    } else if (renderingFrom == "BATTLE") {
      return stats.physicalDamage;
    }
  };

  const getMagicalDamageStat = () => {
    if (renderingFrom == "COMPENDIUM" || renderingFrom == "BOOSTER") {
      return stats.intelligence;
    } else if (renderingFrom == "BATTLE") {
      return stats.magicalDamage;
    }
  };

  return (
    <div
      data-testid="character-card"
      className={[
        // Conditional classes
        renderingFrom == "BOOSTER" ? "rotate-y-180" : "",
        discovered && renderingFrom != "BOOSTER" ? "hover-zoom" : "",
        (discovered || renderingFrom == "BOOSTER") && tier === 5 && rarity === 5
          ? "character-tier-5-rarity-5"
          : discovered || renderingFrom == "BOOSTER"
            ? `character-tier-${tier}`
            : "",
        // Always-applied classes
        `${discovered && `rarity-${rarity}`} ${cardSizeClass}`,
      ]
        .filter(Boolean) // removes empty strings
        .join(" ")} // join with spaces
      style={{ backgroundImage: `url('/templates/charCardTemplate.png')` }}
    >
      {/* Top stats */}
      <div className={`${topStatsClass}`}>
        <div className={`${topStatOne}`}>
          <p title="CURRENT HP">
            ❤️{" "}
            {discovered || renderingFrom == "BOOSTER" ? stats.currentHp : "?"}
          </p>
        </div>
        <div className={`${topStatTwo}`}>
          <p title="CURRENT MP">
            🔷{" "}
            {discovered || renderingFrom == "BOOSTER" ? stats.currentMp : "?"}
          </p>
        </div>
        <div className={`${topStatThree}`}>
          <p title="PHYSICAL DAMAGE">
            ⚔️{" "}
            {discovered || renderingFrom == "BOOSTER"
              ? getPhysicalDamageStat()
              : "?"}
          </p>
        </div>
        <div className={`${topStatFour}`}>
          <p title="MAGICAL DAMAGE">
            🔮{" "}
            {discovered || renderingFrom == "BOOSTER"
              ? getMagicalDamageStat()
              : "?"}
          </p>
        </div>
      </div>

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
            src={`/categories/blankCategory.png`}
            alt={category}
            className={`${cardCategoryImageClass}`}
          />
        )}
        <div className={`${nameAndDescriptionContainerClass}`}>
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
      <div className={`${bottomStatsClass}`}>
        <div className={`${bottomStatOneClass}`}>
          <p title="LEVEL">
            LV. {discovered || renderingFrom == "BOOSTER" ? stats.level : "?"}
          </p>
        </div>
        <div className={`${bottomStatsTwoClass}`}>
          <p title="SPEED">
            ⚡ {discovered || renderingFrom == "BOOSTER" ? stats.speed : "?"}
          </p>
        </div>
        <div className={`${bottomStatsThreeClass}`}>
          <p title="LUCK">
            🍀 {discovered || renderingFrom == "BOOSTER" ? stats.luck : "?"}
          </p>
        </div>
        <div className={`${bottomStatsFourClass}`}>
          {renderingFrom == "COMPENDIUM" && !discovered ? (
            <p>?</p>
          ) : renderingFrom == "BOOSTER" ||
            (renderingFrom == "COMPENDIUM" && discovered) ? (
            <p title="WEIGHT">🪨 {stats.weight}</p>
          ) : (
            renderingFrom == "BATTLE" && (
              <p title="PHYSICAL DEFENSE">❤️ {stats.physicalDefense}</p>
            )
          )}
        </div>
        <div className={`${bottomStatsFiveClass}`}>
          {renderingFrom == "COMPENDIUM" && !discovered ? (
            <p>?</p>
          ) : renderingFrom == "BOOSTER" ||
            (renderingFrom == "COMPENDIUM" && discovered) ? (
            <p title="HEIGHT">📏 {stats.height}</p>
          ) : (
            renderingFrom == "BATTLE" && (
              <p title="MAGICAL DEFENSE">🔷 {stats.magicalDefense}</p>
            )
          )}
        </div>
      </div>
    </div>
  );
};
