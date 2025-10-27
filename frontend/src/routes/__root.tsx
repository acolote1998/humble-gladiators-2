import { createRootRoute, Link, Outlet } from "@tanstack/react-router";
import {
  SignInButton,
  SignOutButton,
  SignedIn,
  SignedOut,
} from "@clerk/clerk-react";
import GetInfinityToken from "../components/GetInfinityToken";

const RootLayout = () => (
  <>
    <div className="absolute right-0 bg-gray-200 rounded-bl-lg border-gray-400 border p-2 flex gap-2">
      <Link to="/campaign">Campaigns</Link>
      <SignedIn>
        {/* <GetInfinityToken /> */}
        <SignOutButton />
      </SignedIn>
      <SignedOut>
        <SignInButton />
      </SignedOut>
    </div>
    <Outlet />
  </>
);

export const Route = createRootRoute({ component: RootLayout });
