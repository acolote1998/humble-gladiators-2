import { useGetBattleForTodayByCampaignIdAndUsery } from "../../../hooks/useBattles";
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

  const { data: battleForTodayData, isLoading: loadingBattleForToday } =
    useGetBattleForTodayByCampaignIdAndUsery(Number(campaignId));
  return (
    <div>
      {loadingBattleForToday ? (
        <div className="pt-15  pb-10 text-center items-center justify-center flex">
          <p className="loader scale-300" />
        </div>
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
