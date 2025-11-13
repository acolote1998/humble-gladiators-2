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
import { SandClockIcon } from "../icons/errors/SandClockIcon";
import type { ArmorType } from "@/types/armorTypes";
import type { BootsType } from "@/types/bootsTypes";
import type { ConsumableType } from "@/types/consumablesTypes";
import type { HelmetType } from "@/types/helmetTypes";
import type { ShieldType } from "@/types/shieldTypes";
import type { SpellType } from "@/types/spellTypes";
import type { WeaponType } from "@/types/weaponTypes";
import ProcessCardButton from "./ProcessCardButton";
import { NewCardInformer } from "./NewCardInformer";

export const ItemsBooster = ({ campaignId }: ItemBoosterInterface) => {
  const { data: cardBack } = useGetCardBackForCampaign(Number(campaignId));
  const [flipped, setFlipped] = useState<boolean>(false);
  const [processingCard, setProcessingCard] = useState<boolean>(false);
  const handleFlip = () => {
    if (flipped) {
      setFlipped(false);
    } else {
      if (!flipped) setFlipped(true);
    }
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
  const updateRemainingCards = (
    cardsToUpdate:
      | ArmorType[]
      | BootsType[]
      | ConsumableType[]
      | HelmetType[]
      | HelmetType[]
      | ShieldType[]
      | SpellType[]
      | WeaponType[]
  ) => {
    if (!processingCard) {
      setProcessingCard(true);
      if (flipped) {
        handleFlip();
      } else {
        setFlipped(true);
        setTimeout(() => {
          setFlipped(false);
        }, 850);
      }
      setTimeout(() => {
        cardsToUpdate.shift();
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
        setProcessingCard(false);
      }, 1500);
    }
  };
  return (
    <>
      <div>
        {isBoosterAvailableLoading ? (
          <Loader />
        ) : isBoosterAvailable &&
          !dataFromItemBooster &&
          !dataFromBoosterLoading ? (
          <div
            onClick={() => {
              setTimeout(() => {
                createItemBoosterMutation(Number(campaignId));
              }, 1200);
            }}
          >
            <h3
              data-testid="open-booster-button"
              className="absolute text-4xl font-semibold text-center bottom-10 left-1/2 -translate-x-1/2"
            >
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
          !dataFromItemBooster &&
          !dataFromBoosterLoading && (
            <div className="text-lg font-semibold text-center flex items-center justify-center gap-4">
              <p>It is not possible to open an item booster right now.</p>
              <SandClockIcon width={28} />
            </div>
          )
        )}

        {dataFromBoosterLoading ? (
          <Loader />
        ) : (
          dataFromItemBooster && (
            <div data-testid="booster-data">
              {/* Armors */}
              {cards?.armors && cards?.armors?.length > 0 && (
                <>
                  <div className={`w-fit h-fit`}>
                    <div
                      className="perspective cursor-pointer  absolute left-[50%] -translate-x-[50%] bottom-45 xl:bottom-5"
                      onClick={() => {
                        if (!processingCard) handleFlip();
                      }}
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
                  {!cards.armors[0].discovered && <NewCardInformer />}
                  <ProcessCardButton
                    cardsLeft={
                      cards.armors.length +
                      cards.boots.length +
                      cards.consumables.length +
                      cards.helmets.length +
                      cards.shields.length +
                      cards.spells.length +
                      cards.weapons.length
                    }
                    buttonText={textNextOrClose()}
                    isBeingProcessed={processingCard}
                    onClickCallback={() => updateRemainingCards(cards.armors)}
                  />
                </>
              )}

              {/* Boots */}
              {cards?.armors?.length === 0 && cards?.boots?.length > 0 && (
                <>
                  <div className={`w-fit h-fit`}>
                    <div
                      className="perspective cursor-pointer  absolute left-[50%] -translate-x-[50%] bottom-45 xl:bottom-5"
                      onClick={() => {
                        if (!processingCard) handleFlip();
                      }}
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
                  {!cards.boots[0].discovered && <NewCardInformer />}
                  <ProcessCardButton
                    cardsLeft={
                      cards.armors.length +
                      cards.boots.length +
                      cards.consumables.length +
                      cards.helmets.length +
                      cards.shields.length +
                      cards.spells.length +
                      cards.weapons.length
                    }
                    buttonText={textNextOrClose()}
                    isBeingProcessed={processingCard}
                    onClickCallback={() => updateRemainingCards(cards.boots)}
                  />
                </>
              )}

              {/* Consumables */}
              {cards?.armors?.length === 0 &&
                cards?.boots?.length === 0 &&
                cards?.consumables?.length > 0 && (
                  <>
                    <div className={`w-fit h-fit`}>
                      <div
                        className="perspective cursor-pointer  absolute left-[50%] -translate-x-[50%] bottom-45 xl:bottom-5"
                        onClick={() => {
                          if (!processingCard) handleFlip();
                        }}
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
                    {!cards.consumables[0].discovered && <NewCardInformer />}
                    <ProcessCardButton
                      cardsLeft={
                        cards.armors.length +
                        cards.boots.length +
                        cards.consumables.length +
                        cards.helmets.length +
                        cards.shields.length +
                        cards.spells.length +
                        cards.weapons.length
                      }
                      buttonText={textNextOrClose()}
                      isBeingProcessed={processingCard}
                      onClickCallback={() =>
                        updateRemainingCards(cards.consumables)
                      }
                    />
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
                        className="perspective cursor-pointer  absolute left-[50%] -translate-x-[50%] bottom-45 xl:bottom-5"
                        onClick={() => {
                          if (!processingCard) handleFlip();
                        }}
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
                    {!cards.helmets[0].discovered && <NewCardInformer />}
                    <ProcessCardButton
                      cardsLeft={
                        cards.armors.length +
                        cards.boots.length +
                        cards.consumables.length +
                        cards.helmets.length +
                        cards.shields.length +
                        cards.spells.length +
                        cards.weapons.length
                      }
                      buttonText={textNextOrClose()}
                      isBeingProcessed={processingCard}
                      onClickCallback={() =>
                        updateRemainingCards(cards.helmets)
                      }
                    />
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
                        className="perspective cursor-pointer  absolute left-[50%] -translate-x-[50%] bottom-45 xl:bottom-5"
                        onClick={() => {
                          if (!processingCard) handleFlip();
                        }}
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
                    {!cards.shields[0].discovered && <NewCardInformer />}
                    <ProcessCardButton
                      cardsLeft={
                        cards.armors.length +
                        cards.boots.length +
                        cards.consumables.length +
                        cards.helmets.length +
                        cards.shields.length +
                        cards.spells.length +
                        cards.weapons.length
                      }
                      buttonText={textNextOrClose()}
                      isBeingProcessed={processingCard}
                      onClickCallback={() =>
                        updateRemainingCards(cards.shields)
                      }
                    />
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
                        className="perspective cursor-pointer  absolute left-[50%] -translate-x-[50%] bottom-45 xl:bottom-5"
                        onClick={() => {
                          if (!processingCard) handleFlip();
                        }}
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
                    {!cards.spells[0].discovered && <NewCardInformer />}
                    <ProcessCardButton
                      cardsLeft={
                        cards.armors.length +
                        cards.boots.length +
                        cards.consumables.length +
                        cards.helmets.length +
                        cards.shields.length +
                        cards.spells.length +
                        cards.weapons.length
                      }
                      buttonText={textNextOrClose()}
                      isBeingProcessed={processingCard}
                      onClickCallback={() => updateRemainingCards(cards.spells)}
                    />
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
                        className="perspective cursor-pointer  absolute left-[50%] -translate-x-[50%] bottom-45 xl:bottom-5"
                        onClick={() => {
                          if (!processingCard) handleFlip();
                        }}
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
                    {!cards.weapons[0].discovered && <NewCardInformer />}
                    <ProcessCardButton
                      cardsLeft={
                        cards.armors.length +
                        cards.boots.length +
                        cards.consumables.length +
                        cards.helmets.length +
                        cards.shields.length +
                        cards.spells.length +
                        cards.weapons.length
                      }
                      buttonText={textNextOrClose()}
                      isBeingProcessed={processingCard}
                      onClickCallback={() =>
                        updateRemainingCards(cards.weapons)
                      }
                    />
                  </>
                )}
            </div>
          )
        )}
      </div>
    </>
  );
};
