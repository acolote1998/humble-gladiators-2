import { useEffect, useState } from "react";
import type { CharacterBoosterType } from "../../types/boosterTypes";
import type { CharacterBoosterInterface } from "../../types/boosterTypes";
import { CharacterCard } from "../cards/CharacterCard";
import { useCreateCharacterBooster } from "../../hooks/useBoosters";
import { useGetCharacterBoosterAvailability } from "../../hooks/useBoosters";
import { useQueryClient } from "@tanstack/react-query";
import { CardBack } from "../cards/CardBack";
import { useGetCardBackForCampaign } from "../../hooks/useCampaigns";
import { Loader } from "../Loader";
import BoosterToOpenPlaceholder from "./BoosterToOpenPlaceholder";

export const CharacterBooster = ({ campaignId }: CharacterBoosterInterface) => {
  const { data: cardBack } = useGetCardBackForCampaign(Number(campaignId));
  const [flipped, setFlipped] = useState(false);
  const handleFlip = () => {
    if (!flipped) setFlipped(true);
  };
  const queryClient = useQueryClient();
  const { data: isBoosterAvailable, isLoading: isBoosterAvailableLoading } =
    useGetCharacterBoosterAvailability(Number(campaignId));
  const [cards, setCards] = useState<CharacterBoosterType>();
  const isLastCard = () => {
    if (cards) return cards?.characters?.length === 1;
  };
  const isBoosterEmpty = () => {
    return cards?.characters.length === 0;
  };

  const textNextOrClose = () => {
    if (isLastCard()) {
      return "Close booster";
    } else return "Next card";
  };

  const {
    mutate: createCharacterBoosterMutation,
    data: dataFromCharacterBooster,
    isPending: dataFromBoosterLoading,
    reset: cleanCharacterBooster,
  } = useCreateCharacterBooster();

  useEffect(() => {
    if (dataFromCharacterBooster)
      setCards({
        characters: [...dataFromCharacterBooster.characters],
      });
  }, [dataFromCharacterBooster]);
  const updateRemainingCards = () => {
    if (cards)
      setCards({
        characters: [...cards.characters],
      });
    if (isBoosterEmpty() && cleanCharacterBooster) {
      cleanCharacterBooster();
      setFlipped(false);
      queryClient.invalidateQueries({
        queryKey: ["character-booster-availability"],
      });
    }
  };
  return (
    <>
      <div>
        {isBoosterAvailableLoading ? (
          <Loader />
        ) : isBoosterAvailable && !dataFromCharacterBooster ? (
          <div
            onClick={() => {
              setTimeout(() => {
                createCharacterBoosterMutation(Number(campaignId));
              }, 1200);
            }}
          >
            <h3 className="absolute text-4xl font-semibold text-center bottom-20 left-[46vw]">
              Open Booster
            </h3>
            {cardBack && (
              <BoosterToOpenPlaceholder
                typeOfBooster="CHARACTER"
                cardBackImage={cardBack.cardBackImgBase64}
              />
            )}
          </div>
        ) : (
          !dataFromCharacterBooster && (
            <div className="text-lg font-semibold text-center flex items-center justify-center">
              It is not possible to open a character booster right now.
            </div>
          )
        )}
        {dataFromBoosterLoading ? (
          <Loader />
        ) : (
          dataFromCharacterBooster && (
            <div>
              {/* Characters */}
              {cards?.characters && cards?.characters?.length > 0 && (
                <>
                  <div className={`w-fit h-fit`}>
                    <div
                      className="perspective cursor-pointer  absolute left-[50%] -translate-x-[50%] bottom-25"
                      onClick={handleFlip}
                    >
                      <div
                        className={`relative w-full h-full transition-transform duration-700 transform-style-preserve-3d ${flipped ? "rotate-y-180" : ""}`}
                      >
                        {cardBack && (
                          <div className="absolute h-full w-full backface-hidden">
                            <CardBack
                              cardBackImgBase64={cardBack?.cardBackImgBase64}
                            />
                          </div>
                        )}
                        {cardBack && (
                          <CharacterCard
                            {...cards.characters[0]}
                            renderingFrom="BOOSTER"
                          />
                        )}
                      </div>
                    </div>
                  </div>
                  {!cards.characters[0].discovered && (
                    <p className="bg-amber-300">NEW!</p>
                  )}
                  <p
                    onClick={() => {
                      cards.characters.shift();
                      updateRemainingCards();
                    }}
                    className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                  >
                    {textNextOrClose()}
                  </p>
                </>
              )}
            </div>
          )
        )}
      </div>
    </>
  );
};
