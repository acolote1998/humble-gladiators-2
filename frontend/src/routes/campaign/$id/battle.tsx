import {
  useGetBattleCreationAvailability,
  useCreateABattleForTodayByCampaignIdAndUser,
  useGetBattleForTodayByCampaignIdAndUsery,
} from "../../../hooks/useBattles";
import { useParams } from "@tanstack/react-router";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/campaign/$id/battle")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/battle" });
  const { mutate: createBattle } =
    useCreateABattleForTodayByCampaignIdAndUser();
  const {
    data: isBattleCreationPossible,
    isError: isBattleCreationPossibleError,
    isLoading: isBattleCreationPossibleLoading,
  } = useGetBattleCreationAvailability(Number(campaignId));
  const {
    data: activeBattleData,
    isLoading: isActiveBattleLoading,
    isError: isActiveBattleError,
  } = useGetBattleForTodayByCampaignIdAndUsery(Number(campaignId));
  return (
    <div>
      {isActiveBattleLoading ? (
        <p>Loading battle...</p>
      ) : isActiveBattleError ? (
        <p>Error loading battle...</p>
      ) : (
        activeBattleData && (
          <p
            onClick={() => {
              console.log(activeBattleData);
            }}
          >
            Check the active battle
          </p>
        )
      )}
      {!activeBattleData &&
        (isBattleCreationPossibleLoading ? (
          <p>Loading battle creation availability...</p>
        ) : isBattleCreationPossibleError ? (
          <p>Error checking battle creation availability</p>
        ) : isBattleCreationPossible ? (
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
