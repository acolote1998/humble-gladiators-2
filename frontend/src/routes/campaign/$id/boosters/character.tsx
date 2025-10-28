import { createFileRoute } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
import { useGetHeroExistence } from "../../../../hooks/useCharacters";
import { CharacterBooster } from "../../../../components/boosters/CharacterBooster";
import { useGetIsBattleOngoing } from "../../../../hooks/useBattles";
export const Route = createFileRoute("/campaign/$id/boosters/character")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({
    from: "/campaign/$id/boosters/character",
  });
  const {
    data: doesHeroExist,
    isLoading: doesHeroExistLoading,
    isError: doesHeroExistError,
  } = useGetHeroExistence(Number(campaignId));
  const { data: isBattleOngoing, isLoading: isBattleOngoingLoading } =
    useGetIsBattleOngoing(Number(campaignId));
  return (
    <div>
      {doesHeroExistLoading || isBattleOngoingLoading ? (
        <p className="loader" />
      ) : doesHeroExistError ? (
        <p>Error...</p>
      ) : doesHeroExist && !isBattleOngoing ? (
        <CharacterBooster campaignId={campaignId} />
      ) : (
        <p>You cannot open a booster during an ongoing battle</p>
      )}
    </div>
  );
}
