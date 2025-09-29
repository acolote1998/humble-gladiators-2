import {
  SignInButton,
  SignedIn,
  SignOutButton,
  SignedOut,
} from "@clerk/clerk-react";
import { useAuth } from "@clerk/clerk-react";

const GetInfinityToken = () => {
  const { getToken } = useAuth();
  return (
    <>
      <SignedOut>
        <SignInButton />
      </SignedOut>
      <SignedIn>
        <p
          onClick={async () => {
            const token = await getToken({ template: "infinityToken" });
            console.log(token);
          }}
        >
          Token
        </p>
        <SignOutButton />
      </SignedIn>
    </>
  );
};

export default GetInfinityToken;
