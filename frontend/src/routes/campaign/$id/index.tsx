import {
  createFileRoute,
  useNavigate,
  useParams,
} from "@tanstack/react-router";
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

  const { data: isBattleOngoing, isLoading: isBattleOngoingLoading } =
    useGetIsBattleOngoing(Number(campaignId));

  return (
    <>
      <div
        className="
          mx-5
          pb-5
          rounded-b-2xl
          border-3
        border-gray-400 
        bg-gray-200
        "
      >
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
      </div>
    </>
  );
}
