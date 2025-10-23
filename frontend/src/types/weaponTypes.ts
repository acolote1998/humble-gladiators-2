import type { RequirementResponseDto } from "./characterTypes";
import type { RenderingFrom } from "./characterTypes";

export type WeaponType = {
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
  renderingFrom: RenderingFrom;
  quantity?: number;
  requirement?: RequirementResponseDto;
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
