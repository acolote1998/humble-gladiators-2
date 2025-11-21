import { UnequipItemIcon } from "@/components/icons/ui/UnequipItemIcon";
type UneuipItemType = {
  unequipItem: () => void;
};
const UnequipItemComponent = ({ unequipItem }: UneuipItemType) => {
  return (
    <div
      onClick={() => {
        unequipItem();
      }}
      className="opacity-100 xl:hover:opacity-100 xl:opacity-0 flex justify-center gap-2 cursor-pointer transition-all duration-250"
      data-testid="unequip-item-inventory"
    >
      <p className="text-[var(--action-positive-bg)] font-semibold tracking-wide">
        Unequip
      </p>
      <UnequipItemIcon width={18} />
    </div>
  );
};

export default UnequipItemComponent;
