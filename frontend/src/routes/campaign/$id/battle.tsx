import {
  useGetOngoingBattleForTodayByCampaignIdAndUsery,
  useGetCheckIfThereIsAnOngoingBattleForToday,
  useGetFinishedBattleForTodayByCampaignIdAndUsery,
} from "../../../hooks/useBattles";
import { useParams } from "@tanstack/react-router";
import { createFileRoute } from "@tanstack/react-router";
import BattleExecuting from "../../../components/battle/BattleExecuting";
import BattleCheckAndCreation from "../../../components/battle/BattleCheckAndCreation";
import BattleFinished from "../../../components/battle/BattleFinished";

export const Route = createFileRoute("/campaign/$id/battle")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/battle" });

  const { data: activeBattleData, isLoading: isActiveBattleLoading } =
    useGetOngoingBattleForTodayByCampaignIdAndUsery(Number(campaignId));

  const {
    data: isThereOngoingBattleToday,
    isLoading: isThereOngoingBattleLoading,
  } = useGetCheckIfThereIsAnOngoingBattleForToday(Number(campaignId));

  const { data: finishedBattleData, isLoading: isLoadingFinishedBattleData } =
    useGetFinishedBattleForTodayByCampaignIdAndUsery(Number(campaignId));
  return (
    <div>
      {isLoadingFinishedBattleData ? (
        <p>Loading...</p>
      ) : finishedBattleData ? (
        <BattleFinished {...finishedBattleData} />
      ) : isThereOngoingBattleLoading ? (
        <p>Loading...</p>
      ) : isThereOngoingBattleToday ? (
        isActiveBattleLoading ? (
          <p>Loading battle...</p>
        ) : activeBattleData ? (
          <BattleExecuting {...activeBattleData} />
        ) : (
          <p>Could not load active battle.</p>
        )
      ) : (
        <BattleCheckAndCreation campaignId={Number(campaignId)} />
      )}
    </div>
  );
}
