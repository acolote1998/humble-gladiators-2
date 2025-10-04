export type CharacterInstanceType = {
  stats: CharacterStats;
  category: CharacterCategoriesEnum;
  characterType: CharacterTypeEnum;
  name: string;
  description: string;
  discovered: boolean;
  campaignId: number;
  rarity: number;
  tier: number;
  goldReward: number;
  expReward: number;
};

type CharacterStats = {
  constitution: number;
  intelligence: number;
  strength: number;
  speed: number;
  luck: number;
  maxHp: number;
  currentHp: number;
  maxMp: number;
  currentMp: number;
  height: number;
  weight: number;
  level: number;
  currentExp: number;
  expForNextLevel: number;
};

type CharacterCategoriesEnum =
  | "HUMANOID"
  | "BEAST"
  | "MONSTER"
  | "CONSTRUCT"
  | "SPIRIT"
  | "UNDEAD"
  | "ELEMENTAL"
  | "MYTHIC"
  | "CELESTIAL"
  | "FIEND"
  | "ABERRATION"
  | "OTHER";

type CharacterTypeEnum = "PLAYER" | "NPC" | "SNAPSHOT";
