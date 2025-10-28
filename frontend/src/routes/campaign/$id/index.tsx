import {
  createFileRoute,
  useNavigate,
  useParams,
} from "@tanstack/react-router";
import { useGetHeroExistence } from "../../../hooks/useCharacters";
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
        ) : (
          !doesHeroExist && (
            <div className="flex justify-center">
              <button
                className="
          border-gray-400 
          bg-gray-500 
          text-white
            m-20
            px-5
            py-5
            text-xl
            rounded-md
            font-semibold
            hover:text-black
            hover:bg-emerald-200
            hover:tracking-wider
            cursor-pointer
            hover:scale-110
            transition-all
            ease-in-out
            duration-800
            "
                onClick={() => {
                  navigate({ to: `/campaign/${campaignId}/createHero` });
                }}
              >
                ⚔️ Forge Your Hero 🛡️
              </button>
            </div>
          )
        )}
      </div>
    </>
  );
}
