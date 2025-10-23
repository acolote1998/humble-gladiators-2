import type { ArmorType } from "./armorTypes";
import type { BootsType } from "./bootsTypes";
import type { CharacterInstanceType } from "./characterTypes";
import type { ConsumableType } from "./consumablesTypes";
import type { HelmetType } from "./helmetTypes";
import type { ShieldType } from "./shieldTypes";
import type { SpellType } from "./spellTypes";
import type { WeaponType } from "./weaponTypes";
export type ItemBoosterType = {
  armors: ArmorType[];
  boots: BootsType[];
  consumables: ConsumableType[];
  helmets: HelmetType[];
  shields: ShieldType[];
  spells: SpellType[];
  weapons: WeaponType[];
};

export type ItemBoosterInterface = {
  campaignId: string;
};

export type CharacterBoosterType = {
  characters: CharacterInstanceType[];
};

export type CharacterBoosterInterface = {
  campaignId: string;
};
