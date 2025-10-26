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
      {winningTeam.length < 1 || losingTeam.length < 1 || onGoing ? (
        <>
          {
            // If no one has played, and it is the enemy's turn (enemy is always team two)
            // then start the battle (it will reload the page which triggers the enemy's
            // turn if it is their turn.
            // otherwise, if it is the hero's turn (always team one), then we can just
            // play a card to start the battle)
          }
          {turns.length < 1 && currentCharacterToPlay.id == teamTwo[0].id ? (
            <p
              className="text-lg text-center bg-green-300"
              onClick={() => {
                window.location.reload();
              }}
            >
              Start Battle
            </p>
          ) : (
            turns.length < 1 && (
              <p className="text-lg text-center bg-green-300">
                You start, cast one of your cards by clicking on it
              </p>
            )
          )}
          <div className="flex flex-col items-center">
            <p className="text-2xl">
              Character to play: {currentCharacterToPlay.name}
            </p>
          </div>
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
                XP {teamOne[0].stats.currentExp}/
                {teamOne[0].stats.expForNextLevel}
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
        </>
      ) : (
        <div>
          <p>This battle is already finshed</p>
          <p>Winner: {winningTeam[0].name}</p>
          <p>Loser: {losingTeam[0].name}</p>
        </div>
      )}
    </div>
  );
};

export default BattleExecuting;
