import type { CardBackDto } from "../../types/campaignTypes";
export const CardBack = ({ cardBackImgBase64 }: CardBackDto) => {
  return (
    <div>
      {cardBackImgBase64 && (
        <>
          <img
            draggable={false}
            className="rounded-xl absolute 
            w-21.25 md:w-44 lg:w-68 xl:w-85 
            h-29.75 md:h-59 lg:h-95 xl:h-119 z-50"
            src={`data:image/jpeg;base64,${cardBackImgBase64}`}
          />
        </>
      )}
    </div>
  );
};
