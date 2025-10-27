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
          font-black
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
        <button
          className="
          border-gray-400 
          bg-gray-200 
            mx-20
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
            text-6xl
          mx-10
          p-5
          rounded-2xl
          border-5
        border-gray-400 
        bg-gray-200
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
          </div>
        </div>
      </SignedIn>
    </>
  );
}
