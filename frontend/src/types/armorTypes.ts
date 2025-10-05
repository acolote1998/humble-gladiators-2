export type ArmorTemplateType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  discovered: boolean;
  equipped: boolean;
  campaignId: number;
  category: ArmorCategoriesEnum;
  physicalDefense: number;
  magicalDefense: number;
};

export type ArmorCategoriesEnum =
  | "ROBE"
  | "PLATE"
  | "MAIL"
  | "SHIRT"
  | "CAPE"
  | "CLOAK"
  | "BACKPACK"
  | "OTHER";
