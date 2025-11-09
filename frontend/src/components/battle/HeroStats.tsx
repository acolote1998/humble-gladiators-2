import type { CharacterInstanceType } from "../../types/characterTypes";
import { LuckIcon } from "../icons/stats/LuckIcon";
import { MagicalDamageIcon } from "../icons/stats/MagicalDamageIcon";
import { MagicalDefenseIcon } from "../icons/stats/MagicalDefenseIcon";
import { PhysicalDamageIcon } from "../icons/stats/PhysicalDamageIcon";
import { PhysicalDefenseIcon } from "../icons/stats/PhysicalDefenseIcon";
import { SpeedIcon } from "../icons/stats/SpeedIcon";
import StatBar from "../stats/StatBar";
type HeroStatsType = {
  character: CharacterInstanceType;
};
const HeroStats = ({ character }: HeroStatsType) => {
  return (
    <>
      <div
        data-testid="battle-hero-stats"
        className="flex flex-col items-center rounded-md bg-[var(--page-container-bg-darker)] border-[var(--page-container-border)] border relative"
      >
        <p className="relative bg-[var(--page-container-bg-darker)] rounded-xl p-1 text-2xl">
          Hero Stats
        </p>
        <p className="text-xl">{character.name}</p>
        {/* HP */}
        <StatBar
          widthPercent={90}
          barHeight={2}
          currentValue={character.stats.currentHp}
          maxValue={character.stats.maxHp}
          type="HP"
        />
        {/* MP */}
        <StatBar
          widthPercent={90}
          barHeight={2}
          currentValue={character.stats.currentMp}
          maxValue={character.stats.maxMp}
          type="MANA"
        />
        {/* XP */}
        {/* <StatBar
      widthPercent={90}
      barHeight={2}
      currentValue={character.stats.currentExp}
      maxValue={character.stats.expForNextLevel}
      type="XP"
    /> */}
        <div className="grid grid-cols-3 gap-5 my-5">
          {/* Physical Damage */}
          <div
            title="Physical Damage"
            className="flex flex-col items-center cursor-pointer select-none gap-1"
          >
            <PhysicalDamageIcon width={24} />
            {character.stats.physicalDamage}
          </div>
          {/* Magical Damage */}
          <div
            title="Magical Damage"
            className="flex flex-col items-center cursor-pointer select-none gap-1"
          >
            <MagicalDamageIcon width={24} />
            {character.stats.magicalDamage}
          </div>
          {/* Physical Defense */}
          <div
            title="Physical Defense"
            className="flex flex-col items-center cursor-pointer select-none gap-1"
          >
            <PhysicalDefenseIcon width={24} />
            {character.stats.physicalDefense}
          </div>
          {/* Magical Defense */}
          <div
            title="Magical Defense"
            className="flex flex-col items-center cursor-pointer select-none gap-1"
          >
            <MagicalDefenseIcon width={24} />
            {character.stats.magicalDefense}
          </div>
          {/* Luck */}
          <div
            title="Luck"
            className="flex flex-col items-center cursor-pointer select-none gap-1"
          >
            <LuckIcon width={24} />
            {character.stats.luck}
          </div>
          {/* Speed */}
          <div
            title="Speed"
            className="flex flex-col items-center cursor-pointer select-none gap-1"
          >
            <SpeedIcon width={24} />
            {character.stats.speed}
          </div>
        </div>
      </div>
    </>
  );
};

export default HeroStats;
