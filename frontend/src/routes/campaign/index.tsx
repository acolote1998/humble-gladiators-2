import { createFileRoute } from "@tanstack/react-router";
import { useNavigate } from "@tanstack/react-router";
import { SignedIn } from "@clerk/clerk-react";
import { useGetAllCampaignsForAUser } from "../../hooks/useCampaigns";
import CampaignInfo from "../../components/campaigns/CampaignInfo";
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
      <div className="flex items-center justify-center">
        <h1
          className="
          text-6xl
          tracking-tighter
          font-extrabold
          text-center
          p-6
          rounded-b-2xl
          border-l-5
          border-r-5
          border-b-5
        border-gray-400 
        bg-gray-200"
        >
          Humble Gladiators 2
        </h1>
      </div>
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
                key={campaign.id}
                onClick={() => {
                  navigate({ to: `/campaign/${campaign.id}` });
                }}
              >
                <CampaignInfo {...campaign} />
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
