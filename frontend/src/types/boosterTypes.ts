import type { ArmorTemplateType } from "./armorTypes";
import type { BootsTemplateType } from "./bootsTypes";
import type { ConsumableTemplateType } from "./consumablesTypes";
import type { HelmetTemplateType } from "./helmetTypes";
import type { ShieldTemplateType } from "./shieldTypes";
import type { SpellTemplateType } from "./spellTypes";
import type { WeaponTemplateType } from "./weaponTypes";
export type ItemBoosterType = {
  armors: ArmorTemplateType[];
  boots: BootsTemplateType[];
  consumables: ConsumableTemplateType[];
  helmets: HelmetTemplateType[];
  shields: ShieldTemplateType[];
  spells: SpellTemplateType[];
  weapons: WeaponTemplateType[];
};
