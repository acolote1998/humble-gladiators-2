import type { TurnResponseDto } from "../../types/battleTypes";
import type { ActionTypeEnum } from "../../types/battleTypes";
type TurnTableTypes = {
  turns: TurnResponseDto[];
};
const TurnTable = ({ turns }: TurnTableTypes) => {
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
  return (
    <div className="absolute rounded-md bg-[var(--page-container-bg)] border-[var(--page-container-border)] border mx-5 top-20 px-5 right-0 w-150 h-110 overflow-y-auto">
      <h2 className="text-center p-2 mt-2 text-xl bg-[var(--page-container-bg-darkerer)] rounded-md text-[var(--light-text)]">
        Turns
      </h2>
      {turns
        .slice()
        .reverse()
        .map((turn, index) => (
          <p
            key={turns.length - index}
            className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]"
          >
            Turn {turns.length - index} - {turn.performingCharacter.name}{" "}
            performed {turnActionToText(turn.action.actionType)} on{" "}
            {turn.targetCharacter.name}
            {turn.action.damageCaused > 0 &&
              ` and caused ${turn.action.damageCaused} damage`}
            {turn.action.healingCaused > 0 &&
              ` and healed ${turn.action.healingCaused} heal points`}
            {turn.action.mpUsage > 0 && ` and used ${turn.action.mpUsage} mp`}
            {turn.action.mpRecoverCaused > 0 &&
              ` and recovered ${turn.action.mpRecoverCaused} mp`}
          </p>
        ))}
    </div>
  );
};

export default TurnTable;
