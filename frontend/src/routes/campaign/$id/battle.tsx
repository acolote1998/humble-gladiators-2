import {
  useGetBattleForTodayByCampaignIdAndUsery,
  useGetCheckIfThereIsAnOngoingBattleForToday,
} from "../../../hooks/useBattles";
import { useParams } from "@tanstack/react-router";
import { createFileRoute } from "@tanstack/react-router";
import BattleExecuting from "../../../components/battle/BattleExecuting";
import BattleCheckAndCreation from "../../../components/battle/BattleCheckAndCreation";

export const Route = createFileRoute("/campaign/$id/battle")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/battle" });

  const { data: activeBattleData, isLoading: isActiveBattleLoading } =
    useGetBattleForTodayByCampaignIdAndUsery(Number(campaignId));

  const {
    data: isThereOngoingBattleToday,
    isLoading: isThereOngoingBattleLoading,
  } = useGetCheckIfThereIsAnOngoingBattleForToday(Number(campaignId));

  return (
    <div>
      {isThereOngoingBattleLoading ? (
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
