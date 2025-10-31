import { useNavigate } from "@tanstack/react-router";
import { useRouterState } from "@tanstack/react-router";
import { useGetHeroExistence } from "../../hooks/useCharacters";
import { CompendiumIcon } from "../icons/CompendiumIcon";
import { ItemBoosterIcon } from "../icons/ItemBoosterIcon";
import { EnemyBoosterIcon } from "../icons/EnemyBoosterIcon";
import { InventoryIcon } from "../icons/InventoryIcon";
import { BattleIcon } from "../icons/BattleIcon";
const NavBar = () => {
  const navigate = useNavigate();
  const { location } = useRouterState();
  const segments = location.pathname.split("/");
  const campaignIndex = segments.indexOf("campaign");
  const campaignId = campaignIndex !== -1 ? segments[campaignIndex + 1] : null;
  const { data: isHeroExisting } = useGetHeroExistence(Number(campaignId));
  return (
    <div
      className="mx-5
          rounded-b-2xl
          border-b-3
          border-x-3
        border-gray-300 
        bg-gray-300
          m-1
          grid
          grid-cols-5
          gap-0.5
          justify-evenly
        "
    >
      <button
        data-testid="navbar-compendium"
        className="
            bg-gray-400
            p-4
            w-full
            h-16
            text-white
            rounded-b-xl
            text-2xl
            font-semibold
            hover:text-xl
            hover:font-bold
            hover:bg-gray-700
            hover:text-green-200
            transition-all
            duration-200
            cursor-pointer
            "
        onClick={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}` });
          } else navigate({ to: `/campaign/${campaignId}/compendium` });
        }}
      >
        <div className="flex items-center justify-center gap-4">
          <p>Compendium</p> <CompendiumIcon width={38} />
        </div>
      </button>
      <button
        data-testid="navbar-item-boosters"
        className="
            bg-gray-400
            p-4
            w-full
            h-16
            text-white
            rounded-b-xl
            text-2xl
            font-semibold
            hover:text-xl
            hover:font-bold
            hover:bg-gray-700
            hover:text-green-200
            transition-all
            duration-200
            cursor-pointer
            "
        onClick={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}` });
          } else navigate({ to: `/campaign/${campaignId}/boosters/item` });
        }}
      >
        <div className="flex items-center justify-center gap-4">
          <p>Item Boosters</p>
          <ItemBoosterIcon width={38} />
        </div>
      </button>
      <button
        data-testid="navbar-character-boosters"
        className="
            bg-gray-400
            p-4
            w-full
            h-16
            text-white
            rounded-b-xl
            text-2xl
            font-semibold
            hover:text-xl
            hover:font-bold
            hover:bg-gray-700
            hover:text-green-200
            transition-all
            duration-200
            cursor-pointer
            "
        onClick={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}` });
          } else navigate({ to: `/campaign/${campaignId}/boosters/character` });
        }}
      >
        <div className="flex items-center justify-center gap-4">
          <p>Character Boosters</p>
          <EnemyBoosterIcon width={38} />
        </div>
      </button>
      <button
        data-testid="navbar-inventory"
        className="
            bg-gray-400
            p-4
            w-full
            h-16
            text-white
            rounded-b-xl
            text-2xl
            font-semibold
            hover:text-xl
            hover:font-bold
            hover:bg-gray-700
            hover:text-green-200
            transition-all
            duration-200
            cursor-pointer
            "
        onClick={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}` });
          } else
            navigate({
              to: `/campaign/${campaignId}/inventory#inventory-start`,
            });
        }}
      >
        <div className="flex items-center justify-center gap-4">
          <p>Inventory</p>
          <InventoryIcon width={38} />
        </div>
      </button>
      <button
        data-testid="navbar-battles"
        className="
            bg-gray-400
            p-4
            w-full
            h-16
            text-white
            rounded-b-xl
            text-2xl
            font-semibold
            hover:text-xl
            hover:font-bold
            hover:bg-gray-700
            hover:text-green-200
            transition-all
            duration-200
            cursor-pointer
            "
        onClick={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}` });
          } else
            navigate({ to: `/campaign/${campaignId}/battle#battle-start` });
        }}
      >
        <div className="flex items-center justify-center gap-4">
          <p>Battles</p>
          <BattleIcon width={38} />
        </div>
      </button>
    </div>
  );
};

export default NavBar;
