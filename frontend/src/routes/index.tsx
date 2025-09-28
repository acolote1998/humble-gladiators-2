import { createFileRoute } from "@tanstack/react-router";
import {
  SignInButton,
  SignedIn,
  SignOutButton,
  SignedOut,
} from "@clerk/clerk-react";
import { useAuth } from "@clerk/clerk-react";
export const Route = createFileRoute("/")({
  component: Index,
});

function Index() {
  const { getToken } = useAuth();
  return (
    <div className="p-2">
      <SignedOut>
        <SignInButton />
      </SignedOut>
      <SignedIn>
        <p
          onClick={async () => {
            console.log(await getToken());
          }}
        >
          Token
        </p>
        <SignOutButton />
      </SignedIn>
    </div>
  );
}
