import type { CharacterInstanceType } from "../../types/characterTypes";
import { calculateTierAndRarityStars } from "../../util/calculateTierAndRarityStars";
import { cardSizeClass } from "./util/CardSize";
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
      <div className="grid grid-cols-4 text-sm mt-3">
        <div className="absolute left-8.5 w-20">
          <p title="CURRENT HP">
            ❤️{" "}
            {discovered || renderingFrom == "BOOSTER" ? stats.currentHp : "?"}
          </p>
        </div>
        <div className="absolute left-26 w-20">
          <p title="CURRENT MP">
            🔷{" "}
            {discovered || renderingFrom == "BOOSTER" ? stats.currentMp : "?"}
          </p>
        </div>
        <div className="absolute left-43.5 w-15">
          <p title="PHYSICAL DAMAGE">
            ⚔️{" "}
            {discovered || renderingFrom == "BOOSTER"
              ? getPhysicalDamageStat()
              : "?"}
          </p>
        </div>
        <div className="absolute left-61 w-20">
          <p title="MAGICAL DAMAGE">
            🔮{" "}
            {discovered || renderingFrom == "BOOSTER"
              ? getMagicalDamageStat()
              : "?"}
          </p>
        </div>
      </div>

      {/* Tier & rarity */}
      <div className="absolute bottom-51.5 left-0 text-sm">
        <div className="relative h-6">
          {/* container for absolute children; give it a height so top:0 has meaning */}
          <span title="TIER" className="absolute left-12 top-0 z-20 w-50">
            T {calculateTierAndRarityStars(tier)}
          </span>
          <span title="RARITY" className="absolute left-46 top-0 z-10 w-50">
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
      <div className="grid grid-cols-5 absolute bottom-14.5 text-sm">
        <div className="absolute left-7 w-15">
          <p title="LEVEL">
            LV. {discovered || renderingFrom == "BOOSTER" ? stats.level : "?"}
          </p>
        </div>
        <div className="absolute left-21 w-15">
          <p title="SPEED">
            ⚡ {discovered || renderingFrom == "BOOSTER" ? stats.speed : "?"}
          </p>
        </div>
        <div className="absolute left-34.5 w-15">
          <p title="LUCK">
            🍀 {discovered || renderingFrom == "BOOSTER" ? stats.luck : "?"}
          </p>
        </div>
        <div className="absolute left-48 w-15">
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
        <div className="absolute left-62 w-15">
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
