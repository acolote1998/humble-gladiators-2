import type { ArmorType } from "./armorTypes";
import type { BootsType } from "./bootsTypes";
import type { ConsumableType } from "./consumablesTypes";
import type { HelmetType } from "./helmetTypes";
import type { ShieldType } from "./shieldTypes";
import type { SpellType } from "./spellTypes";

export type CreateHeroType = {
  heroName: string;
  campaignId: number;
};

export type CharacterInstanceType = {
  id: number;
  stats: CharacterStatsResponseDto;
  category: CharacterCategoriesEnum;
  characterType: CharacterTypeEnum;
  inventory: CharacterInventoryResponseDto;
  name: string;
  description: string;
  discovered: boolean;
  campaignId: number;
  rarity: number;
  tier: number;
  goldReward: number;
  expReward: number;
  imgBase64: string;
  renderingFrom: RenderingFrom;
};

export type RenderingFrom = "COMPENDIUM" | "BOOSTER" | "BATTLE";

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
  physicalDefense: number;
  magicalDefense: number;
  physicalDamage: number;
  magicalDamage: number;
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
  id: number;
  name: string;
  stats: CharacterStatsResponseDto;
  inventory: CharacterInventoryResponseDto;
};

type CharacterInventoryResponseDto = {
  armors: ArmorType[];
  boots: BootsType[];
  consumables: ConsumableType[];
  helmets: HelmetType[];
  shields: ShieldType[];
  spells: SpellType[];
  weapons: WeaponInstanceResponseDto[];
};

export type RequirementResponseDto = {
  requirements: RequirementEntryResponseDto[];
};

type RequirementEntryResponseDto = {
  requirementType: RequirementEntryTypeEnum;
  operator: RequirementEntryOperatorEnum;
  value: string;
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
