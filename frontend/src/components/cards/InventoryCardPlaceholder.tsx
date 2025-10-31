type InventoryCardPlaceholderType = {
  type: ItemType;
};
type ItemType = "ARMOR" | "HELMET" | "SHIELD" | "BOOTS" | "WEAPON";
const InventoryCardPlaceholder = ({ type }: InventoryCardPlaceholderType) => {
  const typeToImagePath = () => {
    switch (type) {
      case "ARMOR":
        return "armor_placeholder";
      case "BOOTS":
        return "boots_placeholder";
      case "HELMET":
        return "helmet_placeholder";
      case "SHIELD":
        return "shield_placeholder";
      case "WEAPON":
        return "weapon_placeholder";
    }
  };
  return (
    <div
      data-testid="item-placeholder"
      className={`opacity-50 scale-90 relative my-5 w-85 h-119 bg-cover bg-no-repeat p-2 select-none cursor-pointer`}
      style={{
        backgroundImage: `url('/templates/${typeToImagePath()}.png')`,
      }}
    />
  );
};

export default InventoryCardPlaceholder;
