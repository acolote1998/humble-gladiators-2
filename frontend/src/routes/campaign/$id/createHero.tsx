import {
  createFileRoute,
  useNavigate,
  useParams,
} from "@tanstack/react-router";
import {
  useCreateHero,
  useGetHeroByCampaignAndUser,
} from "../../../hooks/useCharacters";
import { useEffect } from "react";
import { useState } from "react";
import { Loader } from "../../../components/Loader";

export const Route = createFileRoute("/campaign/$id/createHero")({
  component: RouteComponent,
});

function RouteComponent() {
  const [heroName, setHeroName] = useState<string>("");
  const { mutate: createHero } = useCreateHero();
  const navigate = useNavigate();
  const { id: campaignId } = useParams({ from: "/campaign/$id/createHero" });
  const {
    data: heroData,
    isError: heroError,
    error: heroErrorDetails,
    isLoading: heroLoading,
  } = useGetHeroByCampaignAndUser(Number(campaignId));

  // Check if it's specifically a 404 error (hero not found)
  const isHeroNotFound =
    heroError &&
    (heroErrorDetails as Error & { response?: { status: number } })?.response
      ?.status === 404;

  useEffect(() => {
    if (heroData) {
      navigate({ to: `/campaign/${campaignId}` });
    }
  }, [heroData, campaignId, navigate]);

  return (
    <div
      className="
          mx-5
          rounded-b-2xl
          border-3
        border-gray-400 
        bg-gray-200"
    >
      {heroLoading ? (
        <Loader />
      ) : isHeroNotFound ? (
        <>
          <div className="flex justify-center my-10">
            <div className="bg-gray-300 flex flex-col text-center px-10 py-5 rounded-lg border border-gray-400 gap-5 w-150">
              <h2 className="text-3xl font-semibold">Hero Creation</h2>
              <p className="text-lg font-light italic">Type your hero's name</p>
              <input
                className="bg-white p-1 rounded-md text-center text-lg"
                type="text"
                onChange={(e) => {
                  setHeroName(e.target.value);
                }}
              />
              <button
                className="
              border-gray-500 
              bg-gray-400 
              text-white
                mx-10
                my-5
                px-5
                py-5
                text-xl
                rounded-md
                font-semibold
                hover:text-black
                hover:bg-emerald-200
                hover:tracking-wider
                cursor-pointer
                hover:scale-110
                transition-all
                ease-in-out
                duration-800
                "
                onClick={() => {
                  createHero({
                    campaignId: Number(campaignId),
                    heroName: heroName,
                  });
                }}
              >
                Forge Your Hero ⚔️
              </button>
            </div>
          </div>
        </>
      ) : heroError ? (
        <p>Error loading hero. Please try again.</p>
      ) : null}
    </div>
  );
}
