export type ShieldTemplateType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  discovered: boolean;
  equipped: boolean;
  campaignId: number;
  category: ShieldCategoriesEnum;
  physicalDefense: number;
  imgBase64: string;
  magicalDefense: number;
};

export type ShieldCategoriesEnum =
  | "SHIELD"
  | "BOOK"
  | "AMULET"
  | "RING"
  | "BADGE"
  | "TROPHY"
  | "TOOL"
  | "MISCELLANEOUS"
  | "BAG"
  | "STATIONERY"
  | "SPELLBOOK"
  | "OTHER";
