import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/campaign/$id/boosters/item")({
  component: RouteComponent,
});

function RouteComponent() {
  return <div>Hello "/campaign/$id/boosters/item"!</div>;
}
