import type {
  BattleResponseDto,
  TurnResponseDto,
} from "../../types/battleTypes";
import { CharacterCard } from "../cards/CharacterCard";
import type { CharacterInstanceType } from "../../types/characterTypes";
import { useGetRewardsForFinishedBattleOfTodayByCampaignIdAndUsery } from "../../hooks/useBattles";
import { useNavigate } from "@tanstack/react-router";
import TurnTable from "./TurnTable";
import HeroStats from "./HeroStats";
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
  const navigate = useNavigate();

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

  const getSimulatedCharacter = (charToSimulate: CharacterInstanceType) => {
    const allSnapshotsOfThisBattle = [...startingTeamOne, ...startingTeamTwo];
    const foundSnapShotIndex = allSnapshotsOfThisBattle.findIndex((c) => {
      return (
        c.name === charToSimulate.name &&
        c.description === charToSimulate.description
      );
    });
    const snapShotCharacter = allSnapshotsOfThisBattle[foundSnapShotIndex];
    const simulatedCharacter: CharacterInstanceType = {
      ...charToSimulate,
      stats: { ...snapShotCharacter.stats },
    };
    return simulatedCharacter;
  };

  const simulateTurn = (turn: TurnResponseDto) => {
    const allCharacters = [simulatedHero, simulatedEnemy];
    const targetCharacterIndex = allCharacters.findIndex((c) => {
      return (
        c.name === turn.targetCharacter.name &&
        c.description == turn.targetCharacter.description
      );
    });
    const targetCharacter = allCharacters[targetCharacterIndex];

    const performerCharacterIndex = allCharacters.findIndex((c) => {
      return (
        c.name === turn.performingCharacter.name &&
        c.description == turn.performingCharacter.description
      );
    });
    const performingCharacter = allCharacters[performerCharacterIndex];

    targetCharacter.stats.currentHp += turn.action.healingCaused;
    if (targetCharacter.stats.currentHp > targetCharacter.stats.maxHp) {
      targetCharacter.stats.currentHp = targetCharacter.stats.maxHp;
    }

    targetCharacter.stats.currentHp -= turn.action.damageCaused;
    if (targetCharacter.stats.currentHp < 1) {
      targetCharacter.stats.currentHp = 0;
    }

    performingCharacter.stats.currentMp -= turn.action.mpUsage;
    if (performingCharacter.stats.currentMp < 1) {
      performingCharacter.stats.currentMp = 0;
    }

    targetCharacter.stats.currentMp += turn.action.mpRecoverCaused;
    if (targetCharacter.stats.currentMp > targetCharacter.stats.maxMp) {
      targetCharacter.stats.currentMp = targetCharacter.stats.maxMp;
    }
  };

  const simulateTurns = (turns: TurnResponseDto[]) => {
    for (let i = 0; i < turns.length; i++) {
      simulateTurn(turns[i]);
    }
  };

  const originalHero = getHeroFromWinnersOrLosers(
    startingTeamOne[0].name,
    startingTeamOne[0].description
  );
  const originalEnemy = getHeroFromWinnersOrLosers(
    startingTeamTwo[0].name,
    startingTeamTwo[0].description
  );

  const simulatedHero = getSimulatedCharacter(originalHero);
  const simulatedEnemy = getSimulatedCharacter(originalEnemy);

  simulateTurns(turns);
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
                      ? originalHero.name
                      : rewardsForBattle.battleResult == "VICTORY_TEAM_TWO" &&
                        originalEnemy.name}
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
                      navigate({ to: `/campaign/${campaignId}` });
                    }}
                  >
                    Close Battle
                  </p>
                </div>
              )
            )}
            <div>
              <CharacterCard {...simulatedEnemy} renderingFrom="BATTLE" />
            </div>
          </div>
          <TurnTable turns={turns} />
        </div>
        <div className="grid grid-cols-7">
          <div className="flex flex-col items-center">
            <HeroStats character={simulatedHero} />
          </div>
        </div>
      </>
    </div>
  );
};

export default BattleFinished;
