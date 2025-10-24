import type { CharacterInstanceType } from "./characterTypes";
export type BattleResponseDto = {
  id: number;
  campaignId: number;
  turns: TurnResponseDto[];
  startingTeamOne?: null;
  startingTeamTwo?: null;
  teamOne: CharacterInstanceType[];
  teamTwo: CharacterInstanceType[];
  winningTeam: CharacterInstanceType[];
  losingTeam: CharacterInstanceType[];
  currentCharacterToPlay: CharacterInstanceType;
  onGoing: boolean;
};

export type TurnResponseDto = {
  performingCharacter: CharacterInstanceType;
  targetCharacter: CharacterInstanceType;
  action: ActionResponseDto;
};

export type ActionResponseDto = {
  damageCaused: number;
  healingCaused: number;
  actionType: ActionTypeEnum;
  stateCaused: StateTypeEnum;
};

export type ActionTypeEnum =
  | "SPELL"
  | "PHYSICAL_ATTACK"
  | "CONSUMABLE"
  | "NOTHING";

export type StateTypeEnum =
  | "NONE"
  | "NORMAL"
  | "PARALIZED"
  | "CONFUSED"
  | "FOCUSED"
  | "POISONED";
