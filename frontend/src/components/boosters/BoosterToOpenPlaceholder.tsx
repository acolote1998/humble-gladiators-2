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
      <div
        className="relative 
      translate-y-10 xl:translate-y-0 2xl:translate-y-30
      translate-x-22 sm:translate-x-17 md:translate-x-20 lg:translate-x-15 xl:translate-x-25 2xl:translate-x-50
      "
      >
        {typeOfBooster == "ITEM" && (
          <div className="scale-130 sm:scale-60 lg:scale-40">
            <CardBack cardBackImgBase64={cardBackImage} />
          </div>
        )}

        <div className="scale-130 sm:scale-60 lg:scale-40 translate-20">
          <CardBack cardBackImgBase64={cardBackImage} />
        </div>

        {typeOfBooster == "ITEM" && (
          <div className="scale-130 sm:scale-60 lg:scale-40 translate-40">
            <CardBack cardBackImgBase64={cardBackImage} />
          </div>
        )}
      </div>
    </div>
  );
};

export default BoosterToOpenPlaceholder;
