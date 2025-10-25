import type { BattleResponseDto } from "../../types/battleTypes";
import { ArmorCard } from "../cards/ArmorCard";
import { BootsCard } from "../cards/BootsCard";
import { CharacterCard } from "../cards/CharacterCard";
import { ConsumableCard } from "../cards/ConsumableCard";
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
  return (
    <div>
      <div className="flex flex-col items-center">
        <p className="text-2xl">Enemy</p>
        <CharacterCard {...teamTwo[0]} renderingFrom="BATTLE" />
      </div>
      <div>
        <div className="flex flex-col items-center">
          <p className="text-2xl">Hand</p>
          {teamOne[0].inventory.weapons.map((card) => {
            if (card.equipped) {
              return <WeaponCard {...card} renderingFrom="BATTLE" />;
            }
          })}
          {teamOne[0].inventory.spells.map((card) => {
            return <SpellCard {...card} renderingFrom="BATTLE" />;
          })}
          {teamOne[0].inventory.consumables.map((card) => {
            return <ConsumableCard {...card} renderingFrom="BATTLE" />;
          })}
        </div>
      </div>
    </div>
  );
};

export default BattleExecuting;
