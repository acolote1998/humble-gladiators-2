import { useEffect, useState } from "react";
import type { ItemBoosterType } from "../../types/boosterTypes";
import { ArmorTemplateCard } from "../characters/ArmorTemplateCard";
import { BootsTemplateCard } from "../characters/BootsTemplateCard";
import { ConsumableTemplateCard } from "../characters/ConsumableTemplateCard";
import { HelmetTemplateCard } from "../characters/HelmetTemplateCard";
import { ShieldTemplateCard } from "../characters/ShieldTemplateCard";
import { SpellTemplateCard } from "../characters/SpellTemplateCard";
import { WeaponTemplateCard } from "../characters/WeaponTemplateCard";
import { useCreateItemBooster } from "../../hooks/useBoosters";
import type { ItemBoosterInterface } from "../../types/boosterTypes";
import { useGetItemBoosterAvailability } from "../../hooks/useBoosters";
import { useQueryClient } from "@tanstack/react-query";

export const ItemsBooster = ({ campaignId }: ItemBoosterInterface) => {
  const queryClient = useQueryClient();
  const {
    data: isBoosterAvailable,
    isLoading: isBoosterAvailableLoading,
    isError: isBoosterAvailableError,
  } = useGetItemBoosterAvailability(Number(campaignId));
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
  };
  return (
    <>
      <div>
        {isBoosterAvailableLoading ? (
          <p>Loading booster availability</p>
        ) : isBoosterAvailableError ? (
          <p>Error loading booster availability</p>
        ) : isBoosterAvailable && !dataFromItemBooster ? (
          <p
            onClick={() => {
              createItemBoosterMutation(Number(campaignId));
            }}
            className="bg-gray-400 p-3 rounded-lg"
          >
            Open Item Booster
          </p>
        ) : (
          <p className="bg-gray-500 p-3 rounded-lg cursor-not-allowed">
            Come back tomorrow for a new item booster!
          </p>
        )}

        {dataFromBoosterLoading ? (
          <div className="flex gap-5">
            <p>Opening booster</p>
            <p className="loader"></p>
          </div>
        ) : (
          dataFromItemBooster && (
            <div>
              {/* Armors */}
              {cards?.armors && cards?.armors?.length > 0 && (
                <>
                  <div
                    className={`${cards.armors[0].tier === 5 && cards.armors[0].rarity === 5 ? "armor-tier-5-rarity-5" : `armor-tier-${cards.armors[0].tier}`} w-fit h-fit`}
                  >
                    <ArmorTemplateCard
                      {...cards.armors[0]}
                      renderingFromBooster={true}
                    />
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
                  <div
                    className={`${cards.boots[0].tier === 5 && cards.boots[0].rarity === 5 ? "boot-tier-5-rarity-5" : `boot-tier-${cards.boots[0].tier}`} w-fit h-fit`}
                  >
                    <BootsTemplateCard
                      {...cards.boots[0]}
                      renderingFromBooster={true}
                    />
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
                    <div
                      className={`${cards.consumables[0].tier === 5 && cards.consumables[0].rarity === 5 ? "consumable-tier-5-rarity-5" : `consumable-tier-${cards.consumables[0].tier}`} w-fit h-fit`}
                    >
                      <ConsumableTemplateCard
                        {...cards.consumables[0]}
                        renderingFromBooster={true}
                      />
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
                    <div
                      className={`${cards.helmets[0].tier === 5 && cards.helmets[0].rarity === 5 ? "helmet-tier-5-rarity-5" : `helmet-tier-${cards.helmets[0].tier}`} w-fit h-fit`}
                    >
                      <HelmetTemplateCard
                        {...cards.helmets[0]}
                        renderingFromBooster={true}
                      />
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
                    <ShieldTemplateCard
                      {...cards.shields[0]}
                      renderingFromBooster={true}
                    />
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
                    <SpellTemplateCard
                      {...cards.spells[0]}
                      renderingFromBooster={true}
                    />
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
                    <WeaponTemplateCard
                      {...cards.weapons[0]}
                      renderingFromBooster={true}
                    />
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
