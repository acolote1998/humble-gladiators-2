import type { HelmetType } from "../../types/helmetTypes";
import { calculateTierAndRarityStars } from "../../util/calculateTierAndRarityStars";
import { useEquipItems, useUnequipItems } from "../../hooks/useEquipItems";
import { EquipItemComponent } from "../campaigns/inventory/EquipItemComponent";
import UnequipItemComponent from "../campaigns/inventory/UnequipItemComponent";
import { cardSizeClass, topStatsClass } from "./util/CardSizes";
export const HelmetCard = ({
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
}: HelmetType) => {
  const { mutate: equipItem } = useEquipItems();
  const { mutate: unequipItem } = useUnequipItems();
  return (
    <>
      <div data-testid="helmet-card">
        <div
          className={[
            // Conditional classes
            renderingFrom == "BOOSTER" ? "rotate-y-180" : "",
            discovered && renderingFrom != "BOOSTER" ? "hover-zoom" : "",
            (discovered || renderingFrom == "BOOSTER") &&
            tier === 5 &&
            rarity === 5
              ? "helmet-tier-5-rarity-5"
              : discovered || renderingFrom == "BOOSTER"
                ? `helmet-tier-${tier}`
                : "",
            // Always-applied classes
            `${discovered && `rarity-${rarity}`} ${cardSizeClass}`,
          ]
            .filter(Boolean) // removes empty strings
            .join(" ")} // join with spaces
          style={{
            backgroundImage: `url('/templates/helmetCardTemplate.png')`,
          }}
        >
          {/* Top stats */}
          <div className={`${topStatsClass}`}></div>

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
          <div className="grid grid-cols-5 absolute bottom-14 text-sm">
            <div className="absolute left-7 w-15">
              <p title="PHYSICAL DEFENSE">
                🛡️{" "}
                {discovered || renderingFrom == "BOOSTER"
                  ? physicalDefense
                  : "?"}
              </p>
            </div>
            {/* <div className="absolute left-23 w-15">
          <p>❤️ {(discovered || renderingFrom=="BOOSTER") ? restoreHp : "?"}</p>
        </div> */}
            <div className="absolute left-41.5 w-15">
              <p title="MAGICAL DEFENSE">
                ✨{" "}
                {discovered || renderingFrom == "BOOSTER"
                  ? magicalDefense
                  : "?"}
              </p>
            </div>
            {/* <div className="absolute left-58.5 w-15">
          <p>🧉 {(discovered || renderingFrom=="BOOSTER") ? restoreMp : "?"}</p>
        </div> */}
          </div>
        </div>{" "}
        <div>
          {renderingFrom == "INVENTORY" && !equipped ? (
            <p
              onClick={() => {
                equipItem({
                  campaignId: campaignId,
                  itemId: id,
                  typeItemToEquip: "helmet",
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
                    typeItemToEquip: "helmet",
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
