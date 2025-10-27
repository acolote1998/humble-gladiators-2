import type { ArmorType } from "./armorTypes";
import type { BootsType } from "./bootsTypes";
import type { ConsumableType } from "./consumablesTypes";
import type { HelmetType } from "./helmetTypes";
import type { ShieldType } from "./shieldTypes";
import type { SpellType } from "./spellTypes";
import type { WeaponType } from "./weaponTypes";

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

export type RenderingFrom = "COMPENDIUM" | "BOOSTER" | "BATTLE" | "INVENTORY";

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

type CharacterInventoryResponseDto = {
  armors: ArmorType[];
  boots: BootsType[];
  consumables: ConsumableType[];
  helmets: HelmetType[];
  shields: ShieldType[];
  spells: SpellType[];
  weapons: WeaponType[];
};

export type RequirementResponseDto = {
  requirements: RequirementEntryResponseDto[];
};

type RequirementEntryResponseDto = {
  requirementType: RequirementEntryTypeEnum;
  operator: RequirementEntryOperatorEnum;
  value: string;
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
