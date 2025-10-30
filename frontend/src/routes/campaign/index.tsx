import { createFileRoute } from "@tanstack/react-router";
import { useNavigate } from "@tanstack/react-router";
import { SignedIn } from "@clerk/clerk-react";
import { useGetAllCampaignsForAUser } from "../../hooks/useCampaigns";
import CampaignInfo from "../../components/campaigns/CampaignInfo";
import { Loader } from "../../components/Loader";
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
        <button
          className="
            absolute
          border-gray-500 
          bg-gray-300 
            top-60
            left-25
            mb-5
            px-5
            py-3
            text-xl
            rounded-md
            font-semibold
            hover:bg-emerald-200
            hover:tracking-wider
            cursor-pointer
            hover:scale-110
            transition-all
            ease-in-out
            duration-800
            "
          onClick={() => {
            navigate({ to: "/campaign/create" });
          }}
        >
          ➕📊 New Campaign
        </button>
        <div
          className="
          mx-10
          p-5
          rounded-2xl
          border-5
        border-gray-400 
        bg-gray-200
        min-h-60
        "
        >
          <div className="flex flex-col items-center">
            <p
              className="
            text-3xl
            rounded-md
            font-semibold
            cursor-pointer
          bg-gray-300
            px-10
            py-2
            mb-5
            "
            >
              Your Campaigns
            </p>
            {isLoadingAllCampaigns ? (
              <Loader />
            ) : isErrorLoadingAllCampaigns ? (
              <p>Error loading campaigns</p>
            ) : allCampaigns && allCampaigns.length > 0 ? (
              <div className="flex flex-col gap-5">
                {allCampaigns.map((campaign) => {
                  return (
                    <div
                      key={campaign.id}
                      data-testid={`test-${campaign.name}`}
                    >
                      <CampaignInfo {...campaign} />
                    </div>
                  );
                })}
              </div>
            ) : (
              <p>No campaigns found. Create your first one!</p>
            )}
          </div>
        </div>
      </SignedIn>
    </>
  );
}
