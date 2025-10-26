import type { CharacterInstanceType } from "./characterTypes";
export type BattleResponseDto = {
  id: number;
  campaignId: number;
  turns: TurnResponseDto[];
  startingTeamOne?: CharacterSnaphotType[];
  startingTeamTwo?: CharacterSnaphotType[];
  teamOne: CharacterInstanceType[];
  teamTwo: CharacterInstanceType[];
  winningTeam: CharacterInstanceType[];
  losingTeam: CharacterInstanceType[];
  currentCharacterToPlay: CharacterInstanceType;
  onGoing: boolean;
};

type CharacterSnaphotType = {
  campaignId: number;
  description: string;
  imgBase64: string;
  name: string;
  stats: CharacterSnapshotStatsType;
  userId: string;
};

type CharacterSnapshotStatsType = {
  currentHp: number;
  currentMp: number;
  maxHp: number;
  maxMp: number;
};

export type TurnResponseDto = {
  performingCharacter: CharacterInstanceType;
  targetCharacter: CharacterInstanceType;
  action: ActionResponseDto;
};

export type TurnRequest = {
  campaignId: number;
  battleId: number;
  performingCharacterId: number;
  targetCharacterId: number;
  action: ActionTypeEnum;
  cardToUseId?: number;
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
