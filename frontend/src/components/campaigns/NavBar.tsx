import { useNavigate } from "@tanstack/react-router";
import { useRouterState } from "@tanstack/react-router";
import { useGetHeroExistence } from "../../hooks/useCharacters";
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
            text-lg
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
        Compendium
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
            text-lg
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
        Item Boosters
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
            text-lg
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
        Character Boosters
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
            text-lg
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
        Inventory
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
            text-lg
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
        Battles
      </button>
    </div>
  );
};

export default NavBar;
