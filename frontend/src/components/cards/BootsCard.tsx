import type { BootsType } from "../../types/bootsTypes";
import { calculateTierAndRarityStars } from "../../util/calculateTierAndRarityStars";
import { useEquipItems, useUnequipItems } from "../../hooks/useEquipItems";
import { EquipItemComponent } from "../campaigns/inventory/EquipItemComponent";
import UnequipItemComponent from "../campaigns/inventory/UnequipItemComponent";
import {
  bottomStatOneClass,
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
  topStatsClass,
} from "./util/CardSizes";
export const BootsCard = ({
  id,
  campaignId,
  category,
  description,
  discovered,
  name,
  rarity,
  tier,
  magicalDefense,
  physicalDefense,
  imgBase64,
  renderingFrom,
  equipped,
}: BootsType) => {
  const { mutate: equipItem } = useEquipItems();
  const { mutate: unequipItem } = useUnequipItems();
  return (
    <>
      <div data-testid="boots-card">
        <div
          className={[
            // Conditional classes
            renderingFrom == "BOOSTER" ? "rotate-y-180" : "",
            discovered && renderingFrom != "BOOSTER" ? "hover-zoom" : "",
            (discovered || renderingFrom == "BOOSTER") &&
            tier === 5 &&
            rarity === 5
              ? "boot-tier-5-rarity-5"
              : discovered || renderingFrom == "BOOSTER"
                ? `boot-tier-${tier}`
                : "",
            // Always-applied classes
            `${discovered && `rarity-${rarity}`} ${cardSizeClass}`,
          ]
            .filter(Boolean) // removes empty strings
            .join(" ")} // join with spaces
          style={{ backgroundImage: `url('/templates/bootCardTemplate.png')` }}
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
          <div className="grid grid-cols-5 absolute bottom-14 text-sm">
            <div className={`${bottomStatOneClass}`}>
              <p title="PHYSICAL DEFENSE">
                🛡️{" "}
                {discovered || renderingFrom == "BOOSTER"
                  ? physicalDefense
                  : "?"}
              </p>
            </div>
            <div className="absolute left-41.5 w-15">
              <p title="MAGICAL DEFENSE">
                ✨{" "}
                {discovered || renderingFrom == "BOOSTER"
                  ? magicalDefense
                  : "?"}
              </p>
            </div>
          </div>
        </div>
        <div>
          {renderingFrom == "INVENTORY" && !equipped ? (
            <p
              onClick={() => {
                equipItem({
                  campaignId: campaignId,
                  itemId: id,
                  typeItemToEquip: "boots",
                });
              }}
              className="hover:opacity-100 opacity-0 text-center"
            >
              <EquipItemComponent />
            </p>
          ) : (
            renderingFrom == "INVENTORY" &&
            equipped && (
              <p
                onClick={() => {
                  unequipItem({
                    campaignId: campaignId,
                    typeItemToEquip: "boots",
                  });
                }}
                className="hover:opacity-100 opacity-0 text-center"
              >
                <UnequipItemComponent />
              </p>
            )
          )}
        </div>
      </div>
    </>
  );
};
