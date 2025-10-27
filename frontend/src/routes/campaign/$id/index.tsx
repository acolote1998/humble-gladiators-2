import {
  createFileRoute,
  useNavigate,
  useParams,
} from "@tanstack/react-router";
import { useGetCampaignByIdForAUser } from "../../../hooks/useCampaigns";
import CampaignInfo from "../../../components/campaigns/CampaignInfo";
import { useGetHeroExistence } from "../../../hooks/useCharacters";
import { CharacterBooster } from "../../../components/boosters/CharacterBooster";
import { ItemsBooster } from "../../../components/boosters/ItemsBooster";
import { useGetIsBattleOngoing } from "../../../hooks/useBattles";
export const Route = createFileRoute("/campaign/$id/")({
  component: RouteComponent,
});

function RouteComponent() {
  const navigate = useNavigate();
  const { id: campaignId } = useParams({ from: "/campaign/$id/" });
  const {
    data: doesHeroExist,
    isLoading: doesHeroExistLoading,
    isError: doesHeroExistError,
  } = useGetHeroExistence(Number(campaignId));

  const {
    data: campaignData,
    isError: isCampaignError,
    isLoading: isCampaignLoading,
  } = useGetCampaignByIdForAUser(Number(campaignId));

  const { data: isBattleOngoing, isLoading: isBattleOngoingLoading } =
    useGetIsBattleOngoing(Number(campaignId));

  return (
    <>
      {isCampaignLoading ? (
        <p>Loading campaign</p>
      ) : isCampaignError ? (
        <p>Error loading campaign</p>
      ) : (
        campaignData && (
          <>
            <CampaignInfo {...campaignData} />
            {doesHeroExistLoading ? (
              <p>Loading hero availability</p>
            ) : doesHeroExistError ? (
              <p>Error loading hero availability</p>
            ) : doesHeroExist ? (
              <>
                {isBattleOngoingLoading ? (
                  <p>Loading...</p>
                ) : !isBattleOngoing ? (
                  <>
                    <p
                      onClick={() => {
                        navigate({ to: `/campaign/${campaignId}/compendium` });
                      }}
                      className="bg-gray-400 p-3 rounded-lg"
                    >
                      Go to the compendium
                    </p>
                    <ItemsBooster campaignId={campaignId} />

                    <CharacterBooster campaignId={campaignId} />

                    <p
                      onClick={() => {
                        navigate({ to: `/campaign/${campaignId}/inventory` });
                      }}
                      className="bg-gray-400 p-3 rounded-lg"
                    >
                      Hero Inventory
                    </p>
                  </>
                ) : (
                  <p className="bg-red-400 p-3 rounded-lg">
                    You cannot perform actions during an ongoing battle!!!
                  </p>
                )}
                <p
                  onClick={() => {
                    navigate({ to: `/campaign/${campaignId}/battle` });
                  }}
                  className="bg-gray-400 p-3 rounded-lg"
                >
                  Battles
                </p>
              </>
            ) : (
              <p
                className="p-2 bg-green-300 w-fit rounded-md m-2 border border-green-500"
                onClick={() => {
                  navigate({ to: `/campaign/${campaignId}/createHero` });
                }}
              >
                Click here to create your hero
              </p>
            )}
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
