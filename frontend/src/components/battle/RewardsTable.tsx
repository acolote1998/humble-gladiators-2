import type { BattleRewardsResponseDto } from "../../types/battleTypes";
import { useNavigate } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
import { OpenEyeIcon } from "../icons/ui/OpenEyeIcon";
import { ClosedEyeIcon } from "../icons/ui/ClosedEyeIcon";
type RewardsTableType = {
  characterOneName: string;
  characterTwoName: string;
  rewardsForBattle: BattleRewardsResponseDto;
  isOpen: boolean;
  toggleVisibility: () => void;
};
const RewardsTable = ({
  rewardsForBattle,
  characterOneName,
  characterTwoName,
  isOpen,
  toggleVisibility,
}: RewardsTableType) => {
  const navigate = useNavigate();
  const heroHasWon = () => {
    return rewardsForBattle.battleResult === "VICTORY_TEAM_ONE";
  };
  const { id: campaignId } = useParams({ from: "/campaign/$id/battle" });
  return (
    <div
      className={`absolute rounded-md bg-[var(--page-container-bg)] border-[var(--page-container-border)] border mx-5 top-20 px-5 left-0 transition-all duration-400 ease-in-out
              ${isOpen ? "z-100" : "z-20"}
              ${isOpen ? "xl:w-150" : "w-18.5"}
              ${isOpen ? "xl:h-110" : "h-10"}
              ${isOpen ? "overflow-y-auto" : "overflow-hidden"}
              ${!isOpen ? "xl:translate-y-100" : "xl:translate-y-0"}
              ${!isOpen ? "-translate-x-4 xl:translate-x-0" : "xl:translate-x-0"}
              ${isOpen ? "opacity-100 hover:opacity-100" : "opacity-20 hover:opacity-60"}
        `}
    >
      <div
        onClick={() => {
          toggleVisibility();
        }}
      >
        {isOpen ? (
          <OpenEyeIcon
            data-testid="hide-rewards-toggle"
            className="absolute top-2.5 left-7"
            width={32}
          />
        ) : (
          <ClosedEyeIcon
            data-testid="show-rewards-toggle"
            className="absolute top-2 left-5"
            width={32}
          />
        )}
      </div>
      {isOpen && (
        <>
          <p
            data-testid="battle-finished-title-text"
            className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darkerer)] text-xl text-center text-[var(--light-text)]"
          >
            Battle Finished
          </p>
          <p
            className={`my-2 p-1 rounded-md ${
              heroHasWon()
                ? "bg-[var(--battle-victory-bg)] text-[var(--battle-victory-foreground)]"
                : "bg-[var(--battle-defeat-bg)] text-[var(--battle-defeat-foreground)]"
            }  text-xl text-center`}
          >
            {heroHasWon() ? "Victory" : "Defeat"}
          </p>
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)] text-center">
            {rewardsForBattle?.battleResult == "VICTORY_TEAM_ONE"
              ? characterOneName
              : rewardsForBattle.battleResult == "VICTORY_TEAM_TWO" &&
                characterTwoName}{" "}
            won the battle!
          </p>
        </>
      )}
      {isOpen && heroHasWon() && (
        <>
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            Exp Reward: {rewardsForBattle?.expReward}
          </p>
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            Gold Reward: {rewardsForBattle?.goldReward}
          </p>
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            Item loot:
          </p>
        </>
      )}
      {isOpen &&
        rewardsForBattle.armorLoot.length > 0 &&
        rewardsForBattle.armorLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {isOpen &&
        rewardsForBattle.bootsLoot.length > 0 &&
        rewardsForBattle.bootsLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {isOpen &&
        rewardsForBattle.consumablesLoot.length > 0 &&
        rewardsForBattle.consumablesLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {isOpen &&
        rewardsForBattle.helmetsLoot.length > 0 &&
        rewardsForBattle.helmetsLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {isOpen &&
        rewardsForBattle.shieldsLoot.length > 0 &&
        rewardsForBattle.shieldsLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {isOpen &&
        rewardsForBattle.spellsLoot.length > 0 &&
        rewardsForBattle.spellsLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {isOpen &&
        rewardsForBattle.weaponsLoot.length > 0 &&
        rewardsForBattle.weaponsLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {isOpen && (
        <div
          className="flex justify-center cursor-pointer"
          data-testid="close-battle-button"
        >
          <p
            className="my-2 p-1 rounded-md bg-[var(--highlight-color)] border-[var(--highlight-color-border)] border text-center w-fit"
            onClick={() => {
              navigate({ to: `/campaign/${campaignId}` });
            }}
          >
            Close Battle
          </p>
        </div>
      )}
    </div>
  );
};

export default RewardsTable;
