import type { TurnResponseDto } from "../../types/battleTypes";
import type { ActionTypeEnum } from "../../types/battleTypes";
import { ClosedEyeIcon } from "../icons/ui/ClosedEyeIcon";
import { OpenEyeIcon } from "../icons/ui/OpenEyeIcon";
type TurnTableTypes = {
  turns: TurnResponseDto[];
  isOpen: boolean;
  toggleVisibility: () => void;
};
const TurnTable = ({ turns, isOpen, toggleVisibility }: TurnTableTypes) => {
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
    <div
      className={`absolute rounded-md bg-[var(--page-container-bg)] border-[var(--page-container-border)] border mx-5 top-20 px-5 right-0 transition-all duration-400 ease-in-out
      ${isOpen ? "w-150" : "w-18.5"}
      ${isOpen ? "h-110" : "h-10"}
      ${isOpen ? "overflow-y-auto" : "overflow-hidden"}
      ${!isOpen ? "translate-y-100" : ""}
      ${isOpen ? "opacity-85 hover:opacity-100" : "opacity-20 hover:opacity-60"}
      `}
    >
      <div
        onClick={() => {
          toggleVisibility();
        }}
      >
        {isOpen ? (
          <OpenEyeIcon
            data-testid="hide-turns-toggle"
            className="absolute top-3.5 left-7"
            width={32}
          />
        ) : (
          <ClosedEyeIcon
            data-testid="show-turns-toggle"
            className="absolute"
            width={32}
          />
        )}
      </div>
      {isOpen && (
        <div
          className={`text-center p-2 mt-2 text-xl bg-[var(--page-container-bg-darkerer)] rounded-md text-[var(--light-text)] `}
        >
          <p data-testid="turns-title-text">Turns</p>
        </div>
      )}
      {isOpen &&
        turns
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
