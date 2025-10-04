import type { ArmorTemplateType } from "../../types/armorTypes";
export const ArmorTemplateCard = ({
  category,
  description,
  discovered,
  name,
  rarity,
  tier,
  magicalDefense,
  physicalDefense,
}: ArmorTemplateType) => {
  //Toggle to see all information of the card
  discovered = true;

  return (
    <div
      className="relative my-5 w-85 h-119 bg-cover bg-no-repeat p-2 select-none cursor-pointer"
      style={{ backgroundImage: `url('/templates/armorCardTemplate.png')` }}
    >
      {/* Top stats */}
      <div className="grid grid-cols-4 text-sm mt-3">
        {/* <div className="absolute left-8.5 w-20">
          <p>❤️ {discovered ? "pl" : "?"}</p>
        </div>
        <div className="absolute left-26 w-15">
          <p>⚔️ {discovered ? "pl" : "?"}</p>
        </div>
        <div className="absolute left-43 w-20">
          <p>🔷 {discovered ? "pl" : "?"}</p>
        </div>
        <div className="absolute left-61 w-20">
          <p>🔮 {discovered ? "pl" : "?"}</p>
        </div> */}
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
      <div className="grid grid-cols-5 absolute bottom-14 text-sm">
        <div className="absolute left-7 w-15">
          <p>🛡️ {discovered ? physicalDefense : "?"}</p>
        </div>
        {/* <div className="absolute left-23 w-15">
          <p>❤️ {discovered ? restoreHp : "?"}</p>
        </div> */}
        <div className="absolute left-41.5 w-15">
          <p>✨ {discovered ? magicalDefense : "?"}</p>
        </div>
        {/* <div className="absolute left-58.5 w-15">
          <p>🧉 {discovered ? restoreMp : "?"}</p>
        </div> */}
      </div>
    </div>
  );
};
