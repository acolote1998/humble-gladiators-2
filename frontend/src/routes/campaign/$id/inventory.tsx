import { createFileRoute } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
import { useGetHeroByCampaignAndUser } from "../../../hooks/useCharacters";

export const Route = createFileRoute("/campaign/$id/inventory")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/inventory" });
  const {
    data: heroData,
    isLoading: isLoadingHero,
    isError: isErrorHero,
  } = useGetHeroByCampaignAndUser(Number(campaignId));
  return (
    <div>
      {isLoadingHero ? (
        <p>Loading hero...</p>
      ) : isErrorHero ? (
        <p>Error loading hero</p>
      ) : (
        heroData && <div>Hero data here</div>
      )}
    </div>
  );
}
