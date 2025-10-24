import { useGetBattleCreationAvailability } from "../../../hooks/useBattles";
import { useParams } from "@tanstack/react-router";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/campaign/$id/battle")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/battle" });
  const {
    data: isBattleCreationPossible,
    isError: isBattleCreationPossibleError,
    isLoading: isBattleCreationPossibleLoading,
  } = useGetBattleCreationAvailability(Number(campaignId));
  return (
    <div>
      {isBattleCreationPossibleLoading ? (
        <p>Loading...</p>
      ) : isBattleCreationPossibleError ? (
        <p>Error checking battle creation availability</p>
      ) : isBattleCreationPossible ? (
        <p>Create battle</p>
      ) : (
        <p>
          Not possible to create battle, try opening a booster character or come
          back tomorrow
        </p>
      )}
    </div>
  );
}
