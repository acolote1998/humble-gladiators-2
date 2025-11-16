import { useEffect, useState } from "react";
import { CardIcon } from "../icons/typeofcards/CardIcon";

type ProcessCardButtonProps = {
  onClickCallback: () => void;
  buttonText: string;
  isBeingProcessed: boolean;
  cardsLeft: number;
};

const ProcessCardButton = ({
  buttonText,
  onClickCallback,
  isBeingProcessed,
  cardsLeft,
}: ProcessCardButtonProps) => {
  const [cardsToMap, setCardsToMap] = useState<string[]>([]);
  useEffect(() => {
    const array: string[] = [];
    for (let index = 0; index < cardsLeft - 1; index++) {
      array.push("");
    }
    setCardsToMap(array);
  }, [cardsLeft]);
  return (
    <div className="flex items-center justify-center">
      <div
        onClick={() => {
          onClickCallback();
        }}
        className={`${isBeingProcessed ? "opacity-30 cursor-progress" : "opacity-100 cursor-pointer"} bg-[var(--information-color)] text-[var(--light-text)]
        p-2 m-2 md:ml-120 lg:ml-[60%] rounded-xl select-none transition-all duration-300 flex w-40 py-5 text-lg gap-2 font-semibold justify-center`}
      >
        <p>{buttonText}</p>
        {cardsToMap?.map((_, i) => {
          return <CardIcon key={i} width={24} />;
        })}
      </div>
    </div>
  );
};

export default ProcessCardButton;
