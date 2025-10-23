import type { RequirementResponseDto } from "./characterTypes";
import type { RenderingFrom } from "./characterTypes";
export type HelmetType = {
  id: number;
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
  imgBase64: string;
  magicalDefense: number;
  renderingFrom: RenderingFrom;
  quantity?: number;
  requirement?: RequirementResponseDto;
};

export type HelmetCategoriesEnum =
  | "HELMET"
  | "HARD_HAT"
  | "MILITARY_HELMET"
  | "VISOR"
  | "DECORATIVE_HELMET"
  | "MASK"
  | "HAT"
  | "HOOD"
  | "OTHER";
