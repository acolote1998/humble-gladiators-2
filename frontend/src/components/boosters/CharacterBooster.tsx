import { useEffect, useState } from "react";
import type { CharacterBoosterType } from "../../types/boosterTypes";
import type { CharacterBoosterInterface } from "../../types/boosterTypes";
import { CharacterInstanceCard } from "../characters/CharacterInstanceCard";
import { useCreateCharacterBooster } from "../../hooks/useBoosters";

export const CharacterBooster = ({ campaignId }: CharacterBoosterInterface) => {
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
    }
  };
  return (
    <div>
      <p
        onClick={() => {
          createCharacterBoosterMutation(Number(campaignId));
        }}
        className="bg-gray-400 p-3 rounded-lg"
      >
        Open Character Booster
      </p>
      {dataFromCharacterBooster && (
        <div>
          {/* Characters */}
          {cards?.characters && cards?.characters?.length > 0 && (
            <>
              <CharacterInstanceCard {...cards.characters[0]} />
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
      )}
    </div>
  );
};
