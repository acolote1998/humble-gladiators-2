import { createFileRoute } from "@tanstack/react-router";
import CreateCampaign from "../../components/campaigns/CreateCampaign";
import { SignedIn } from "@clerk/clerk-react";
export const Route = createFileRoute("/campaign/create")({
  component: RouteComponent,
});

function RouteComponent() {
  return (
    <>
      <SignedIn>
        <CreateCampaign />
      </SignedIn>
    </>
  );
}
