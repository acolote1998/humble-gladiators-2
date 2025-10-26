import type {
  BattleResponseDto,
  TurnResponseDto,
} from "../../types/battleTypes";
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
  startingTeamOne,
  startingTeamTwo,
  refetchBattle,
}: BattleResponseDto) => {
  const [messageOfWhatsHappening, setMessageOfWhatsHappening] =
    useState<string>("");
  const [isChoosingTarget, setIsChoosingTarget] = useState<boolean>(false);
  const [chosenTargetId, setChosenTargetId] = useState<number>();
  const [chosenCardAction, setChosenCardAction] = useState<ActionTypeEnum>();
  const [chosenCardId, setChosenCardId] = useState<number>();

  const turnActionToText = (turn: TurnResponseDto) => {
    switch (turn.action.actionType) {
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
      setMessageOfWhatsHappening("Target selected");
      setTimeout(() => {
        setChosenTargetId(targetCharacterId);
        setIsChoosingTarget(false);
        setMessageOfWhatsHappening("");
      }, 1500);
    } else {
      setMessageOfWhatsHappening("You need to choose a card to play first");
      setTimeout(() => {
        setMessageOfWhatsHappening("");
      }, 1500);
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
      setMessageOfWhatsHappening(
        `${teamOne[0].name} uses a ${chosenCardAction} on ${teamOne[0].id === chosenTargetId ? teamOne[0].name : teamTwo[0].id === chosenTargetId ? teamTwo[0].name : ""}!`
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
        setMessageOfWhatsHappening("");
        setIsChoosingTarget(false);
        setChosenTargetId(undefined);
        setChosenCardAction(undefined);
        setChosenCardId(undefined);
        if (refetchBattle) refetchBattle();
      }, 2800);
    }
  }, [
    battleId,
    campaignId,
    chosenCardAction,
    chosenCardId,
    chosenTargetId,
    teamOne,
    triggerActionMutation,
    refetchBattle,
    teamTwo,
    triggerNpcTurn,
  ]);
  const [isGameStarted, setIsGameStarted] = useState<boolean>(false);

  useEffect(() => {
    if (isGameStarted || turns.length > 0) {
      if (currentCharacterToPlay.id === teamTwo[0].id)
        setTimeout(() => {
          setMessageOfWhatsHappening(`${teamTwo[0].name} is thinking his turn`);
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
        console.log("hola?");
        eraseNpcTurnTraces();
        setMessageOfWhatsHappening("");
        if (refetchBattle) refetchBattle();
      }, 2500);
    }
  }, [isNpcTurnTriggeredSuccessfully, refetchBattle, eraseNpcTurnTraces]);

  const isHeroEquippingWeapon = (): boolean => {
    let doesItHaveEquippedWeapon = false;
    teamOne[0].inventory.weapons.forEach((w) => {
      if (w.equipped) doesItHaveEquippedWeapon = true;
    });
    return doesItHaveEquippedWeapon;
  };
  return (
    <div>
      {messageOfWhatsHappening.length > 0 && (
        <p className="text-lg text-center bg-yellow-300">
          {messageOfWhatsHappening}
        </p>
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
          {turns.length < 1 && currentCharacterToPlay.id == teamTwo[0].id ? (
            <p
              className="text-lg text-center bg-green-300"
              onClick={() => {
                setIsGameStarted(true);
              }}
            >
              Start Battle
            </p>
          ) : (
            turns.length < 1 && (
              <p className="text-lg text-center bg-green-300">
                You start, cast one of your cards by clicking on it
              </p>
            )
          )}
          <div className="flex flex-col items-center">
            <p className="text-2xl">
              Character to play: {currentCharacterToPlay.name}
            </p>
          </div>
          <div className="relative">
            <div className="flex flex-col items-center">
              <p className="text-2xl">Enemy</p>
              <div onClick={() => chooseTarget(teamTwo[0].id)}>
                <CharacterCard {...teamTwo[0]} renderingFrom="BATTLE" />
              </div>
            </div>
            <div className="absolute rounded-md bg-gray-200 border-gray-400 border-1 mx-5 top-20 px-5 right-0 w-150 h-110 overflow-y-scroll">
              {turns
                .slice()
                .reverse()
                .map((turn, index) => (
                  <p
                    key={turns.length - index}
                    className="my-2 p-1 rounded-md bg-gray-300"
                  >
                    Turn {turns.length - index} -{" "}
                    {turn.performingCharacter.name} performed{" "}
                    {turnActionToText(turn)} on {turn.targetCharacter.name}
                    {turn.action.damageCaused > 0 &&
                      ` and caused ${turn.action.damageCaused} damage`}
                    {turn.action.healingCaused > 0 &&
                      ` and healed ${turn.action.healingCaused} heal points`}
                  </p>
                ))}
            </div>
          </div>
          <div className="grid grid-cols-7">
            <div
              className="flex flex-col items-center"
              onClick={() => {
                chooseTarget(teamOne[0].id);
              }}
            >
              <p className="text-2xl">Hero Stats</p>
              <p className="text-xl">{teamOne[0].name}</p>
              <p>
                HP {teamOne[0].stats.currentHp}/{teamOne[0].stats.maxHp}
              </p>
              <p>
                MP {teamOne[0].stats.currentMp}/{teamOne[0].stats.maxMp}
              </p>
              <p>
                XP {teamOne[0].stats.currentExp}/
                {teamOne[0].stats.expForNextLevel}
              </p>
              <p>LCK {teamOne[0].stats.luck}</p>
              <p>SPD {teamOne[0].stats.speed}</p>
              <p>
                P. DMG {teamOne[0].stats.physicalDamage} / P. DEF{" "}
                {teamOne[0].stats.physicalDefense}
              </p>
              <p>
                M. DMG {teamOne[0].stats.magicalDamage} / M. DEF{" "}
                {teamOne[0].stats.magicalDefense}
              </p>
            </div>
            <div className="col-span-6 flex flex-col items-center">
              <p className="text-2xl">Hand</p>
              <div className="grid grid-cols-5">
                <div
                  className={
                    `transition-all ease-in-out duration-500 hover:-translate-y-62 hover:z-50 ` +
                    `${chosenCardAction == "PHYSICAL_ATTACK" ? " -translate-y-15" : "translate-y-0"}`
                  }
                  onClick={() => {
                    if (currentCharacterToPlay.id == teamOne[0].id) {
                      //It is the hero's turn, so they can attack
                      setIsChoosingTarget(true);
                      setChosenCardAction("PHYSICAL_ATTACK");
                      setChosenCardId(undefined);
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
                {teamOne[0].inventory.spells
                  .filter((spell, index, self) => {
                    return (
                      index ===
                      self.findIndex(
                        (firstOcurrenceSpell) =>
                          firstOcurrenceSpell.name == spell.name
                      )
                    );
                  })
                  .map((card) => {
                    return (
                      <div
                        className={
                          `transition-all ease-in-out duration-500 hover:-translate-y-62 hover:z-50  ` +
                          `${teamOne[0].stats.currentMp >= card.mpCost ? "opacity-100 " : "opacity-50 "}` +
                          `${chosenCardAction == "SPELL" && card.id == chosenCardId ? " -translate-y-15" : "translate-y-0"}`
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
                          } else if (teamOne[0].stats.currentMp < card.mpCost) {
                            setMessageOfWhatsHappening("Not enough mp");
                            setTimeout(() => {
                              setMessageOfWhatsHappening("");
                            }, 1500);
                          }
                        }}
                      >
                        <SpellCard {...card} renderingFrom="BATTLE" />
                      </div>
                    );
                  })}
                {teamOne[0].inventory.consumables.map((card) => {
                  return (
                    <div
                      className={
                        `transition-all ease-in-out duration-500 hover:-translate-y-62 hover:z-50  ` +
                        `${chosenCardAction == "CONSUMABLE" && card.id == chosenCardId ? "-translate-y-15" : "translate-y-0"}`
                      }
                      onClick={() => {
                        if (currentCharacterToPlay.id == teamOne[0].id) {
                          //It is the hero's turn, so they can use a consumable
                          setIsChoosingTarget(true);
                          setChosenCardAction("CONSUMABLE");
                          setChosenCardId(card.id);
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
