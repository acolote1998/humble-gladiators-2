import { createFileRoute } from "@tanstack/react-router";
import { useNavigate } from "@tanstack/react-router";
import { SignedIn } from "@clerk/clerk-react";
export const Route = createFileRoute("/campaigns/")({
  component: CampaignsRoute,
});

function CampaignsRoute() {
  const navigate = useNavigate();
  return (
    <>
      <SignedIn>
        <div className="p-2">
          <button
            onClick={() => {
              navigate({ to: "/campaigns/create" });
            }}
          >
            Create Campaign
          </button>
        </div>
      </SignedIn>
    </>
  );
}
