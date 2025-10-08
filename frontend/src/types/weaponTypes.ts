export type WeaponTemplateType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  discovered: boolean;
  equipped: boolean;
  campaignId: number;
  category: WeaponCategoriesEnum;
  physicalDamage: number;
  imgBase64: string;
  magicalDamage: number;
};

export type WeaponCategoriesEnum =
  | "SWORD"
  | "AXE"
  | "MACE"
  | "DAGGER"
  | "SPEAR"
  | "STAFF"
  | "CLUB"
  | "HAMMER"
  | "BOW"
  | "GUN"
  | "WAND"
  | "MAGIC_ORB"
  | "ENCHANTED_GLOVE"
  | "TOOL"
  | "WHIP"
  | "THROWABLE"
  | "FOOD"
  | "OTHER";
