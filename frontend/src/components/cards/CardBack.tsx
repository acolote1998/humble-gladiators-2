import type { CardBackDto } from "../../types/campaignTypes";
export const CardBack = ({ cardBackImgBase64 }: CardBackDto) => {
  return (
    <div>
      {cardBackImgBase64 && (
        <>
          <img
            draggable={false}
            className="rounded-xl absolute w-85 h-119 z-50"
            src={`data:image/jpeg;base64,${cardBackImgBase64}`}
          />
        </>
      )}
    </div>
  );
};
