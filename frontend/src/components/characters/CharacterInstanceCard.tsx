import type { CharacterInstanceType } from "../../types/characterTypes";
export const CharacterInstanceCard = ({
  category,
  description,
  discovered,
  name,
  rarity,
  stats,
  tier,
}: CharacterInstanceType) => {
  return (
    <div
      className="relative my-5 w-85 h-119 bg-cover bg-no-repeat p-2 select-none cursor-pointer"
      style={{ backgroundImage: "url('/templates/charCardTemplate.png')" }}
    >
      {/* Top stats */}
      <div className="grid grid-cols-4 text-sm mt-3">
        <div className="absolute left-8.5 w-20">
          <p>❤️ {stats.currentHp}</p>
        </div>
        <div className="absolute left-26 w-15">
          <p>⚔️ {stats.strength}</p>
        </div>
        <div className="absolute left-43 w-20">
          <p>🔷 {stats.currentMp}</p>
        </div>
        <div className="absolute left-61 w-20">
          <p>🔮 {stats.intelligence}</p>
        </div>
      </div>

      {/* Tier & rarity */}
      <div className="flex justify-center gap-25 absolute bottom-52.5 left-0 right-0 px-4 text-sm">
        <p>Tier {tier}</p>
        <p>Rarity {rarity}</p>
      </div>

      {/* Category & name */}
      <div className="flex flex-col items-center mt-6">
        <img
          draggable={false}
          src={`/categories/${category}.png`}
          alt={category}
          className="w-65.5 h-auto"
        />
        <p className="text-lg mt-8 ">{name}</p>
        <p className="text-sm opacity-80 text-center p-1 mt-0.5 px-7">
          {description}
        </p>
      </div>

      {/* Bottom stats */}
      <div className="grid grid-cols-5 absolute bottom-14.5 text-sm">
        <div className="absolute left-7 w-15">
          <p>LV. {stats.level}</p>
        </div>
        <div className="absolute left-21 w-15">
          <p>⚡ {stats.speed}</p>
        </div>
        <div className="absolute left-34.5 w-15">
          <p>🍀 {stats.luck}</p>
        </div>
        <div className="absolute left-48 w-15">
          <p>🪨 {stats.weight}</p>
        </div>
        <div className="absolute left-62 w-15">
          <p>📏 {stats.height}</p>
        </div>
      </div>
    </div>
  );
};
