import type { BattleResponseDto } from "../../types/battleTypes";
import { CharacterCard } from "../cards/CharacterCard";
import { ConsumableCard } from "../cards/ConsumableCard";
import { PunchCard } from "../cards/PunchCard";
import { SpellCard } from "../cards/SpellCard";
import { WeaponCard } from "../cards/WeaponCard";
import { useCastActionInBattle } from "../../hooks/useBattles";
import type { ActionTypeEnum } from "../../types/battleTypes";
import { useEffect, useState } from "react";
import { useTriggerNpcTurnForTodaysBattle } from "../../hooks/useBattles";
import { toast } from "react-toastify";
import { useQueryClient } from "@tanstack/react-query";
import TurnTable from "./TurnTable";
import HeroStats from "./HeroStats";
import StatBar from "../stats/StatBar";
const BattleExecuting = ({
  campaignId,
  currentCharacterToPlay,
  id: battleId,
  losingTeam,
  onGoing,
  teamOne,
  teamTwo,
  turns,
  winningTeam,
  // startingTeamOne,
  // startingTeamTwo,
}: BattleResponseDto) => {
  const [areTurnsVisible, setAreTurnsVisible] = useState<boolean>(true);
  const [isChoosingTarget, setIsChoosingTarget] = useState<boolean>(false);
  const [chosenTargetId, setChosenTargetId] = useState<number>();
  const [chosenCardAction, setChosenCardAction] = useState<ActionTypeEnum>();
  const [chosenCardId, setChosenCardId] = useState<number>();
  const queryClient = useQueryClient();

  const toggleTurnVisibility = () => {
    setAreTurnsVisible((prev) => !prev);
  };

  const turnActionToText = (action: ActionTypeEnum) => {
    switch (action) {
      case "SPELL":
        return "a spell";
      case "PHYSICAL_ATTACK":
        return "a physical attack";
      case "CONSUMABLE":
        return "a consumable";
    }
  };

  const chooseTarget = (targetCharacterId: number) => {
    if (isChoosingTarget) {
      setChosenTargetId(targetCharacterId);
      setIsChoosingTarget(false);
    } else {
      if (currentCharacterToPlay.id == teamOne[0].id) {
        toast.warn("You need to choose a card to play first");
      } else {
        toast.error("It is the enemy's turn");
      }
    }
  };
  const { mutate: triggerActionMutation } = useCastActionInBattle();
  const {
    mutate: triggerNpcTurn,
    isSuccess: isNpcTurnTriggeredSuccessfully,
    reset: eraseNpcTurnTraces,
  } = useTriggerNpcTurnForTodaysBattle();

  useEffect(() => {
    if (chosenCardAction && chosenTargetId) {
      toast.success(
        `${teamOne[0].name} uses a ${turnActionToText(chosenCardAction)} on ${teamOne[0].id === chosenTargetId ? teamOne[0].name : teamTwo[0].id === chosenTargetId ? teamTwo[0].name : ""}!`
      );
      triggerActionMutation({
        action: chosenCardAction,
        battleId: battleId,
        campaignId: campaignId,
        performingCharacterId: teamOne[0].id,
        targetCharacterId: chosenTargetId,
        cardToUseId:
          chosenCardAction != "PHYSICAL_ATTACK" ? chosenCardId : undefined,
      });
      setTimeout(() => {
        setIsChoosingTarget(false);
        setChosenTargetId(undefined);
        setChosenCardAction(undefined);
        setChosenCardId(undefined);
        queryClient.invalidateQueries({
          queryKey: ["todays-battle", campaignId],
        });
      }, 2800);
    }
  }, [
    battleId,
    campaignId,
    chosenCardAction,
    chosenCardId,
    chosenTargetId,
    teamOne,
    teamTwo,
    triggerActionMutation,
    queryClient,
  ]);
  const [isGameStarted, setIsGameStarted] = useState<boolean>(false);

  useEffect(() => {
    if (isGameStarted || turns.length > 0) {
      if (currentCharacterToPlay.id === teamTwo[0].id)
        setTimeout(() => {
          toast.info(`${teamTwo[0].name} is thinking...`);
        }, 2500);
      setTimeout(() => {
        triggerNpcTurn(Number(campaignId));
      }, 5500);
    }
  }, [
    currentCharacterToPlay,
    isGameStarted,
    campaignId,
    teamTwo,
    triggerNpcTurn,
    turns,
  ]);

  useEffect(() => {
    if (isNpcTurnTriggeredSuccessfully) {
      setTimeout(() => {
        eraseNpcTurnTraces();
        queryClient.invalidateQueries({
          queryKey: ["todays-battle", campaignId],
        });
      }, 2500);
    }
  }, [
    isNpcTurnTriggeredSuccessfully,
    queryClient,
    campaignId,
    eraseNpcTurnTraces,
  ]);

  useEffect(() => {
    if (turns.length > 0) {
      if (turns[turns.length - 1].performingCharacter.id == teamTwo[0].id) {
        const turn = turns[turns.length - 1];
        toast.success(
          `${turn.performingCharacter.name} performed ${turnActionToText(turn.action.actionType)} on ${turn.targetCharacter.name}`
        );
      }
    }
  }, [turns, teamTwo]);

  const isHeroEquippingWeapon = (): boolean => {
    let doesItHaveEquippedWeapon = false;
    teamOne[0].inventory.weapons.forEach((w) => {
      if (w.equipped) doesItHaveEquippedWeapon = true;
    });
    return doesItHaveEquippedWeapon;
  };

  const getUniqueSpells = () => {
    return teamOne[0].inventory.spells.filter((spell, index, self) => {
      return (
        index ===
        self.findIndex(
          (firstOccurrenceSpell) => firstOccurrenceSpell.name === spell.name
        )
      );
    });
  };

  const getUniqueConsumables = () => {
    return teamOne[0].inventory.consumables.filter(
      (consumable, index, self) => {
        return (
          index ===
          self.findIndex(
            (firstOccurrenceConsumable) =>
              firstOccurrenceConsumable.name === consumable.name
          )
        );
      }
    );
  };

  return (
    <div className="relative">
      {teamTwo[0].backgroundImgBase64 && (
        <img
          draggable={false}
          src={`data:image/jpeg;base64,${teamTwo[0].backgroundImgBase64}`}
          className="absolute scale-y-110 xl:scale-y-100"
        />
      )}
      {winningTeam.length < 1 || losingTeam.length < 1 || onGoing ? (
        <>
          {
            // If no one has played, and it is the enemy's turn (enemy is always team two)
            // then start the battle (it will reload the page which triggers the enemy's
            // turn if it is their turn.
            // otherwise, if it is the hero's turn (always team one), then we can just
            // play a card to start the battle)
          }
          {!isGameStarted && turns.length < 1 && (
            <div
              className="absolute w-full text-center z-5 translate-y-10 xl:translate-y-70"
              data-testid="start-game-label"
            >
              {currentCharacterToPlay.id == teamTwo[0].id ? (
                <p
                  data-testid="start-battle-trigger"
                  className="text-sm xl:text-lg text-center bg-[var(--action-positive-bg)] text-[var(--action-positive-foreground)]"
                  onClick={() => {
                    setIsGameStarted(true);
                  }}
                >
                  Start Battle
                </p>
              ) : (
                !isGameStarted &&
                turns.length < 1 && (
                  <p className="text-sm xl:text-lg text-center bg-[var(--action-positive-bg)] text-[var(--action-positive-foreground)]">
                    You start, cast one of your cards by clicking on it
                  </p>
                )
              )}
            </div>
          )}
          <div className="relative flex justify-center">
            <p
              className={`absolute left-1 xl:left-5 top-1 xl:top-5 bg-[var(--page-container-bg-darker)] rounded-xl p-1 text-sm xl:text-2xl transition-all ease-in-out duration-500
                ${areTurnsVisible ? "opacity-100" : "opacity-20"}
                `}
            >
              {currentCharacterToPlay.name === teamOne[0].name
                ? "Your turn"
                : "Enemy's turn"}
            </p>
          </div>
          <div className="relative xl:h-154">
            <div className="flex flex-col items-center xl:py-3.5">
              <StatBar
                barHeight={1.25}
                currentValue={teamTwo[0].stats.currentHp}
                maxValue={teamTwo[0].stats.maxHp}
                type="HP"
                widthPercent={30}
              />
              <StatBar
                barHeight={1.25}
                currentValue={teamTwo[0].stats.currentMp}
                maxValue={teamTwo[0].stats.maxMp}
                type="MANA"
                widthPercent={30}
              />
              <div
                className="order-1 xl:order-3"
                onClick={() => chooseTarget(teamTwo[0].id)}
              >
                <CharacterCard {...teamTwo[0]} renderingFrom="BATTLE" />
              </div>
            </div>
            <TurnTable
              turns={turns}
              isOpen={areTurnsVisible}
              toggleVisibility={toggleTurnVisibility}
            />
          </div>
          <div className="flex flex-col xl:grid xl:grid-cols-7">
            <div
              onClick={() => {
                chooseTarget(teamOne[0].id);
              }}
            >
              <HeroStats character={teamOne[0]} />
            </div>
            <div className="col-span-6 flex flex-col items-center border-[var(--page-container-border)] rounded-md bg-[var(--page-container-bg-darker)] relative">
              <p className="relative  rounded-xl p-1 text-2xl">Hand</p>
              <div className={`grid grid-cols-5`}>
                <div
                  className={
                    `scale-85 xl:hover:scale-100 transition-all ease-in-out duration-500 xl:hover:-translate-y-62 xl:hover:z-50 ` +
                    `${chosenCardAction == "PHYSICAL_ATTACK" ? " -translate-y-10 xl:-translate-y-15" : "translate-y-0"}`
                  }
                  onClick={() => {
                    if (currentCharacterToPlay.id == teamOne[0].id) {
                      //It is the hero's turn, so they can attack
                      setIsChoosingTarget(true);
                      setChosenCardAction("PHYSICAL_ATTACK");
                      setChosenCardId(undefined);
                      toast.info("Physical Attack selected");
                    } else {
                      toast.warn("It is the enemy's turn");
                    }
                  }}
                >
                  {isHeroEquippingWeapon() ? (
                    teamOne[0].inventory.weapons.map((card) => {
                      if (card.equipped) {
                        return <WeaponCard {...card} renderingFrom="BATTLE" />;
                      }
                    })
                  ) : (
                    <PunchCard />
                  )}
                </div>
                {getUniqueSpells().map((card) => {
                  return (
                    <div
                      className={
                        `scale-85 xl:hover:scale-100 transition-all ease-in-out duration-500 xl:hover:-translate-y-62 xl:hover:z-50  ` +
                        `${teamOne[0].stats.currentMp >= card.mpCost ? "opacity-100 " : "opacity-50 "}` +
                        `${chosenCardAction == "SPELL" && card.id == chosenCardId ? " -translate-y-10 xl:-translate-y-15" : "translate-y-0"}`
                      }
                      onClick={() => {
                        if (
                          currentCharacterToPlay.id == teamOne[0].id &&
                          teamOne[0].stats.currentMp >= card.mpCost
                        ) {
                          //It is the hero's turn, so they can use a spell, and they have enough MP for the card
                          setIsChoosingTarget(true);
                          setChosenCardAction("SPELL");
                          setChosenCardId(card.id);
                          toast.info("Spell selected");
                        } else if (currentCharacterToPlay.id != teamOne[0].id) {
                          toast.warn("It is the enemy's turn");
                        } else if (teamOne[0].stats.currentMp < card.mpCost) {
                          toast.warn("Not enough MP");
                        }
                      }}
                    >
                      <SpellCard {...card} renderingFrom="BATTLE" />
                    </div>
                  );
                })}
                {getUniqueConsumables().map((card) => {
                  return (
                    <div
                      className={
                        `scale-85 xl:hover:scale-100 transition-all ease-in-out duration-500 xl:hover:-translate-y-62 xl:hover:z-50  ` +
                        `${chosenCardAction == "CONSUMABLE" && card.id == chosenCardId ? "-translate-y-10 xl:-translate-y-15" : "translate-y-0"}`
                      }
                      onClick={() => {
                        if (currentCharacterToPlay.id == teamOne[0].id) {
                          //It is the hero's turn, so they can use a consumable
                          setIsChoosingTarget(true);
                          setChosenCardAction("CONSUMABLE");
                          setChosenCardId(card.id);
                          toast.info("Consumable selected");
                        } else {
                          toast.warn("It is the enemy's turn");
                        }
                      }}
                    >
                      <ConsumableCard {...card} renderingFrom="BATTLE" />
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </>
      ) : (
        <div>
          <p>This battle is already finshed</p>
          <p>Winner: {winningTeam[0].name}</p>
          <p>Loser: {losingTeam[0].name}</p>
        </div>
      )}
    </div>
  );
};

export default BattleExecuting;
