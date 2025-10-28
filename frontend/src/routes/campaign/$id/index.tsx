import { createFileRoute, useParams } from "@tanstack/react-router";
import { useGetHeroExistence } from "../../../hooks/useCharacters";
import { CreateHeroButton } from "../../../components/campaigns/CreateHeroButton";
export const Route = createFileRoute("/campaign/$id/")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/" });
  const {
    data: doesHeroExist,
    isLoading: doesHeroExistLoading,
    isError: doesHeroExistError,
  } = useGetHeroExistence(Number(campaignId));

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
          <div className="pt-15  pb-10 text-center items-center justify-center flex">
            <p className="loader scale-300" />
          </div>
        ) : doesHeroExistError ? (
          <p>Error loading hero availability</p>
        ) : (
          !doesHeroExist && <CreateHeroButton campaignId={Number(campaignId)} />
        )}
      </div>
    </>
  );
}
