import { createFileRoute } from "@tanstack/react-router";
import CreateCampaign from "../components/campaigns/CreateCampaign";

export const Route = createFileRoute("/")({
  component: Index,
});

function Index() {
  return <CreateCampaign />;
}
