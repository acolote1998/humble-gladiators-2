import type { ArmorCategoriesEnum } from "./armorTypes";
import type { BootsCategoriesEnum } from "./bootsTypes";
import type { HelmetCategoriesEnum } from "./helmetTypes";
import type { ShieldCategoriesEnum } from "./shieldTypes";
import type { ConsumablesCategoriesEnum } from "./consumablesTypes";
import type { SpellCategoriesEnum } from "./spellTypes";
import type { WeaponCategoriesEnum } from "./weaponTypes";

export type CharacterInstanceType = {
  stats: CharacterStats;
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

type CharacterStats = {
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

export type HeroCharacterType = {
  name: string;
  stats: CharacterStats;
  inventory: HeroInventoryType;
};

type HeroInventoryType = {
  armors: HeroArmorType[];
  boots: HeroBootsType[];
  consumables: HeroConsumablesType[];
  helmets: HeroHelmetsType[];
  shields: HeroShieldsType[];
  spells: HeroSpellsType[];
  weapons: HeroWeaponsType[];
};

type HeroRequirementType = {
  requirements: HeroRequirementEntryType[];
};

type HeroRequirementEntryType = {
  requirementType: RequirementEntryTypeEnum;
  operator: RequirementEntryOperatorEnum;
  value: string;
};

type HeroConsumablesType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: HeroRequirementType;
  category: ConsumablesCategoriesEnum;
  restoreHp: number;
  restoreMp: number;
};

type HeroSpellsType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: HeroRequirementType;
  category: SpellCategoriesEnum;
  restoreHp: number;
  physicalDamage: number;
  magicalDamage: number;
};

type HeroWeaponsType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: HeroRequirementType;
  category: WeaponCategoriesEnum;
  physicalDamage: number;
  magicalDamage: number;
};

type HeroShieldsType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: HeroRequirementType;
  category: ShieldCategoriesEnum;
  physicalDefense: number;
  magicalDefense: number;
};

type HeroBootsType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: HeroRequirementType;
  category: BootsCategoriesEnum;
  physicalDefense: number;
  magicalDefense: number;
};

type HeroHelmetsType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: HeroRequirementType;
  category: HelmetCategoriesEnum;
  physicalDefense: number;
  magicalDefense: number;
};

type HeroArmorType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  quantity: number;
  equipped: boolean;
  requirement: HeroRequirementType;
  category: ArmorCategoriesEnum;
  physicalDefense: number;
  magicalDefense: number;
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
