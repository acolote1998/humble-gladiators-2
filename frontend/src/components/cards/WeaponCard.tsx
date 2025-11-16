import "../../css/cards/cards-general.css";
import type { WeaponType } from "../../types/weaponTypes";
import { calculateTierAndRarityStars } from "../../util/calculateTierAndRarityStars";
import { useEquipItems, useUnequipItems } from "../../hooks/useEquipItems";
import { EquipItemComponent } from "../campaigns/inventory/EquipItemComponent";
import UnequipItemComponent from "../campaigns/inventory/UnequipItemComponent";
import {
  BottomStatOneClass,
  cardCategoryImageClass,
  cardDescriptionClass,
  cardImageClass,
  cardNameClass,
  cardSizeClass,
  categoryAndNameClass,
  nameContainerClass,
  rarityClass,
  tierAndRarityClass,
  tierAndRarityContainer,
  tierClass,
  topStatsClass,
  regularCardBottomStatsClass,
  regularCardBottomStatsThree,
} from "./util/CardSizes";
export const WeaponCard = ({
  id,
  campaignId,
  category,
  description,
  discovered,
  name,
  rarity,
  tier,
  physicalDamage,
  magicalDamage,
  imgBase64,
  renderingFrom,
  equipped,
}: WeaponType) => {
  const { mutate: equipItem } = useEquipItems();
  const { mutate: unequipItem } = useUnequipItems();
  return (
    <>
      <div data-testid="weapon-card">
        <div
          className={[
            // Conditional classes
            renderingFrom == "BOOSTER" ? "rotate-y-180" : "",
            discovered && renderingFrom != "BOOSTER" ? "hover-zoom" : "",
            (discovered || renderingFrom == "BOOSTER") &&
            tier === 5 &&
            rarity === 5
              ? "weapon-tier-5-rarity-5"
              : discovered || renderingFrom == "BOOSTER"
                ? `weapon-tier-${tier}`
                : "",
            // Always-applied classes
            `${discovered && `rarity-${rarity}`} ${cardSizeClass}`,
          ]
            .filter(Boolean) // removes empty strings
            .join(" ")} // join with spaces
          style={{
            backgroundImage: `url('/templates/weaponCardTemplate.png')`,
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
            <div className={`${BottomStatOneClass}`}>
              <p title="PHYSICAL DAMAGE">
                ⚔️{" "}
                {discovered || renderingFrom == "BOOSTER"
                  ? physicalDamage
                  : "?"}
              </p>
            </div>
            <div className={`${regularCardBottomStatsThree}`}>
              <p title="MAGICAL DAMAGE">
                ✨{" "}
                {discovered || renderingFrom == "BOOSTER" ? magicalDamage : "?"}
              </p>
            </div>
          </div>
        </div>
        <div>
          {renderingFrom == "INVENTORY" && !equipped ? (
            <EquipItemComponent
              equipItem={() => {
                equipItem({
                  campaignId: campaignId,
                  itemId: id,
                  typeItemToEquip: "weapon",
                });
              }}
            />
          ) : (
            renderingFrom == "INVENTORY" &&
            equipped && (
              <UnequipItemComponent
                unequipItem={() => {
                  unequipItem({
                    campaignId: campaignId,
                    typeItemToEquip: "weapon",
                  });
                }}
              />
            )
          )}
        </div>
      </div>
    </>
  );
};
