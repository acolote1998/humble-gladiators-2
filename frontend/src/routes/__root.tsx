import { createRootRoute, Link, Outlet } from "@tanstack/react-router";
import {
  SignInButton,
  SignOutButton,
  SignedIn,
  SignedOut,
} from "@clerk/clerk-react";
import GetInfinityToken from "../components/GetInfinityToken";
import NavBar from "../components/campaigns/NavBar";

const RootLayout = () => (
  <>
    <div className="flex items-center justify-center">
      <h1
        className="
          text-6xl
          tracking-tighter
          font-black
          text-center
          p-6
          rounded-b-2xl
          border-l-5
          border-r-5
          border-b-5
        border-gray-400 
        bg-gray-200"
      >
        Humble Gladiators 2
      </h1>
    </div>
    <NavBar />
    <div className="absolute top-0 right-0 bg-gray-200 rounded-bl-lg border-gray-400 border p-2 flex gap-2">
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
