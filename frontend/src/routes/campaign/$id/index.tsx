import {
  createFileRoute,
  useNavigate,
  useParams,
} from "@tanstack/react-router";
import { useGetCampaignByIdForAUser } from "../../../hooks/useCampaigns";
import CampaignItem from "../../../components/campaigns/CampaignItem";
import { useGetHeroByCampaignAndUser } from "../../../hooks/userCharacters";
import { useCreateItemBooster } from "../../../hooks/useBoosters";
export const Route = createFileRoute("/campaign/$id/")({
  component: RouteComponent,
});

function RouteComponent() {
  const navigate = useNavigate();
  const { id: campaignId } = useParams({ from: "/campaign/$id/" });
  const {
    data: heroData,
    isError: heroError,
    isLoading: heroLoading,
  } = useGetHeroByCampaignAndUser(Number(campaignId));
  const { mutate: createItemBoosterMutation, data: dataFromItemBooster } =
    useCreateItemBooster();

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
      {heroLoading ? (
        "Loading hero"
      ) : heroData ? (
        <>
          <p
            onClick={() => {
              console.log(heroData);
            }}
            className="bg-gray-400 p-3 rounded-lg"
          >
            Log Hero Data
          </p>
          <p
            onClick={() => {
              createItemBoosterMutation(Number(campaignId));
            }}
            className="bg-gray-400 p-3 rounded-lg"
          >
            Open Item Booster
          </p>
          {dataFromItemBooster && (
            <p
              onClick={() => {
                console.log(dataFromItemBooster);
              }}
              className="bg-gray-400 p-3 rounded-lg"
            >
              Log Item Booster
            </p>
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
      ) : (
        heroError && (
          <p
            onClick={() => {
              navigate({ to: `/campaign/${campaignId}/createHero` });
            }}
          >
            Click here to create your hero
          </p>
        )
      )}
    </>
  );
}
