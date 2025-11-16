import {
  createRootRoute,
  Link,
  Outlet,
  useRouterState,
} from "@tanstack/react-router";
import {
  SignInButton,
  SignOutButton,
  SignedIn,
  SignedOut,
} from "@clerk/clerk-react";
import GetInfinityToken from "../components/GetInfinityToken";
import NavBar from "../components/campaigns/NavBar";
import Logo from "../components/Logo";
import { SignOutIcon } from "@/components/icons/navbar/SignOutIcon";
import { SignInIcon } from "@/components/icons/navbar/SignInIcon";
import { CampaignIcon } from "@/components/icons/navbar/CampaignIcon";

const RootLayout = () => {
  const { location } = useRouterState();
  return (
    <>
      <Logo />
      {location.pathname !== "/campaign" &&
        location.pathname !== "/campaign/create" &&
        location.pathname !== "/" && <NavBar />}
      <div className="fixed z-5 bottom-0 xl:bottom-auto xl:top-0 right-0 bg-[var(--page-container-bg)] rounded-tl-lg xl:rounded-tl-none xl:rounded-bl-lg border-[var(--page-container-border)] border p-2 flex gap-4">
        <SignedIn>
          {/* <GetInfinityToken /> */}
          <Link to="/campaign">
            <div className="flex cursor-pointer gap-2">
              <CampaignIcon width={16} />
              <p className="font-semibold">All Campaigns</p>
            </div>
          </Link>
          <SignOutButton>
            <div className="flex cursor-pointer">
              <SignOutIcon width={24} />
            </div>
          </SignOutButton>
        </SignedIn>
        <SignedOut>
          <SignInButton>
            <div className="flex cursor-pointer gap-1">
              <SignInIcon width={24} />
              <p className="font-semibold">Sign in to see your campaigns</p>
            </div>
          </SignInButton>
        </SignedOut>
      </div>
      <Outlet />
    </>
  );
};

export const Route = createRootRoute({ component: RootLayout });
