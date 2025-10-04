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
  const getCategory = () => {
    return `/categories/${category}.png`;
  };
  return (
    <div className="w-100 h-150 relative">
      <p className="absolute z-1 left-15 top-6 font-bold">
        ❤️ {stats.currentHp}
      </p>
      <p className="absolute z-1 left-34 top-6 font-bold">
        {
          //PHYSICAL DAMAGE => AFFECTED BY THE STRENGTH
        }
        ⚔️ {stats.strength}
      </p>
      <p className="absolute z-1 left-56 top-6 font-bold">
        🔷 {stats.currentMp}
      </p>
      <p className="absolute z-1 left-75 top-6 font-bold">
        {
          //MAGIC DAMAGE => AFFECTED BY THE INTELLIGENCE
        }
        🔮 {stats.intelligence}
      </p>
      <p className="absolute z-1 left-34 top-80 font-bold">{name}</p>
      <p className="absolute z-1 left-25 top-73 font-bold">Tier {tier}</p>
      <p className="absolute z-1 left-62 top-73 font-bold">Rarity {rarity}</p>
      <p className="absolute z-1 left-12 top-90 font-thin">{description}</p>
      <img
        className="absolute z-1 h-57 w-auto left-12 top-13"
        src={getCategory()}
      />
      <p className="absolute z-1 left-13 top-122.5 font-bold">
        LV. {stats.level}
      </p>
      <p className="absolute z-1 left-27 top-122.5 font-bold">
        ⚡ {stats.speed}
      </p>
      <p className="absolute z-1 left-44 top-122.5 font-bold">
        🍀 {stats.luck}
      </p>
      <p className="absolute z-1 left-60.5 top-122.5 font-bold">
        🪨 {stats.weight}
      </p>
      <p className="absolute z-1 left-75 top-122.5 font-bold">
        📏 {stats.height}
      </p>
      <img className="absolute z-0" src="/templates/charCardTemplate.png" />
    </div>
  );
};
