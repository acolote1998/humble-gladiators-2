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

  return (
    <div>
      <CharacterBooster campaignId={campaignId} />
    </div>
  );
}
