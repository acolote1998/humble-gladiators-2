import { createFileRoute } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
import { CharacterBooster } from "../../../../components/boosters/CharacterBooster";
import { PageContainer } from "@/components/ui/PageContainer";
export const Route = createFileRoute("/campaign/$id/boosters/character")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({
    from: "/campaign/$id/boosters/character",
  });

  return (
    <PageContainer vh="76">
      <CharacterBooster campaignId={Number(campaignId)} />
    </PageContainer>
  );
}
