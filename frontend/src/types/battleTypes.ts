import type { ArmorType } from "./armorTypes";
import type { BootsType } from "./bootsTypes";
import type { CharacterInstanceType } from "./characterTypes";
import type { ConsumableType } from "./consumablesTypes";
import type { HelmetType } from "./helmetTypes";
import type { ShieldType } from "./shieldTypes";
import type { SpellType } from "./spellTypes";
import type { WeaponType } from "./weaponTypes";
export type BattleResponseDto = {
  id: number;
  campaignId: number;
  turns: TurnResponseDto[];
  startingTeamOne: CharacterSnaphotType[];
  startingTeamTwo: CharacterSnaphotType[];
  teamOne: CharacterInstanceType[];
  teamTwo: CharacterInstanceType[];
  winningTeam: CharacterInstanceType[];
  losingTeam: CharacterInstanceType[];
  currentCharacterToPlay: CharacterInstanceType;
  onGoing: boolean;
};

export type BattleRewardsResponseDto = {
  expReward: number;
  goldReward: number;
  armorLoot: ArmorType[];
  bootsLoot: BootsType[];
  consumablesLoot: ConsumableType[];
  helmetsLoot: HelmetType[];
  shieldsLoot: ShieldType[];
  spellsLoot: SpellType[];
  weaponsLoot: WeaponType[];
  battleResult: BattleResultEnumDto;
};

type BattleResultEnumDto = "NONE" | "VICTORY_TEAM_ONE" | "VICTORY_TEAM_TWO";

type CharacterSnaphotType = {
  campaignId: number;
  description: string;
  imgBase64: string;
  name: string;
  stats: CharacterSnapshotStatsType;
  userId: string;
};

type CharacterSnapshotStatsType = {
  constitution: number;
  intelligence: number;
  strength: number;
  speed: number;
  luck: number;
  maxHp: number;
  currentHp: number;
  maxMp: number;
  currentMp: number;
  height: number;
  weight: number;
  level: number;
  currentExp: number;
  expForNextLevel: number;
  physicalDefense: number;
  magicalDefense: number;
  physicalDamage: number;
  magicalDamage: number;
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
