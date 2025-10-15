import { useState } from "react";
import type { CharacterBoosterType } from "../../types/boosterTypes";
import { CharacterInstanceCard } from "../characters/CharacterInstanceCard";

export const CharacterBooster = ({ characters }: CharacterBoosterType) => {
  const [isBoosterOpen, setIsBoosterOpen] = useState<boolean>(false);
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
          {characters.map((char) => (
            <CharacterInstanceCard {...char} />
          ))}
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
