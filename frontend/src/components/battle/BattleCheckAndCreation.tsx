import { useGetBattleCreationAvailability } from "../../hooks/useBattles";
import { useCreateABattleForTodayByCampaignIdAndUser } from "../../hooks/useBattles";
import { SandClockIcon } from "../icons/errors/SandClockIcon";
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
          data-testid="create-battle-button"
          className="
              bg-[var(--page-container-border)]
              text-[var(--light-text)]
                mx-10
                my-5
                px-5
                py-5
                text-xl
                rounded-md
                font-semibold
                hover:text-[var(--dark-text)]
                hover:bg-[var(--creation-color)] 
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
        <div className="text-lg font-semibold text-center flex items-center justify-center gap-4">
          <p>It is not possible create a battle at the moment.</p>
          <SandClockIcon width={28} />
        </div>
      )}
    </div>
  );
};

export default BattleCheckAndCreation;
