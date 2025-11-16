import { EquipItemIcon } from "@/components/icons/ui/EquipItemIcon";
type EquipItemType = {
  equipItem: () => void;
};
export const EquipItemComponent = ({ equipItem }: EquipItemType) => {
  return (
    <div
      onClick={() => {
        equipItem();
      }}
      className="opacity-100 xl:hover:opacity-100 xl:opacity-0 flex justify-center gap-2 cursor-pointer transition-all duration-250"
      data-testid="equip-item-inventory"
    >
      <p className="text-[var(--action-positive-bg)] font-semibold tracking-wide">
        Equip
      </p>
      <EquipItemIcon width={18} />
    </div>
  );
};
