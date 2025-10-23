import type { RequirementResponseDto } from "./characterTypes";
import type { RenderingFrom } from "./characterTypes";

export type ConsumableType = {
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
  imgBase64: string;
  renderingFrom: RenderingFrom;
  quantity?: number;
  requirement?: RequirementResponseDto;
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
