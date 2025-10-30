import { useGetBattleForTodayByCampaignIdAndUsery } from "../../../hooks/useBattles";
import { useParams } from "@tanstack/react-router";
import { createFileRoute } from "@tanstack/react-router";
import BattleExecuting from "../../../components/battle/BattleExecuting";
import BattleCheckAndCreation from "../../../components/battle/BattleCheckAndCreation";
import BattleFinished from "../../../components/battle/BattleFinished";
import { Loader } from "../../../components/Loader";

export const Route = createFileRoute("/campaign/$id/battle")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/battle" });

  const { data: battleForTodayData, isLoading: loadingBattleForToday } =
    useGetBattleForTodayByCampaignIdAndUsery(Number(campaignId));
  return (
    <div
      className="
          mx-5
          p-5
          rounded-b-2xl
          border-3
        border-gray-400 
        bg-gray-200
        "
      id="battle-start"
    >
      {loadingBattleForToday ? (
        <Loader />
      ) : battleForTodayData ? (
        battleForTodayData.onGoing ? (
          <BattleExecuting {...battleForTodayData} />
        ) : (
          !battleForTodayData.onGoing && (
            <BattleFinished {...battleForTodayData} />
          )
        )
      ) : (
        <BattleCheckAndCreation campaignId={Number(campaignId)} />
      )}
    </div>
  );
}
