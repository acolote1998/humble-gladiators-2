import type { BattleRewardsResponseDto } from "../../types/battleTypes";
import { useNavigate } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
type RewardsTableType = {
  characterOneName: string;
  characterTwoName: string;
  rewardsForBattle: BattleRewardsResponseDto;
};
const RewardsTable = ({
  rewardsForBattle,
  characterOneName,
  characterTwoName,
}: RewardsTableType) => {
  const navigate = useNavigate();
  const heroHasWon = () => {
    return rewardsForBattle.battleResult === "VICTORY_TEAM_ONE";
  };
  const { id: campaignId } = useParams({ from: "/campaign/$id/battle" });
  return (
    <div className="absolute rounded-md bg-[var(--page-container-bg)] border-[var(--page-container-border)] border mx-5 top-20 px-5 left-0 w-150 h-110 overflow-y-auto">
      <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darkerer)] text-xl text-center text-[var(--light-text)]">
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
      {heroHasWon() && (
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
      {rewardsForBattle.armorLoot.length > 0 &&
        rewardsForBattle.armorLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {rewardsForBattle.bootsLoot.length > 0 &&
        rewardsForBattle.bootsLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {rewardsForBattle.consumablesLoot.length > 0 &&
        rewardsForBattle.consumablesLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {rewardsForBattle.helmetsLoot.length > 0 &&
        rewardsForBattle.helmetsLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {rewardsForBattle.shieldsLoot.length > 0 &&
        rewardsForBattle.shieldsLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {rewardsForBattle.spellsLoot.length > 0 &&
        rewardsForBattle.spellsLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
      {rewardsForBattle.weaponsLoot.length > 0 &&
        rewardsForBattle.weaponsLoot.map((i) => (
          <p className="my-2 p-1 rounded-md bg-[var(--page-container-bg-darker)]">
            {i.name} - T {i.tier} - R {i.rarity}
          </p>
        ))}
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
    </div>
  );
};

export default RewardsTable;
