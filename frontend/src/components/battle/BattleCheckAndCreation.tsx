import { useGetBattleCreationAvailability } from "../../hooks/useBattles";
import { useCreateABattleForTodayByCampaignIdAndUser } from "../../hooks/useBattles";
import { Loader } from "../Loader";
type BattleCreationComponentType = { campaignId: number };
const BattleCheckAndCreation = ({
  campaignId,
}: BattleCreationComponentType) => {
  const { mutate: createBattle } =
    useCreateABattleForTodayByCampaignIdAndUser();
  const {
    data: isBattleCreationPossible,
    isLoading: isBattleCreationPossibleLoading,
  } = useGetBattleCreationAvailability(Number(campaignId));
  return (
    <div className="text-lg font-semibold text-center flex items-center justify-center">
      {isBattleCreationPossibleLoading ? (
        <Loader />
      ) : isBattleCreationPossible ? (
        <p
          className="
              bg-gray-400
              text-white
                mx-10
                my-5
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
          onClick={() => createBattle(Number(campaignId))}
        >
          Click here to start a battle!
        </p>
      ) : (
        <p>It is not possible to create a battle at the moment</p>
      )}
    </div>
  );
};

export default BattleCheckAndCreation;
