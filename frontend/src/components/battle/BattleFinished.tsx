import type { BattleResponseDto } from "../../types/battleTypes";
import { CharacterCard } from "../cards/CharacterCard";
import { ConsumableCard } from "../cards/ConsumableCard";
import { PunchCard } from "../cards/PunchCard";
import { SpellCard } from "../cards/SpellCard";
import { WeaponCard } from "../cards/WeaponCard";
import type { ActionTypeEnum } from "../../types/battleTypes";
import type { CharacterInstanceType } from "../../types/characterTypes";
import {
  useGetRewardsForFinishedBattleOfTodayByCampaignIdAndUsery,
  useRecoverCharactersAfterFinishedTodaysBattle,
} from "../../hooks/useBattles";
import { useNavigate } from "@tanstack/react-router";
const BattleFinished = ({
  campaignId,
  losingTeam,
  turns,
  winningTeam,
  startingTeamOne,
  startingTeamTwo,
}: BattleResponseDto) => {
  const { data: rewardsForBattle, isLoading: loadingRewards } =
    useGetRewardsForFinishedBattleOfTodayByCampaignIdAndUsery(
      Number(campaignId)
    );
  const { mutate: recoverHeroes } =
    useRecoverCharactersAfterFinishedTodaysBattle();
  const navigate = useNavigate();
  const turnActionToText = (action: ActionTypeEnum) => {
    switch (action) {
      case "SPELL":
        return "a spell";
      case "PHYSICAL_ATTACK":
        return "a physical attack";
      case "CONSUMABLE":
        return "a consumable";
    }
  };

  const isHeroEquippingWeapon = (char: CharacterInstanceType): boolean => {
    let doesItHaveEquippedWeapon = false;
    char.inventory.weapons.forEach((w) => {
      if (w.equipped) doesItHaveEquippedWeapon = true;
    });
    return doesItHaveEquippedWeapon;
  };

  const getHeroFromWinnersOrLosers = (
    heroReferenceName: string,
    heroReferenceDescription: string
  ): CharacterInstanceType => {
    const allChars: CharacterInstanceType[] = [...winningTeam, ...losingTeam];
    const foundHeroIndex = allChars.findIndex((c) => {
      return (
        c.name === heroReferenceName &&
        c.description === heroReferenceDescription
      );
    });
    return allChars[foundHeroIndex];
  };

  const hero = getHeroFromWinnersOrLosers(
    startingTeamOne[0].name,
    startingTeamOne[0].description
  );
  const enemy = getHeroFromWinnersOrLosers(
    startingTeamTwo[0].name,
    startingTeamTwo[0].description
  );
  return (
    <div>
      <>
        <div className="relative">
          <div className="flex flex-col items-center">
            <p className="text-2xl">Enemy</p>
            {loadingRewards ? (
              <p>Loading...</p>
            ) : (
              rewardsForBattle && (
                <div className="absolute rounded-md bg-gray-200 border-gray-400 border mx-5 top-20 px-5 left-0 w-150 h-110 overflow-y-scroll">
                  <p className="my-2 p-1 rounded-md bg-gray-500 text-xl text-center text-white">
                    Battle Finished
                  </p>
                  <p className="my-2 p-1 rounded-md bg-gray-300">
                    Winner:{" "}
                    {rewardsForBattle?.battleResult == "VICTORY_TEAM_ONE"
                      ? hero.name
                      : rewardsForBattle.battleResult == "VICTORY_TEAM_TWO" &&
                        enemy.name}
                  </p>
                  <p className="my-2 p-1 rounded-md bg-gray-300">
                    Exp Reward: {rewardsForBattle?.expReward}
                  </p>
                  <p className="my-2 p-1 rounded-md bg-gray-300">
                    Gold Reward: {rewardsForBattle?.goldReward}
                  </p>
                  <p className="my-2 p-1 rounded-md bg-gray-300">Item loot:</p>
                  {rewardsForBattle.armorLoot.length > 0 &&
                    rewardsForBattle.armorLoot.map((i) => (
                      <p className="my-2 p-1 rounded-md bg-gray-300">
                        {i.name} - T {i.tier} - R {i.rarity}
                      </p>
                    ))}
                  {rewardsForBattle.bootsLoot.length > 0 &&
                    rewardsForBattle.bootsLoot.map((i) => (
                      <p className="my-2 p-1 rounded-md bg-gray-300">
                        {i.name} - T {i.tier} - R {i.rarity}
                      </p>
                    ))}
                  {rewardsForBattle.consumablesLoot.length > 0 &&
                    rewardsForBattle.consumablesLoot.map((i) => (
                      <p className="my-2 p-1 rounded-md bg-gray-300">
                        {i.name} - T {i.tier} - R {i.rarity}
                      </p>
                    ))}
                  {rewardsForBattle.helmetsLoot.length > 0 &&
                    rewardsForBattle.helmetsLoot.map((i) => (
                      <p className="my-2 p-1 rounded-md bg-gray-300">
                        {i.name} - T {i.tier} - R {i.rarity}
                      </p>
                    ))}
                  {rewardsForBattle.shieldsLoot.length > 0 &&
                    rewardsForBattle.shieldsLoot.map((i) => (
                      <p className="my-2 p-1 rounded-md bg-gray-300">
                        {i.name} - T {i.tier} - R {i.rarity}
                      </p>
                    ))}
                  {rewardsForBattle.spellsLoot.length > 0 &&
                    rewardsForBattle.spellsLoot.map((i) => (
                      <p className="my-2 p-1 rounded-md bg-gray-300">
                        {i.name} - T {i.tier} - R {i.rarity}
                      </p>
                    ))}
                  {rewardsForBattle.weaponsLoot.length > 0 &&
                    rewardsForBattle.weaponsLoot.map((i) => (
                      <p className="my-2 p-1 rounded-md bg-gray-300">
                        {i.name} - T {i.tier} - R {i.rarity}
                      </p>
                    ))}
                  <p
                    className="my-2 p-1 rounded-md bg-red-400 text-center"
                    onClick={() => {
                      recoverHeroes(Number(campaignId));
                      navigate({ to: `/campaign/${campaignId}` });
                    }}
                  >
                    Close Battle
                  </p>
                </div>
              )
            )}
            <div>
              <CharacterCard {...enemy} renderingFrom="BATTLE" />
            </div>
          </div>
          <div className="absolute rounded-md bg-gray-200 border-gray-400 border mx-5 top-20 px-5 right-0 w-150 h-110 overflow-y-scroll">
            {turns
              .slice()
              .reverse()
              .map((turn, index) => (
                <p
                  key={turns.length - index}
                  className="my-2 p-1 rounded-md bg-gray-300"
                >
                  Turn {turns.length - index} - {turn.performingCharacter.name}{" "}
                  performed {turnActionToText(turn.action.actionType)} on{" "}
                  {turn.targetCharacter.name}
                  {turn.action.damageCaused > 0 &&
                    ` and caused ${turn.action.damageCaused} damage`}
                  {turn.action.healingCaused > 0 &&
                    ` and healed ${turn.action.healingCaused} heal points`}
                </p>
              ))}
          </div>
        </div>
        <div className="grid grid-cols-7">
          <div className="flex flex-col items-center">
            <p className="text-2xl">Hero Stats</p>
            <p className="text-xl">{hero.name}</p>
            <p>
              HP {hero.stats.currentHp}/{hero.stats.maxHp}
            </p>
            <p>
              MP {hero.stats.currentMp}/{hero.stats.maxMp}
            </p>
            <p>
              XP {hero.stats.currentExp}/{hero.stats.expForNextLevel}
            </p>
            <p>LCK {hero.stats.luck}</p>
            <p>SPD {hero.stats.speed}</p>
            <p>
              P. DMG {hero.stats.physicalDamage} / P. DEF{" "}
              {hero.stats.physicalDefense}
            </p>
            <p>
              M. DMG {hero.stats.magicalDamage} / M. DEF{" "}
              {hero.stats.magicalDefense}
            </p>
          </div>
          <div className="col-span-6 flex flex-col items-center">
            <p className="text-2xl">Hand</p>
            <div className="grid grid-cols-5">
              <div
                className={`transition-all ease-in-out duration-500 hover:-translate-y-62 hover:z-50 `}
              >
                {isHeroEquippingWeapon(hero) ? (
                  hero.inventory.weapons.map((card) => {
                    if (card.equipped) {
                      return <WeaponCard {...card} renderingFrom="BATTLE" />;
                    }
                  })
                ) : (
                  <PunchCard />
                )}
              </div>
              {hero.inventory.spells
                .filter((spell, index, self) => {
                  return (
                    index ===
                    self.findIndex(
                      (firstOcurrenceSpell) =>
                        firstOcurrenceSpell.name == spell.name
                    )
                  );
                })
                .map((card) => {
                  return (
                    <div
                      className={
                        `transition-all ease-in-out duration-500 hover:-translate-y-62 hover:z-50  ` +
                        `${startingTeamOne[0].stats.currentMp >= card.mpCost ? "opacity-100 " : "opacity-50 "}`
                      }
                    >
                      <SpellCard {...card} renderingFrom="BATTLE" />
                    </div>
                  );
                })}
              {hero.inventory.consumables.map((card) => {
                return (
                  <div
                    className={`transition-all ease-in-out duration-500 hover:-translate-y-62 hover:z-50  `}
                  >
                    <ConsumableCard {...card} renderingFrom="BATTLE" />
                  </div>
                );
              })}
            </div>
          </div>
        </div>
      </>
    </div>
  );
};

export default BattleFinished;
