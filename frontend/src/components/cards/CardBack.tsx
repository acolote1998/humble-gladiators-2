import type { CardBackType } from "../../types/cardBackType";
import { useGetCardBackForCampaign } from "../../hooks/useCampaigns";

export const CardBack = ({ campaignId }: CardBackType) => {
  const {
    data: cardBack,
    isError: cardBackError,
    isLoading: cardBackLoading,
  } = useGetCardBackForCampaign(Number(campaignId));
  return (
    <div>
      {cardBackLoading ? (
        <p>Loading card back</p>
      ) : cardBackError ? (
        <p>Card back loading</p>
      ) : (
        cardBack && (
          <>
            <img
              draggable={false}
              className="rounded-xl absolute w-85 h-119 z-50"
              src={`data:image/jpeg;base64,${cardBack.cardBackImgBase64}`}
            />
          </>
        )
      )}
    </div>
  );
};
