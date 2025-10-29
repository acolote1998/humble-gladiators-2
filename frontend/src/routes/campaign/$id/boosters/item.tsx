import { createFileRoute } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
import { ItemsBooster } from "../../../../components/boosters/ItemsBooster";
export const Route = createFileRoute("/campaign/$id/boosters/item")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({
    from: "/campaign/$id/boosters/item",
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
      <ItemsBooster campaignId={Number(campaignId)} />
    </div>
  );
}
