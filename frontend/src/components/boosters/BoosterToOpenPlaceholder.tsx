import { useState } from "react";
import { CardBack } from "../cards/CardBack";
type TypeOfBooster = "CHARACTER" | "ITEM";
type BoosterPlaceHolderType = {
  cardBackImage: string;
  typeOfBooster: TypeOfBooster;
};
const BoosterToOpenPlaceholder = ({
  cardBackImage,
  typeOfBooster,
}: BoosterPlaceHolderType) => {
  const [fading, setFading] = useState<boolean>(false);
  return (
    <div
      className={`${fading ? "opacity-0 scale-0" : "opacity-100 scale-100"} transition-all duration-1500`}
      onClick={() => {
        setFading(true);
      }}
    >
      <div className="relative pl-[28vw] pt-[5vh]">
        {typeOfBooster == "ITEM" && (
          <div className="scale-70">
            <CardBack cardBackImgBase64={cardBackImage} />
          </div>
        )}

        <div className="scale-70 translate-20">
          <CardBack cardBackImgBase64={cardBackImage} />
        </div>

        {typeOfBooster == "ITEM" && (
          <div className="scale-70 translate-40">
            <CardBack cardBackImgBase64={cardBackImage} />
          </div>
        )}
      </div>
    </div>
  );
};

export default BoosterToOpenPlaceholder;
