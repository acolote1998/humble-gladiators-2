import { useEffect, useState } from "react";
import type { ItemBoosterType } from "../../types/boosterTypes";
import { ArmorCard } from "../cards/ArmorCard";
import { BootsCard } from "../cards/BootsCard";
import { ConsumableCard } from "../cards/ConsumableCard";
import { HelmetCard } from "../cards/HelmetCard";
import { ShieldCard } from "../cards/ShieldCard";
import { SpellCard } from "../cards/SpellCard";
import { WeaponCard } from "../cards/WeaponCard";
import { useCreateItemBooster } from "../../hooks/useBoosters";
import type { ItemBoosterInterface } from "../../types/boosterTypes";
import { useGetItemBoosterAvailability } from "../../hooks/useBoosters";
import { useQueryClient } from "@tanstack/react-query";
import { CardBack } from "../cards/CardBack";
import { useGetCardBackForCampaign } from "../../hooks/useCampaigns";
import { Loader } from "../Loader";
import BoosterToOpenPlaceholder from "./BoosterToOpenPlaceholder";

export const ItemsBooster = ({ campaignId }: ItemBoosterInterface) => {
  const { data: cardBack } = useGetCardBackForCampaign(Number(campaignId));
  const [flipped, setFlipped] = useState(false);
  const handleFlip = () => {
    if (!flipped) setFlipped(true);
  };
  const queryClient = useQueryClient();
  const { data: isBoosterAvailable, isLoading: isBoosterAvailableLoading } =
    useGetItemBoosterAvailability(Number(campaignId));
  const {
    mutate: createItemBoosterMutation,
    data: dataFromItemBooster,
    isPending: dataFromBoosterLoading,
    reset: cleanItemBooster,
  } = useCreateItemBooster();
  const [cards, setCards] = useState<ItemBoosterType>();
  const isLastCard = () => {
    if (cards)
      return (
        cards?.armors?.length +
          cards?.boots?.length +
          cards?.consumables?.length +
          cards?.helmets?.length +
          cards?.shields?.length +
          cards?.spells?.length +
          cards?.weapons?.length ===
        1
      );
  };
  const isBoosterEmpty = () => {
    return (
      cards?.armors.length === 0 &&
      cards?.boots.length === 0 &&
      cards?.consumables.length === 0 &&
      cards.helmets.length === 0 &&
      cards.shields.length === 0 &&
      cards.spells.length === 0 &&
      cards.weapons.length === 0
    );
  };

  const textNextOrClose = () => {
    if (isLastCard()) {
      return "Close booster";
    } else return "Next card";
  };

  useEffect(() => {
    if (dataFromItemBooster)
      setCards({
        armors: [...dataFromItemBooster.armors],
        boots: [...dataFromItemBooster.boots],
        consumables: [...dataFromItemBooster.consumables],
        helmets: [...dataFromItemBooster.helmets],
        shields: [...dataFromItemBooster.shields],
        spells: [...dataFromItemBooster.spells],
        weapons: [...dataFromItemBooster.weapons],
      });
  }, [dataFromItemBooster]);
  const updateRemainingCards = () => {
    if (cards)
      setCards({
        armors: [...cards.armors],
        boots: [...cards.boots],
        consumables: [...cards.consumables],
        helmets: [...cards.helmets],
        shields: [...cards.shields],
        spells: [...cards.spells],
        weapons: [...cards.weapons],
      });
    if (isBoosterEmpty() && cleanItemBooster) {
      cleanItemBooster();
      queryClient.invalidateQueries({
        queryKey: ["items-booster-availability"],
      });
    }
    setFlipped(false);
  };
  return (
    <>
      <div>
        {isBoosterAvailableLoading ? (
          <Loader />
        ) : isBoosterAvailable && !dataFromItemBooster ? (
          <div
            onClick={() => {
              setTimeout(() => {
                createItemBoosterMutation(Number(campaignId));
              }, 1200);
            }}
          >
            <h3 className="absolute text-4xl font-semibold text-center bottom-20 left-[46vw]">
              Open Booster
            </h3>
            {cardBack && (
              <BoosterToOpenPlaceholder
                typeOfBooster="ITEM"
                cardBackImage={cardBack.cardBackImgBase64}
              />
            )}
          </div>
        ) : (
          !dataFromItemBooster && (
            <div className="text-lg font-semibold text-center flex items-center justify-center">
              It is not possible to open an item booster right now.
            </div>
          )
        )}

        {dataFromBoosterLoading ? (
          <Loader />
        ) : (
          dataFromItemBooster && (
            <div>
              {/* Armors */}
              {cards?.armors && cards?.armors?.length > 0 && (
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
                              cardBackImgBase64={cardBack.cardBackImgBase64}
                            />
                          </div>
                        )}
                        {cardBack && (
                          <ArmorCard
                            {...cards.armors[0]}
                            renderingFrom="BOOSTER"
                          />
                        )}
                      </div>
                    </div>
                  </div>
                  {!cards.armors[0].discovered && (
                    <p className="bg-amber-300">NEW!</p>
                  )}
                  <p
                    onClick={() => {
                      cards.armors.shift();
                      updateRemainingCards();
                    }}
                    className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                  >
                    {textNextOrClose()}
                  </p>
                </>
              )}

              {/* Boots */}
              {cards?.armors?.length === 0 && cards?.boots?.length > 0 && (
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
                              cardBackImgBase64={cardBack.cardBackImgBase64}
                            />
                          </div>
                        )}
                        {cardBack && (
                          <BootsCard
                            {...cards.boots[0]}
                            renderingFrom="BOOSTER"
                          />
                        )}
                      </div>
                    </div>
                  </div>
                  {!cards.boots[0].discovered && (
                    <p className="bg-amber-300">NEW!</p>
                  )}
                  <p
                    onClick={() => {
                      cards.boots.shift();
                      updateRemainingCards();
                    }}
                    className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                  >
                    {textNextOrClose()}
                  </p>
                </>
              )}

              {/* Consumables */}
              {cards?.armors?.length === 0 &&
                cards?.boots?.length === 0 &&
                cards?.consumables?.length > 0 && (
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
                                cardBackImgBase64={cardBack.cardBackImgBase64}
                              />
                            </div>
                          )}
                          {cardBack && (
                            <ConsumableCard
                              {...cards.consumables[0]}
                              renderingFrom="BOOSTER"
                            />
                          )}
                        </div>
                      </div>
                    </div>
                    {!cards.consumables[0].discovered && (
                      <p className="bg-amber-300">NEW!</p>
                    )}
                    <p
                      onClick={() => {
                        cards.consumables.shift();
                        updateRemainingCards();
                      }}
                      className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                    >
                      {textNextOrClose()}
                    </p>
                  </>
                )}

              {/* Helmets */}
              {cards?.armors?.length === 0 &&
                cards?.boots?.length === 0 &&
                cards?.consumables?.length === 0 &&
                cards?.helmets?.length > 0 && (
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
                                cardBackImgBase64={cardBack.cardBackImgBase64}
                              />
                            </div>
                          )}
                          {cardBack && (
                            <HelmetCard
                              {...cards.helmets[0]}
                              renderingFrom="BOOSTER"
                            />
                          )}
                        </div>
                      </div>
                    </div>
                    {!cards.helmets[0].discovered && (
                      <p className="bg-amber-300">NEW!</p>
                    )}
                    <p
                      onClick={() => {
                        cards.helmets.shift();
                        updateRemainingCards();
                      }}
                      className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                    >
                      {textNextOrClose()}
                    </p>
                  </>
                )}

              {/* Shields */}
              {cards?.armors?.length === 0 &&
                cards?.boots?.length === 0 &&
                cards?.consumables?.length === 0 &&
                cards?.helmets?.length === 0 &&
                cards?.shields?.length > 0 && (
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
                                cardBackImgBase64={cardBack.cardBackImgBase64}
                              />
                            </div>
                          )}
                          {cardBack && (
                            <ShieldCard
                              {...cards.shields[0]}
                              renderingFrom="BOOSTER"
                            />
                          )}
                        </div>
                      </div>
                    </div>
                    {!cards.shields[0].discovered && (
                      <p className="bg-amber-300">NEW!</p>
                    )}
                    <p
                      onClick={() => {
                        cards.shields.shift();
                        updateRemainingCards();
                      }}
                      className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                    >
                      {textNextOrClose()}
                    </p>
                  </>
                )}

              {/* Spells */}
              {cards?.armors?.length === 0 &&
                cards?.boots?.length === 0 &&
                cards?.consumables?.length === 0 &&
                cards?.helmets?.length === 0 &&
                cards?.shields?.length === 0 &&
                cards?.spells?.length > 0 && (
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
                                cardBackImgBase64={cardBack.cardBackImgBase64}
                              />
                            </div>
                          )}
                          {cardBack && (
                            <SpellCard
                              {...cards.spells[0]}
                              renderingFrom="BOOSTER"
                            />
                          )}
                        </div>
                      </div>
                    </div>
                    {!cards.spells[0].discovered && (
                      <p className="bg-amber-300">NEW!</p>
                    )}
                    <p
                      onClick={() => {
                        cards.spells.shift();
                        updateRemainingCards();
                      }}
                      className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                    >
                      {textNextOrClose()}
                    </p>
                  </>
                )}

              {/* Weapons */}
              {cards?.armors?.length === 0 &&
                cards?.boots?.length === 0 &&
                cards?.consumables?.length === 0 &&
                cards?.helmets?.length === 0 &&
                cards?.shields?.length === 0 &&
                cards?.spells?.length === 0 &&
                cards?.weapons?.length > 0 && (
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
                                cardBackImgBase64={cardBack.cardBackImgBase64}
                              />
                            </div>
                          )}
                          {cardBack && (
                            <WeaponCard
                              {...cards.weapons[0]}
                              renderingFrom="BOOSTER"
                            />
                          )}
                        </div>
                      </div>
                    </div>
                    {!cards.weapons[0].discovered && (
                      <p className="bg-amber-300">NEW!</p>
                    )}
                    <p
                      onClick={() => {
                        cards.weapons.shift();
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
