import { createFileRoute, useParams } from "@tanstack/react-router";
import { useGetCampaignByIdForAUser } from "../../hooks/useCampaigns";
import CampaignItem from "../../components/campaigns/CampaignItem";
import { useGetCharactersByCampaignAndUser } from "../../hooks/userCharacters";
import { useGetAllArmorTemplatesForCampaignByUser } from "../../hooks/useArmors";
import { useGetAllBootsTemplatesForCampaignByUser } from "../../hooks/useBoots";

export const Route = createFileRoute("/campaign/$id")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id" });
  const { data: characterInstancesData } = useGetCharactersByCampaignAndUser(
    Number(campaignId)
  );
  const { data: armorTemplatesData } = useGetAllArmorTemplatesForCampaignByUser(
    Number(campaignId)
  );
  const { data: bootsTemplatesData } = useGetAllBootsTemplatesForCampaignByUser(
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
      <p
        onClick={() => {
          console.log(armorTemplatesData);
        }}
      >
        Log Armors
      </p>
      <p
        onClick={() => {
          console.log(bootsTemplatesData);
        }}
      >
        Log Boots
      </p>
    </>
  );
}
