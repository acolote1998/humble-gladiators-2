export type SpellTemplateType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  discovered: boolean;
  equipped: boolean;
  campaignId: number;
  category: SpellCategoriesEnum;
  physicalDamage: number;
  magicalDamage: number;
  restoreHp: number;
};

type SpellCategoriesEnum =
  | "FIRE_SPELL"
  | "ICE_SPELL"
  | "EARTH_SPELL"
  | "WATER_SPELL"
  | "AIR_SPELL"
  | "LIGHTNING_SPELL"
  | "HEALING_SPELL"
  | "BUFF_SPELL"
  | "DEBUFF_SPELL"
  | "SUMMON_SPELL"
  | "GENERAL_SPELL"
  | "OTHER";
