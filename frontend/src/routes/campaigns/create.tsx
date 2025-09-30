import { createFileRoute } from "@tanstack/react-router";
import CreateCampaign from "../../components/campaigns/CreateCampaign";

export const Route = createFileRoute("/campaigns/create")({
  component: RouteComponent,
});

function RouteComponent() {
  return <CreateCampaign />;
}
