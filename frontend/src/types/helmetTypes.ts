export type HelmetTemplateType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  discovered: boolean;
  equipped: boolean;
  campaignId: number;
  category: HelmetCategoriesEnum;
  physicalDefense: number;
  magicalDefense: number;
};

type HelmetCategoriesEnum =
  | "HELMET"
  | "HARD_HAT"
  | "MILITARY_HELMET"
  | "VISOR"
  | "DECORATIVE_HELMET"
  | "MASK"
  | "HAT"
  | "HOOD"
  | "OTHER";
