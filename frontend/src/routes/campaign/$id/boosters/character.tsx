import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/campaign/$id/boosters/character")({
  component: RouteComponent,
});

function RouteComponent() {
  return <div>Hello "/campaign/$id/boosters/character"!</div>;
}
