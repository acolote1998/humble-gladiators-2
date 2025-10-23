import type { RequirementResponseDto } from "./characterTypes";
import type { RenderingFrom } from "./characterTypes";

export type BootsType = {
  id: number;
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
  imgBase64: string;
  magicalDefense: number;
  renderingFrom: RenderingFrom;
  quantity?: number;
  requirement?: RequirementResponseDto;
};

export type BootsCategoriesEnum =
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
