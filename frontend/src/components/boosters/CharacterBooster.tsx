import { useEffect, useState } from "react";
import type { CharacterBoosterType } from "../../types/boosterTypes";
import type { CharacterBoosterInterface } from "../../types/boosterTypes";
import { CharacterInstanceCard } from "../cards/CharacterInstanceCard";
import { useCreateCharacterBooster } from "../../hooks/useBoosters";
import { useGetCharacterBoosterAvailability } from "../../hooks/useBoosters";
import { useQueryClient } from "@tanstack/react-query";
import { CardBack } from "../cards/CardBack";

export const CharacterBooster = ({ campaignId }: CharacterBoosterInterface) => {
  const [flipped, setFlipped] = useState(false);
  const handleFlip = () => {
    if (!flipped) setFlipped(true);
  };
  const queryClient = useQueryClient();
  const {
    data: isBoosterAvailable,
    isLoading: isBoosterAvailableLoading,
    isError: isBoosterAvailableError,
  } = useGetCharacterBoosterAvailability(Number(campaignId));
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
          <p>Loading booster availability</p>
        ) : isBoosterAvailableError ? (
          <p>Error loading booster availability</p>
        ) : isBoosterAvailable && !dataFromCharacterBooster ? (
          <p
            onClick={() => {
              createCharacterBoosterMutation(Number(campaignId));
            }}
            className="bg-gray-400 p-3 rounded-lg"
          >
            Open Character Booster
          </p>
        ) : (
          <p className="bg-gray-500 p-3 rounded-lg cursor-not-allowed">
            Come back tomorrow for a new character booster!
          </p>
        )}
        {dataFromBoosterLoading ? (
          <div className="flex gap-5">
            <p>Opening booster</p>
            <p className="loader"></p>
          </div>
        ) : (
          dataFromCharacterBooster && (
            <div>
              {/* Characters */}
              {cards?.characters && cards?.characters?.length > 0 && (
                <>
                  <div
                    className={`${cards.characters[0].tier === 5 && cards.characters[0].rarity === 5 ? "character-tier-5-rarity-5" : `character-tier-${cards.characters[0].tier}`} w-fit h-fit`}
                  >
                    <div
                      className="perspective cursor-pointer"
                      onClick={handleFlip}
                    >
                      <div
                        className={`relative w-full h-full transition-transform duration-700 transform-style-preserve-3d ${flipped ? "rotate-y-180" : ""}`}
                      >
                        <div className="absolute h-full w-full backface-hidden">
                          <CardBack campaignId={String(campaignId)} />
                        </div>
                        <div className="backface-hidden"></div>
                        <CharacterInstanceCard
                          {...cards.characters[0]}
                          renderingFromBooster={true}
                        />
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
