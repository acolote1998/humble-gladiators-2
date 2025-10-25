import type { BattleResponseDto } from "../../types/battleTypes";
import { CharacterCard } from "../cards/CharacterCard";
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
  return (
    <div>
      <div className="flex flex-col items-center">
        <p className="text-2xl">Enemy</p>
        <CharacterCard {...teamTwo[0]} renderingFrom="BATTLE" />
      </div>
    </div>
  );
};

export default BattleExecuting;
