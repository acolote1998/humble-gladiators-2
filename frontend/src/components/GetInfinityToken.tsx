import { useAuth } from "@clerk/clerk-react";

const GetInfinityToken = () => {
  const { getToken } = useAuth();
  return (
    <>
      <p
        onClick={async () => {
          const token = await getToken({ template: "infinityToken" });
          console.log(token);
        }}
      >
        Token
      </p>
    </>
  );
};

export default GetInfinityToken;
