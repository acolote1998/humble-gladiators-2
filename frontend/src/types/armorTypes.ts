import type { RenderingFrom } from "./characterTypes";
export type ArmorType = {
  id: number;
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
  imgBase64: string;
  renderingFrom: RenderingFrom;
  quantity?: number;
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
