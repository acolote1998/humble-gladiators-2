import {
  createFileRoute,
  useNavigate,
  useParams,
} from "@tanstack/react-router";
import { useGetCampaignByIdForAUser } from "../../../hooks/useCampaigns";
import CampaignItem from "../../../components/campaigns/CampaignItem";
import { useGetHeroByCampaignAndUser } from "../../../hooks/useCharacters";

import { useCreateCharacterBooster } from "../../../hooks/useBoosters";
import { CharacterBooster } from "../../../components/boosters/CharacterBooster";
import { ItemsBooster } from "../../../components/boosters/ItemsBooster";
export const Route = createFileRoute("/campaign/$id/")({
  component: RouteComponent,
});

function RouteComponent() {
  const navigate = useNavigate();
  const { id: campaignId } = useParams({ from: "/campaign/$id/" });
  const {
    data: heroData,
    isLoading: heroLoading,
    isError: isHeroError,
    error: heroErrorDetails,
  } = useGetHeroByCampaignAndUser(Number(campaignId));

  // Check if it's specifically a 404 error (hero not found)
  const isHeroNotFound =
    isHeroError &&
    (heroErrorDetails as Error & { response?: { status: number } })?.response
      ?.status === 404;

  const {
    mutate: createCharacterBoosterMutation,
    data: dataFromCharacterBooster,
    reset: cleanCharacterBooster,
  } = useCreateCharacterBooster();

  const {
    data: campaignData,
    isError: isCampaignError,
    isLoading: isCampaignLoadingError,
  } = useGetCampaignByIdForAUser(Number(campaignId));
  return (
    <>
      {isCampaignError ? (
        <p>Loading</p>
      ) : isCampaignLoadingError ? (
        <p>Error loading</p>
      ) : (
        campaignData && <CampaignItem {...campaignData} />
      )}
      {isHeroNotFound ? (
        <p
          onClick={() => {
            navigate({ to: `/campaign/${campaignId}/createHero` });
          }}
        >
          Click here to create your hero
        </p>
      ) : isHeroError ? (
        <p>Error loading hero. Please try again.</p>
      ) : heroLoading ? (
        <p>Loading hero</p>
      ) : (
        heroData && (
          <>
            <p
              onClick={() => {
                console.log(heroData);
              }}
              className="bg-gray-400 p-3 rounded-lg"
            >
              Log Hero Data
            </p>

            <ItemsBooster campaignId={campaignId} />
            <p
              onClick={() => {
                createCharacterBoosterMutation(Number(campaignId));
              }}
              className="bg-gray-400 p-3 rounded-lg"
            >
              Open Character Booster
            </p>
            {dataFromCharacterBooster && (
              <CharacterBooster
                {...dataFromCharacterBooster}
                cleanCharacterBooster={cleanCharacterBooster}
              />
            )}
            <p
              onClick={() => {
                navigate({ to: `/campaign/${campaignId}/compendium` });
              }}
              className="bg-gray-400 p-3 rounded-lg"
            >
              Go to the compendium
            </p>
          </>
        )
      )}
      {campaignData?.coverImgBase64 && (
        <img
          draggable={false}
          src={`data:image/jpeg;base64,${campaignData?.coverImgBase64}`}
          alt={campaignData?.name}
        />
      )}
    </>
  );
}
