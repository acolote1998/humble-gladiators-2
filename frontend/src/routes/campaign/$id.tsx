import { createFileRoute, useParams } from "@tanstack/react-router";
import { useGetCampaignByIdForAUser } from "../../hooks/useCampaigns";
import CampaignItem from "../../components/campaigns/CampaignItem";

export const Route = createFileRoute("/campaign/$id")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id } = useParams({ from: "/campaign/$id" });
  const {
    data: campaignData,
    isError,
    isLoading,
  } = useGetCampaignByIdForAUser(Number(id));
  return (
    <>
      {isLoading ? (
        <p>Loading</p>
      ) : isError ? (
        <p>Error loading</p>
      ) : (
        campaignData && <CampaignItem {...campaignData} />
      )}
    </>
  );
}
