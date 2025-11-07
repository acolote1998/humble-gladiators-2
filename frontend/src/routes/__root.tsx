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

const RootLayout = () => {
  const { location } = useRouterState();
  return (
    <>
      <Logo />
      {location.pathname !== "/campaign" &&
        location.pathname !== "/campaign/create" &&
        location.pathname !== "/" && <NavBar />}
      <div className="absolute top-0 right-0 bg-[var(--page-container-bg)] rounded-bl-lg border-[var(--page-container-border)] border p-2 flex gap-2">
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
};

export const Route = createRootRoute({ component: RootLayout });
