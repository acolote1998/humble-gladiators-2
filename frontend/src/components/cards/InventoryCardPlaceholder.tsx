import { useNavigate, useRouterState } from "@tanstack/react-router";
import { cardSizeClass } from "./util/CardSizes";

type InventoryCardPlaceholderType = {
  type: ItemType;
};
type ItemType = "ARMOR" | "HELMET" | "SHIELD" | "BOOTS" | "WEAPON";
const InventoryCardPlaceholder = ({ type }: InventoryCardPlaceholderType) => {
  const navigate = useNavigate();
  const { location } = useRouterState();
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
      className={`opacity-50 scale-90 ${cardSizeClass}`}
      style={{
        backgroundImage: `url('/templates/${typeToImagePath()}.png')`,
      }}
      onClick={() => {
        navigate({ to: `${location.pathname}#card-displayer` });
      }}
    />
  );
};

export default InventoryCardPlaceholder;
