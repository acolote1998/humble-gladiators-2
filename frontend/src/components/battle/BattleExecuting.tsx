import type { BattleResponseDto } from "../../types/battleTypes";
import { CharacterCard } from "../cards/CharacterCard";
import { ConsumableCard } from "../cards/ConsumableCard";
import { PunchCard } from "../cards/PunchCard";
import { SpellCard } from "../cards/SpellCard";
import { WeaponCard } from "../cards/WeaponCard";
const BattleExecuting = ({
  campaignId,
  currentCharacterToPlay,
  id,
  losingTeam,
  onGoing,
  teamOne,
  teamTwo,
  turns,
  winningTeam,
  startingTeamOne,
  startingTeamTwo,
}: BattleResponseDto) => {
  const isHeroEquippingWeapon = (): boolean => {
    let doesItHaveEquippedWeapon = false;
    teamOne[0].inventory.weapons.forEach((w) => {
      if (w.equipped) doesItHaveEquippedWeapon = true;
    });
    return doesItHaveEquippedWeapon;
  };
  return (
    <div>
      <div className="flex flex-col items-center">
        <p className="text-2xl">Enemy</p>
        <CharacterCard {...teamTwo[0]} renderingFrom="BATTLE" />
      </div>
      <div className="grid grid-cols-7">
        <div className="flex flex-col items-center">
          <p className="text-2xl">Hero Stats</p>
          <p className="text-xl">{teamOne[0].name}</p>
          <p>
            HP {teamOne[0].stats.currentHp}/{teamOne[0].stats.maxHp}
          </p>
          <p>
            MP {teamOne[0].stats.currentMp}/{teamOne[0].stats.maxMp}
          </p>
          <p>
            XP {teamOne[0].stats.currentExp}/{teamOne[0].stats.expForNextLevel}
          </p>
          <p>LCK {teamOne[0].stats.luck}</p>
          <p>SPD {teamOne[0].stats.speed}</p>
          <p>
            P. DMG {teamOne[0].stats.physicalDamage} / P. DEF{" "}
            {teamOne[0].stats.physicalDefense}
          </p>
          <p>
            M. DMG {teamOne[0].stats.magicalDamage} / M. DEF{" "}
            {teamOne[0].stats.magicalDefense}
          </p>
        </div>
        <div className="col-span-6 flex flex-col items-center">
          <p className="text-2xl">Hand</p>
          <div className="grid grid-cols-5">
            {isHeroEquippingWeapon() ? (
              teamOne[0].inventory.weapons.map((card) => {
                if (card.equipped) {
                  return <WeaponCard {...card} renderingFrom="BATTLE" />;
                }
              })
            ) : (
              <PunchCard />
            )}
            {teamOne[0].inventory.spells.map((card) => {
              return <SpellCard {...card} renderingFrom="BATTLE" />;
            })}
            {teamOne[0].inventory.consumables.map((card) => {
              return <ConsumableCard {...card} renderingFrom="BATTLE" />;
            })}
          </div>
        </div>
      </div>
    </div>
  );
};

export default BattleExecuting;
