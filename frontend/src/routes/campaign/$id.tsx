import { createFileRoute, useParams } from "@tanstack/react-router";
import { useGetCampaignByIdForAUser } from "../../hooks/useCampaigns";
import CampaignItem from "../../components/campaigns/CampaignItem";
import { useGetCharactersByCampaignAndUser } from "../../hooks/userCharacters";

export const Route = createFileRoute("/campaign/$id")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id" });
  const { data: characterInstancesData } = useGetCharactersByCampaignAndUser(
    Number(campaignId)
  );
  const {
    data: campaignData,
    isError: isCampaignError,
    isLoading: isCampaignLoadingError,
  } = useGetCampaignByIdForAUser(Number(campaignId));
  return (
    <>
      {isCampaignError ? (
        <p>Loading</p>
      ) : isCampaignLoadingError ? (
        <p>Error loading</p>
      ) : (
        campaignData && <CampaignItem {...campaignData} />
      )}
      <p
        onClick={() => {
          console.log(characterInstancesData);
        }}
      >
        Log Characters
      </p>
    </>
  );
}
