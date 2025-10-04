export type BootsTemplateType = {
  name: string;
  description: string;
  rarity: number;
  tier: number;
  value: number;
  discovered: boolean;
  equipped: boolean;
  campaignId: number;
  category: BootsCategoriesEnum;
  physicalDefense: number;
  magicalDefense: number;
};

type BootsCategoriesEnum =
  | "BOOTS"
  | "COMBAT_BOOTS"
  | "SNEAKERS"
  | "LOAFERS"
  | "SLIPPERS"
  | "SANDALS"
  | "HEELS"
  | "MOCCASINS"
  | "CLOGS"
  | "PLATFORMS"
  | "OTHER";
