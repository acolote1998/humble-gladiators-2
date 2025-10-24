import {
  useGetBattleCreationAvailability,
  useCreateABattleForTodayByCampaignIdAndUser,
} from "../../../hooks/useBattles";
import { useParams } from "@tanstack/react-router";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/campaign/$id/battle")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/battle" });
  const { data: createdBattle, mutate: createBattle } =
    useCreateABattleForTodayByCampaignIdAndUser();
  const {
    data: isBattleCreationPossible,
    isError: isBattleCreationPossibleError,
    isLoading: isBattleCreationPossibleLoading,
  } = useGetBattleCreationAvailability(Number(campaignId));
  return (
    <div>
      {!createdBattle ? (
        isBattleCreationPossibleLoading ? (
          <p>Loading...</p>
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
        )
      ) : (
        <p
          onClick={() => {
            console.log(createdBattle);
          }}
        >
          Check the created battle
        </p>
      )}
    </div>
  );
}
