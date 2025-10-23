import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/campaign/$id/inventory")({
  component: RouteComponent,
});

function RouteComponent() {
  return <div>Hello "/campaign/$id/inventory"!</div>;
}
