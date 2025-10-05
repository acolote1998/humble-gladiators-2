export type ConsumableTemplateType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  discovered: boolean;
  equipped: boolean;
  campaignId: number;
  category: ConsumablesCategoriesEnum;
  restoreHp: number;
  restoreMp: number;
};

export type ConsumablesCategoriesEnum =
  | "FOOD"
  | "DRINK"
  | "MEDICINE"
  | "TREAT"
  | "TOY"
  | "TOOL"
  | "GADGET"
  | "BOOK"
  | "DOCUMENT"
  | "ACCESSORY"
  | "MISCELLANEOUS"
  | "OTHER";
