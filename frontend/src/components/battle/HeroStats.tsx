import type { CharacterInstanceType } from "../../types/characterTypes";
type HeroStatsType = {
  character: CharacterInstanceType;
};
const HeroStats = ({ character }: HeroStatsType) => {
  return (
    <>
      <p className="text-2xl">Hero Stats</p>
      <p className="text-xl">{character.name}</p>
      <p>
        HP {character.stats.currentHp}/{character.stats.maxHp}
      </p>
      <p>
        MP {character.stats.currentMp}/{character.stats.maxMp}
      </p>
      <p>
        XP {character.stats.currentExp}/{character.stats.expForNextLevel}
      </p>
      <p>LCK {character.stats.luck}</p>
      <p>SPD {character.stats.speed}</p>
      <p>
        P. DMG {character.stats.physicalDamage} / P. DEF{" "}
        {character.stats.physicalDefense}
      </p>
      <p>
        M. DMG {character.stats.magicalDamage} / M. DEF{" "}
        {character.stats.magicalDefense}
      </p>
    </>
  );
};

export default HeroStats;
