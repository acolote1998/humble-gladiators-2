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
import { PageContainer } from "@/components/ui/PageContainer";

export const Route = createFileRoute("/campaign/$id/createHero")({
  component: RouteComponent,
});

function RouteComponent() {
  const [heroName, setHeroName] = useState<string>("");
  const [createHeroButtonDisabled, setCreateHeroButtonDisabled] =
    useState<boolean>(false);
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
    <PageContainer>
      {heroLoading ? (
        <Loader />
      ) : isHeroNotFound ? (
        <>
          <div className="flex justify-center my-10">
            <div
              className={`bg-gray-300 flex flex-col text-center px-10 py-5 rounded-lg border border-gray-400 gap-5 w-150`}
            >
              <h2 className="text-3xl font-semibold">Hero Creation</h2>
              <p className="text-lg font-light italic">Type your hero's name</p>
              <input
                className="bg-white p-1 rounded-md text-center text-lg"
                data-testid="hero-name-input"
                type="text"
                onChange={(e) => {
                  setHeroName(e.target.value);
                }}
              />
              <button
                data-testid="hero-creation-button"
                disabled={createHeroButtonDisabled}
                className={`
                ${createHeroButtonDisabled ? "opacity-60 cursor-progress" : "opacity-100 cursor-pointer"}
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
                hover:scale-110
                transition-all
                ease-in-out
                duration-800
                `}
                onClick={() => {
                  setCreateHeroButtonDisabled(true);
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
    </PageContainer>
  );
}
