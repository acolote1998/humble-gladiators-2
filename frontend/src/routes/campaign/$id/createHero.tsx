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
    <div>
      {heroLoading ? (
        <p>Loading...</p>
      ) : isHeroNotFound ? (
        <>
          <div
            className="
          bg-emerald-200
            p-2
            m-2
            rounded-lg
            border-1
            border-emerald-400
            flex
            flex-col
            items-center
            text-center
            gap-4"
          >
            <h2 className="text-lg font-semibold">Hero Creation</h2>
            <p>Type your hero's name</p>
            <input
              className="bg-emerald-100 p-1 rounded-md"
              type="text"
              onChange={(e) => {
                setHeroName(e.target.value);
              }}
            />
            <button
              className="bg-emerald-300 p-2 rounded-md border-1 border-emerald-500"
              onClick={() => {
                createHero({
                  campaignId: Number(campaignId),
                  heroName: heroName,
                });
              }}
            >
              Create Hero
            </button>
          </div>
        </>
      ) : heroError ? (
        <p>Error loading hero. Please try again.</p>
      ) : null}
    </div>
  );
}
