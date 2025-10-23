type TypeItemToEquip = "armor" | "boots" | "helmet" | "shield" | "weapon";

export type EquipItemType = {
  campaignId: number;
  itemId: number;
  typeItemToEquip: TypeItemToEquip;
};
