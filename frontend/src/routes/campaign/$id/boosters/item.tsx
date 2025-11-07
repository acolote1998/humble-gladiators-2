import { createFileRoute } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
import { ItemsBooster } from "../../../../components/boosters/ItemsBooster";
import { PageContainer } from "@/components/ui/PageContainer";
export const Route = createFileRoute("/campaign/$id/boosters/item")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({
    from: "/campaign/$id/boosters/item",
  });

  return (
    <PageContainer h="166">
      <ItemsBooster campaignId={Number(campaignId)} />
    </PageContainer>
  );
}
