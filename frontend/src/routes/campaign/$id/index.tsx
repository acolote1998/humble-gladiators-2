import { createFileRoute, useParams } from "@tanstack/react-router";
import { useGetHeroExistence } from "../../../hooks/useCharacters";
import { RedirectCreateHeroButton } from "../../../components/campaigns/RedirectCreateHeroButton";
import { Loader } from "../../../components/Loader";
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
          <Loader />
        ) : doesHeroExistError ? (
          <p>Error loading hero availability</p>
        ) : (
          !doesHeroExist && (
            <RedirectCreateHeroButton campaignId={Number(campaignId)} />
          )
        )}
      </div>
    </>
  );
}
