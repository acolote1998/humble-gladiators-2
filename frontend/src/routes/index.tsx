import { createFileRoute } from "@tanstack/react-router";
import GetInfinityToken from "../components/GetInfinityToken";

export const Route = createFileRoute("/")({
  component: Index,
});

function Index() {
  return <GetInfinityToken />;
}
