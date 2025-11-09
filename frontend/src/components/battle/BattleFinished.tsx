import type {
  BattleResponseDto,
  TurnResponseDto,
} from "../../types/battleTypes";
import { CharacterCard } from "../cards/CharacterCard";
import type { CharacterInstanceType } from "../../types/characterTypes";
import { useGetRewardsForFinishedBattleOfTodayByCampaignIdAndUsery } from "../../hooks/useBattles";
import TurnTable from "./TurnTable";
import HeroStats from "./HeroStats";
import RewardsTable from "./RewardsTable";
import { Loader } from "../Loader";
import { useState } from "react";
import StatBar from "../stats/StatBar";
const BattleFinished = ({
  campaignId,
  losingTeam,
  turns,
  winningTeam,
  startingTeamOne,
  startingTeamTwo,
}: BattleResponseDto) => {
  const [areTurnsVisible, setAreTurnsVisible] = useState<boolean>(false);
  const toggleTurnVisibility = () => {
    setAreTurnsVisible((prev) => !prev);
  };
  const [areRewardsVisible, setAreRewardsVisible] = useState<boolean>(true);
  const toggleRewardsVisibility = () => {
    setAreRewardsVisible((prev) => !prev);
  };
  const { data: rewardsForBattle, isLoading: loadingRewards } =
    useGetRewardsForFinishedBattleOfTodayByCampaignIdAndUsery(
      Number(campaignId)
    );

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
          {originalEnemy.backgroundImgBase64 && (
            <img
              draggable={false}
              src={`data:image/jpeg;base64,${originalEnemy.backgroundImgBase64}`}
              className="absolute"
            />
          )}
          <div className="flex flex-col items-center relative">
            {loadingRewards ? (
              <Loader />
            ) : (
              rewardsForBattle && (
                <div className="-translate-x-222 translate-y-18">
                  <RewardsTable
                    isOpen={areRewardsVisible}
                    toggleVisibility={toggleRewardsVisibility}
                    characterOneName={originalHero.name}
                    characterTwoName={originalEnemy.name}
                    rewardsForBattle={rewardsForBattle}
                  />
                </div>
              )
            )}
            <div className="mt-5">
              <StatBar
                barHeight={2}
                currentValue={simulatedEnemy.stats.currentHp}
                maxValue={simulatedEnemy.stats.maxHp}
                type="HP"
                widthPercent={100}
              />
              <StatBar
                barHeight={2}
                currentValue={simulatedEnemy.stats.currentMp}
                maxValue={simulatedEnemy.stats.maxMp}
                type="MANA"
                widthPercent={100}
              />
              <CharacterCard {...simulatedEnemy} renderingFrom="BATTLE" />
            </div>
          </div>
          <div className="-translate-y-132">
            <TurnTable
              turns={turns}
              isOpen={areTurnsVisible}
              toggleVisibility={toggleTurnVisibility}
            />
          </div>
        </div>
        <div className="grid grid-cols-7 mt-4">
          <HeroStats character={simulatedHero} />
        </div>
      </>
    </div>
  );
};

export default BattleFinished;
