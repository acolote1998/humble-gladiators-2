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
    <div>
      {isBattleCreationPossibleLoading ? (
        <Loader />
      ) : isBattleCreationPossible ? (
        <p onClick={() => createBattle(Number(campaignId))}>Create battle</p>
      ) : (
        <p>Come back tomorrow for a new battle</p>
      )}
    </div>
  );
};

export default BattleCheckAndCreation;
