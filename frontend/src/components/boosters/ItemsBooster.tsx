import { useEffect, useState } from "react";
import type { ItemBoosterType } from "../../types/boosterTypes";
import { ArmorTemplateCard } from "../characters/ArmorTemplateCard";
import { BootsTemplateCard } from "../characters/BootsTemplateCard";
import { ConsumableTemplateCard } from "../characters/ConsumableTemplateCard";
import { HelmetTemplateCard } from "../characters/HelmetTemplateCard";
import { ShieldTemplateCard } from "../characters/ShieldTemplateCard";
import { SpellTemplateCard } from "../characters/SpellTemplateCard";
import { WeaponTemplateCard } from "../characters/WeaponTemplateCard";

export const ItemsBooster = ({
  armors,
  boots,
  consumables,
  helmets,
  shields,
  spells,
  weapons,
  cleanItemBooster,
}: ItemBoosterType) => {
  const [isBoosterOpen, setIsBoosterOpen] = useState<boolean>(false);
  const [cards, setCards] = useState<ItemBoosterType>();
  const checkIfBoosterIsEmpty = () => {
    if (
      cards?.armors.length === 0 &&
      cards?.boots.length === 0 &&
      cards?.consumables.length === 0 &&
      cards.helmets.length === 0 &&
      cards.shields.length === 0 &&
      cards.spells.length === 0 &&
      cards.weapons.length === 0
    ) {
      if (cleanItemBooster) cleanItemBooster();
    }
  };

  useEffect(() => {
    setCards({
      armors: [...armors],
      boots: [...boots],
      consumables: [...consumables],
      helmets: [...helmets],
      shields: [...shields],
      spells: [...spells],
      weapons: [...weapons],
    });
  }, [armors, boots, consumables, helmets, shields, spells, weapons]);
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
    checkIfBoosterIsEmpty();
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
          Open item booster!
        </p>
      )}
      {isBoosterOpen && (
        <div>
          {/* Armors */}
          {cards?.armors && cards?.armors?.length > 0 && (
            <>
              <ArmorTemplateCard {...cards.armors[0]} />
              <p
                onClick={() => {
                  cards.armors.shift();
                  updateRemainingCards();
                }}
                className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
              >
                Next
              </p>
            </>
          )}

          {/* Boots */}
          {cards?.armors?.length === 0 && cards?.boots?.length > 0 && (
            <>
              <BootsTemplateCard {...cards.boots[0]} />
              <p
                onClick={() => {
                  cards.boots.shift();
                  updateRemainingCards();
                }}
                className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
              >
                Next
              </p>
            </>
          )}

          {/* Consumables */}
          {cards?.armors?.length === 0 &&
            cards?.boots?.length === 0 &&
            cards?.consumables?.length > 0 && (
              <>
                <ConsumableTemplateCard {...cards.consumables[0]} />
                <p
                  onClick={() => {
                    cards.consumables.shift();
                    updateRemainingCards();
                  }}
                  className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                >
                  Next
                </p>
              </>
            )}

          {/* Helmets */}
          {cards?.armors?.length === 0 &&
            cards?.boots?.length === 0 &&
            cards?.consumables?.length === 0 &&
            cards?.helmets?.length > 0 && (
              <>
                <HelmetTemplateCard {...cards.helmets[0]} />
                <p
                  onClick={() => {
                    cards.helmets.shift();
                    updateRemainingCards();
                  }}
                  className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                >
                  Next
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
                <ShieldTemplateCard {...cards.shields[0]} />
                <p
                  onClick={() => {
                    cards.shields.shift();
                    updateRemainingCards();
                  }}
                  className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                >
                  Next
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
                <SpellTemplateCard {...cards.spells[0]} />
                <p
                  onClick={() => {
                    cards.spells.shift();
                    updateRemainingCards();
                  }}
                  className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                >
                  Next
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
                <WeaponTemplateCard {...cards.weapons[0]} />
                <p
                  onClick={() => {
                    cards.weapons.shift();
                    updateRemainingCards();
                  }}
                  className="cursor-pointer bg-blue-200 text-center p-2 m-2 rounded-xl"
                >
                  Next
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
