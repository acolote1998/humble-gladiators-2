import {
  createRootRoute,
  Link,
  Outlet,
  useRouterState,
  useMatchRoute,
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
  const matchRoute = useMatchRoute();
  return (
    <>
      {!matchRoute({ to: "/campaign/$id/battle" }) && <Logo />}
      {location.pathname !== "/campaign" && location.pathname !== "/" && (
        <NavBar />
      )}
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
};

export const Route = createRootRoute({ component: RootLayout });
