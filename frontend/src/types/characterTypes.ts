import type { ArmorCategoriesEnum } from "./armorTypes";
import type { BootsCategoriesEnum } from "./bootsTypes";
import type { HelmetCategoriesEnum } from "./helmetTypes";
import type { ShieldCategoriesEnum } from "./shieldTypes";
import type { ConsumablesCategoriesEnum } from "./consumablesTypes";
import type { SpellCategoriesEnum } from "./spellTypes";
import type { WeaponCategoriesEnum } from "./weaponTypes";

export type CharacterInstanceType = {
  stats: CharacterStatsResponseDto;
  category: CharacterCategoriesEnum;
  characterType: CharacterTypeEnum;
  name: string;
  description: string;
  discovered: boolean;
  campaignId: number;
  rarity: number;
  tier: number;
  goldReward: number;
  expReward: number;
};

type CharacterStatsResponseDto = {
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
};

type CharacterCategoriesEnum =
  | "HUMANOID"
  | "BEAST"
  | "MONSTER"
  | "CONSTRUCT"
  | "SPIRIT"
  | "UNDEAD"
  | "ELEMENTAL"
  | "MYTHIC"
  | "CELESTIAL"
  | "FIEND"
  | "ABERRATION"
  | "OTHER";

type CharacterTypeEnum = "PLAYER" | "NPC" | "SNAPSHOT";

export type HeroResponseDto = {
  name: string;
  stats: CharacterStatsResponseDto;
  inventory: CharacterInventoryResponseDto;
};

type CharacterInventoryResponseDto = {
  armors: ArmorInstanceResponseDto[];
  boots: BootsInstanceResponseDto[];
  consumables: ConsumableInstanceResponseDto[];
  helmets: HelmetInstanceResponseDto[];
  shields: ShieldInstanceResponseDto[];
  spells: SpellInstanceResponseDto[];
  weapons: WeaponInstanceResponseDto[];
};

type RequirementResponseDto = {
  requirements: RequirementEntryResponseDto[];
};

type RequirementEntryResponseDto = {
  requirementType: RequirementEntryTypeEnum;
  operator: RequirementEntryOperatorEnum;
  value: string;
};

type ConsumableInstanceResponseDto = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: RequirementResponseDto;
  category: ConsumablesCategoriesEnum;
  restoreHp: number;
  imgBase64: string;
  restoreMp: number;
};

type SpellInstanceResponseDto = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: RequirementResponseDto;
  category: SpellCategoriesEnum;
  restoreHp: number;
  imgBase64: string;
  physicalDamage: number;
  magicalDamage: number;
};

type WeaponInstanceResponseDto = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: RequirementResponseDto;
  category: WeaponCategoriesEnum;
  imgBase64: string;
  physicalDamage: number;
  magicalDamage: number;
};

type ShieldInstanceResponseDto = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: RequirementResponseDto;
  category: ShieldCategoriesEnum;
  physicalDefense: number;
  imgBase64: string;
  magicalDefense: number;
};

type BootsInstanceResponseDto = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: RequirementResponseDto;
  category: BootsCategoriesEnum;
  physicalDefense: number;
  imgBase64: string;
  magicalDefense: number;
};

type HelmetInstanceResponseDto = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: RequirementResponseDto;
  category: HelmetCategoriesEnum;
  imgBase64: string;
  physicalDefense: number;
  magicalDefense: number;
};

type ArmorInstanceResponseDto = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: RequirementResponseDto;
  category: ArmorCategoriesEnum;
  physicalDefense: number;
  magicalDefense: number;
  imgBase64: string;
};

type RequirementEntryOperatorEnum =
  | "MORETHAN"
  | "LESSTHAN"
  | "MOREOREQUALTHAN"
  | "LESSOREQUALTHAN"
  | "EQUALTHAN"
  | "IN"
  | "NOT_IN"
  | "EXISTS"
  | "NOT_EXISTS";

type RequirementEntryTypeEnum =
  | "LEVEL"
  | "HP"
  | "MP"
  | "HEIGHT"
  | "WEIGHT"
  | "CONSTITUTION"
  | "INTELLIGENCE"
  | "STRENGTH"
  | "SPEED"
  | "LUCK"
  | "LOCALVICTORIES"
  | "ONLINEVICTORIES"
  | "TOTALVICTORIES"
  | "ITEMID"
  | "GOLD";
