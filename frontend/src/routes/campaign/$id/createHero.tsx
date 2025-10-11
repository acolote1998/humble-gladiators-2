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
          <p>Type your hero's name</p>
          <input
            type="text"
            onChange={(e) => {
              setHeroName(e.target.value);
            }}
          />
          <button
            onClick={() => {
              createHero({
                campaignId: Number(campaignId),
                heroName: heroName,
              });
            }}
          >
            Create Hero
          </button>
        </>
      ) : heroError ? (
        <p>Error loading hero. Please try again.</p>
      ) : null}
    </div>
  );
}
