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
    <div>
      <ItemsBooster campaignId={campaignId} />
    </div>
  );
}
