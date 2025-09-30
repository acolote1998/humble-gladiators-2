import { createFileRoute } from "@tanstack/react-router";
import { useNavigate } from "@tanstack/react-router";
import { SignedIn } from "@clerk/clerk-react";
import { useGetAllCampaignsForAUser } from "../../hooks/useCampaigns";
import CampaignItem from "../../components/campaigns/CampaignItem";
export const Route = createFileRoute("/campaign/")({
  component: CampaignsRoute,
});

function CampaignsRoute() {
  const navigate = useNavigate();
  const {
    data: allCampaigns,
    isError: isErrorLoadingAllCampaigns,
    isLoading: isLoadingAllCampaigns,
  } = useGetAllCampaignsForAUser();
  return (
    <>
      <SignedIn>
        <div className="p-2">
          <p>Your Campaigns:</p>
          {isLoadingAllCampaigns ? (
            <p>Loading campaigns</p>
          ) : isErrorLoadingAllCampaigns ? (
            <p>Error loading campaigns</p>
          ) : allCampaigns && allCampaigns.length > 0 ? (
            allCampaigns.map((campaign) => (
              <div
                onClick={() => {
                  navigate({ to: `/campaign/${campaign.id}` });
                }}
              >
                <CampaignItem key={campaign.id} {...campaign} />
              </div>
            ))
          ) : (
            <p>No campaigns found</p>
          )}
          <button
            className="bg-[var(--div-lighter-bg)] p-2 rounded-md"
            onClick={() => {
              navigate({ to: "/campaign/create" });
            }}
          >
            Create Campaign
          </button>
        </div>
      </SignedIn>
    </>
  );
}
