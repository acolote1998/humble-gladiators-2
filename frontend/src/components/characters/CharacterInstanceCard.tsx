import type { CharacterInstanceType } from "../../types/characterTypes";
import { calculateTierAndRarityStars } from "../../util/calculateTierAndRarityStars";
export const CharacterInstanceCard = ({
  category,
  description,
  discovered,
  name,
  rarity,
  stats,
  tier,
}: CharacterInstanceType) => {
  //Toggle to see all information of the card
  //   discovered = true;

  return (
    <div
      className="relative my-5 w-85 h-119 bg-cover bg-no-repeat p-2 select-none cursor-pointer"
      style={{ backgroundImage: `url('/templates/charCardTemplate.png')` }}
    >
      {/* Top stats */}
      <div className="grid grid-cols-4 text-sm mt-3">
        <div className="absolute left-8.5 w-20">
          <p>❤️ {discovered ? stats.currentHp : "?"}</p>
        </div>
        <div className="absolute left-26 w-15">
          <p>⚔️ {discovered ? stats.strength : "?"}</p>
        </div>
        <div className="absolute left-43 w-20">
          <p>🔷 {discovered ? stats.currentMp : "?"}</p>
        </div>
        <div className="absolute left-61 w-20">
          <p>🔮 {discovered ? stats.intelligence : "?"}</p>
        </div>
      </div>

      {/* Tier & rarity */}
      <div className="absolute bottom-51.5 left-0 text-sm">
        <div className="relative h-6">
          {/* container for absolute children; give it a height so top:0 has meaning */}
          <span className="absolute left-12 top-0 z-20 w-50">
            T {calculateTierAndRarityStars(tier)}
          </span>
          <span className="absolute left-46 top-0 z-10 w-50">
            R {calculateTierAndRarityStars(rarity)}
          </span>
        </div>
      </div>

      {/* Category & name */}
      <div className="flex flex-col items-center mt-6">
        <img
          draggable={false}
          src={
            discovered ? `/categories/${category}.png` : `/categories/OTHER.png`
          }
          alt={category}
          className="w-65.5 h-auto"
        />
        <p className="text-lg mt-8 ">{discovered ? name : "?"}</p>
        <p className="text-sm opacity-80 text-center p-1 mt-0.5 px-7">
          {discovered ? description : "?"}
        </p>
      </div>

      {/* Bottom stats */}
      <div className="grid grid-cols-5 absolute bottom-14.5 text-sm">
        <div className="absolute left-7 w-15">
          <p>LV. {discovered ? stats.level : "?"}</p>
        </div>
        <div className="absolute left-21 w-15">
          <p>⚡ {discovered ? stats.speed : "?"}</p>
        </div>
        <div className="absolute left-34.5 w-15">
          <p>🍀 {discovered ? stats.luck : "?"}</p>
        </div>
        <div className="absolute left-48 w-15">
          <p>🪨 {discovered ? stats.weight : "?"}</p>
        </div>
        <div className="absolute left-62 w-15">
          <p>📏 {discovered ? stats.height : "?"}</p>
        </div>
      </div>
    </div>
  );
};
