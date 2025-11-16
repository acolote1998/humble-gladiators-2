import { BattleIcon } from "../icons/navbar/BattleIcon";
import { CampaignIcon } from "../icons/navbar/CampaignIcon";
import { CompendiumIcon } from "../icons/navbar/CompendiumIcon";
import { EnemyBoosterIcon } from "../icons/navbar/EnemyBoosterIcon";
import { InventoryIcon } from "../icons/navbar/InventoryIcon";
import { ItemBoosterIcon } from "../icons/navbar/ItemBoosterIcon";

type NavBarItemTYpe = {
  name: NameType;
  onClickItem: () => void;
  userIsHere: boolean;
};
type NameType =
  | "Campaign"
  | "Compendium"
  | "Item Boosters"
  | "Character Boosters"
  | "Inventory"
  | "Battles";
const NavBarItem = ({ name, onClickItem, userIsHere }: NavBarItemTYpe) => {
  const getItemIcon = (itemName: string) => {
    switch (itemName) {
      case "Campaign":
        return <CampaignIcon width={38} />;
      case "Compendium":
        return <CompendiumIcon width={38} />;
      case "Item Boosters":
        return <ItemBoosterIcon width={38} />;
      case "Character Boosters":
        return <EnemyBoosterIcon width={38} />;
      case "Inventory":
        return <InventoryIcon width={38} />;
      case "Battles":
        return <BattleIcon width={38} />;
    }
  };
  return (
    <button
      data-testid={`navbar-${name.replace(" ", "-").toLowerCase()}`}
      className={`
            ${userIsHere ? "bg-[var(--page-container-bg-darkest)]" : "bg-[var(--page-container-border)]"}
            p-4
            w-full
            h-16
            text-[var(--light-text)]
            rounded-b-xl
            text-lg 2xl:text-2xl
            font-semibold
            hover:text-xl
            hover:font-bold
            hover:bg-[var(--page-container-bg-darkest)]
            hover:text-[var(--action-positive-bg)]
            transition-all
            duration-200
            cursor-pointer
            `}
      onClick={() => {
        onClickItem();
      }}
    >
      <div className="flex items-center justify-center gap-4">
        <p className="hidden xl:block">{name}</p> {getItemIcon(name)}
      </div>
    </button>
  );
};

export default NavBarItem;
