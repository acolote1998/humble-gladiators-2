import {
  useGetBattleCreationAvailability,
  useCreateABattleForTodayByCampaignIdAndUser,
  useGetBattleForTodayByCampaignIdAndUsery,
  useGetCheckIfThereIsAnOngoingBattleForToday,
} from "../../../hooks/useBattles";
import { useParams } from "@tanstack/react-router";
import { createFileRoute } from "@tanstack/react-router";
import BattleExecuting from "../../../components/battle/BattleExecuting";

export const Route = createFileRoute("/campaign/$id/battle")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/battle" });
  const { mutate: createBattle } =
    useCreateABattleForTodayByCampaignIdAndUser();
  const {
    data: isBattleCreationPossible,
    isLoading: isBattleCreationPossibleLoading,
  } = useGetBattleCreationAvailability(Number(campaignId));
  const { data: activeBattleData, isLoading: isActiveBattleLoading } =
    useGetBattleForTodayByCampaignIdAndUsery(Number(campaignId));

  const { data: isThereOngoingBattleToday } =
    useGetCheckIfThereIsAnOngoingBattleForToday(Number(campaignId));

  return (
    <div>
      {activeBattleData ? (
        <BattleExecuting {...activeBattleData} />
      ) : isBattleCreationPossible ? (
        <p
          onClick={() => {
            createBattle(Number(campaignId));
          }}
        >
          Create battle
        </p>
      ) : (
        <p>Not possible to create battle, open a new character booster...</p>
      )}
    </div>
  );
}
