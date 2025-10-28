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
    <div className="absolute rounded-md bg-gray-200 border-gray-400 border mx-5 top-20 px-5 left-0 w-150 h-110 overflow-y-auto">
      <p className="my-2 p-1 rounded-md bg-gray-500 text-xl text-center text-white">
        Battle Finished
      </p>
      <p
        className={`my-2 p-1 rounded-md ${heroHasWon() ? "bg-green-400" : "bg-red-400"}  text-xl text-center text-white`}
      >
        {heroHasWon() ? "Victory" : "Defeat"}
      </p>
      <p className="my-2 p-1 rounded-md bg-gray-300 text-center">
        {rewardsForBattle?.battleResult == "VICTORY_TEAM_ONE"
          ? characterOneName
          : rewardsForBattle.battleResult == "VICTORY_TEAM_TWO" &&
            characterTwoName}{" "}
        won the battle!
      </p>
      {heroHasWon() && (
        <>
          <p className="my-2 p-1 rounded-md bg-gray-300">
            Exp Reward: {rewardsForBattle?.expReward}
          </p>
          <p className="my-2 p-1 rounded-md bg-gray-300">
            Gold Reward: {rewardsForBattle?.goldReward}
          </p>
          <p className="my-2 p-1 rounded-md bg-gray-300">Item loot:</p>
        </>
      )}
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
      <div className="flex justify-center cursor-pointer">
        <p
          className="my-2 p-1 rounded-md bg-yellow-200 border-yellow-500 border text-center w-fit"
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
