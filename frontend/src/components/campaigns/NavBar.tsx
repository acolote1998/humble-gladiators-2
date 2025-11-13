import { useNavigate } from "@tanstack/react-router";
import { useRouterState } from "@tanstack/react-router";
import { useGetHeroExistence } from "../../hooks/useCharacters";
import { CompendiumIcon } from "../icons/navbar/CompendiumIcon";
import { ItemBoosterIcon } from "../icons/navbar/ItemBoosterIcon";
import { EnemyBoosterIcon } from "../icons/navbar/EnemyBoosterIcon";
import { InventoryIcon } from "../icons/navbar/InventoryIcon";
import { BattleIcon } from "../icons/navbar/BattleIcon";
import { CampaignIcon } from "../icons/navbar/CampaignIcon";
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
        border-[var(--page-container-bg-darker)] 
        bg-[var(--page-container-bg-darker)]
          m-1
          grid
          grid-cols-6
          gap-0.5
          justify-evenly
        "
    >
      <button
        data-testid="navbar-campaign"
        className="
            bg-[var(--page-container-border)]
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
            "
        onClick={() => {
          navigate({ to: `/campaign/${campaignId}#campaign` });
        }}
      >
        <div className="flex items-center justify-center gap-4">
          <p className="hidden xl:block">Campaign</p>{" "}
          <CampaignIcon width={38} />
        </div>
      </button>
      <button
        data-testid="navbar-compendium"
        className="
            bg-[var(--page-container-border)]
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
            "
        onClick={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}#campaign` });
          } else navigate({ to: `/campaign/${campaignId}/compendium` });
        }}
      >
        <div className="flex items-center justify-center gap-4">
          <p className="hidden xl:block">Compendium</p>{" "}
          <CompendiumIcon width={38} />
        </div>
      </button>
      <button
        data-testid="navbar-item-boosters"
        className="
            bg-[var(--page-container-border)]
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
            "
        onClick={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}#campaign` });
          } else navigate({ to: `/campaign/${campaignId}/boosters/item` });
        }}
      >
        <div className="flex items-center justify-center gap-4">
          <p className="hidden xl:block">Item Boosters</p>
          <ItemBoosterIcon width={38} />
        </div>
      </button>
      <button
        data-testid="navbar-character-boosters"
        className="
            bg-[var(--page-container-border)]
            p-4
            w-full
            h-16
            text-[var(--light-text)]
            rounded-b-xl
            text-xs 2xl:text-xl
            font-semibold
            hover:font-bold
            hover:bg-[var(--page-container-bg-darkest)]
            hover:text-[var(--action-positive-bg)]
            transition-all
            duration-200
            cursor-pointer
            "
        onClick={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}#campaign` });
          } else navigate({ to: `/campaign/${campaignId}/boosters/character` });
        }}
      >
        <div className="flex items-center justify-center gap-4">
          <p className="hidden xl:block">Character Boosters</p>
          <EnemyBoosterIcon width={38} />
        </div>
      </button>
      <button
        data-testid="navbar-inventory"
        className="
            bg-[var(--page-container-border)]
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
            "
        onClick={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}#campaign` });
          } else
            navigate({
              to: `/campaign/${campaignId}/inventory#inventory-start`,
            });
        }}
      >
        <div className="flex items-center justify-center gap-4">
          <p className="hidden xl:block">Inventory</p>
          <InventoryIcon width={38} />
        </div>
      </button>
      <button
        data-testid="navbar-battles"
        className="
            bg-[var(--page-container-border)]
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
            "
        onClick={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}#campaign` });
          } else
            navigate({ to: `/campaign/${campaignId}/battle#battle-start` });
        }}
      >
        <div className="flex items-center justify-center gap-4">
          <p className="hidden xl:block">Battles</p>
          <BattleIcon width={38} />
        </div>
      </button>
    </div>
  );
};

export default NavBar;
