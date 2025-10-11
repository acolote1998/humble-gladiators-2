import {
  createFileRoute,
  useNavigate,
  useParams,
} from "@tanstack/react-router";
import {
  useCreateHero,
  useGetHeroByCampaignAndUser,
} from "../../../hooks/userCharacters";
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
    isLoading: heroLoading,
  } = useGetHeroByCampaignAndUser(Number(campaignId));

  useEffect(() => {
    if (heroData) {
      navigate({ to: `/campaign/${campaignId}` });
    }
  }, [heroData, campaignId, navigate]);

  return (
    <div>
      {heroLoading ? (
        <p>Loading...</p>
      ) : (
        heroError && (
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
        )
      )}
    </div>
  );
}
