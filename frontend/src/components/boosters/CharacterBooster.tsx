import { useEffect, useState } from "react";
import type { CharacterBoosterType } from "../../types/boosterTypes";
import { CharacterInstanceCard } from "../characters/CharacterInstanceCard";

export const CharacterBooster = ({
  characters,
  cleanCharacterBooster,
}: CharacterBoosterType) => {
  const [isBoosterOpen, setIsBoosterOpen] = useState<boolean>(false);
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

  useEffect(() => {
    setCards({
      characters: [...characters],
    });
  }, [characters]);
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
      {!isBoosterOpen && (
        <p
          onClick={() => {
            setIsBoosterOpen(true);
          }}
          className="bg-red-400 text-center p-2 m-2 rounded-xl"
        >
          Open character booster!
        </p>
      )}
      {isBoosterOpen && (
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

          {/* Close button */}
          <p
            onClick={() => {
              setIsBoosterOpen(false);
            }}
            className="bg-red-300 text-center p-2 m-2 rounded-xl"
          >
            Close
          </p>
        </div>
      )}
    </div>
  );
};
