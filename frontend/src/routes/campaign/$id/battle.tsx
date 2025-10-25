import {
  useGetBattleCreationAvailability,
  useCreateABattleForTodayByCampaignIdAndUser,
  useGetBattleForTodayByCampaignIdAndUsery,
} from "../../../hooks/useBattles";
import { useParams } from "@tanstack/react-router";
import { createFileRoute } from "@tanstack/react-router";
import BattleExecuting from "../../../components/battle/BattleExecuting";
import { useEffect } from "react";

export const Route = createFileRoute("/campaign/$id/battle")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/battle" });
  const {
    mutate: createBattle,
    data: createdBattleData,
    isPending: isBattleCreationPending,
  } = useCreateABattleForTodayByCampaignIdAndUser();
  const {
    data: isBattleCreationPossible,
    isError: isBattleCreationPossibleError,
    isLoading: isBattleCreationPossibleLoading,
  } = useGetBattleCreationAvailability(Number(campaignId));
  const {
    data: activeBattleData,
    isLoading: isActiveBattleLoading,
    refetch: fetchCreatedBattlePurposly,
  } = useGetBattleForTodayByCampaignIdAndUsery(Number(campaignId));

  useEffect(() => {
    if (!createdBattleData && !isBattleCreationPending) {
      fetchCreatedBattlePurposly();
    }
  }, [createdBattleData, fetchCreatedBattlePurposly, isBattleCreationPending]);
  return (
    <div>
      {createdBattleData ? (
        <BattleExecuting {...createdBattleData} />
      ) : !isBattleCreationPossible && isActiveBattleLoading ? (
        <p>Loading battle...</p>
      ) : (
        activeBattleData &&
        activeBattleData.onGoing && <BattleExecuting {...activeBattleData} />
      )}
      {!activeBattleData &&
        (isBattleCreationPossibleLoading ? (
          <p>Loading battle creation availability...</p>
        ) : isBattleCreationPossibleError ? (
          <p>Error checking battle creation availability</p>
        ) : isBattleCreationPossible && !isBattleCreationPending ? (
          <p
            onClick={() => {
              createBattle(Number(campaignId));
            }}
          >
            Create battle
          </p>
        ) : (
          <p>
            Not possible to create battle, try opening a booster character or
            come back tomorrow
          </p>
        ))}
    </div>
  );
}
