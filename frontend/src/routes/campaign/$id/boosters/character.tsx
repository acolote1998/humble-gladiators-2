import { createFileRoute } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
import { CharacterBooster } from "../../../../components/boosters/CharacterBooster";
export const Route = createFileRoute("/campaign/$id/boosters/character")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({
    from: "/campaign/$id/boosters/character",
  });

  return (
    <div
      className="
          mx-5
          p-5
          rounded-b-2xl
          border-3
        border-gray-400 
        bg-gray-200
        h-170
        "
    >
      <CharacterBooster campaignId={Number(campaignId)} />
    </div>
  );
}
