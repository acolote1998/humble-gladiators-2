import { createFileRoute } from "@tanstack/react-router";
import {
  SignInButton,
  SignedIn,
  SignOutButton,
  SignedOut,
} from "@clerk/clerk-react";

export const Route = createFileRoute("/")({
  component: Index,
});

function Index() {
  return (
    <div className="p-2">
      <SignedOut>
        <SignInButton />
      </SignedOut>
      <SignedIn>
        <SignOutButton />
      </SignedIn>
    </div>
  );
}
