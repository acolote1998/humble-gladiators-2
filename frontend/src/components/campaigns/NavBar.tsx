import { useNavigate } from "@tanstack/react-router";
import { useRouterState } from "@tanstack/react-router";
import { useGetHeroExistence } from "../../hooks/useCharacters";
import NavBarItem from "../ui/NavBarItem";
const NavBar = () => {
  const navigate = useNavigate();
  const { location } = useRouterState();
  const segments = location.pathname.split("/");
  const campaignIndex = segments.indexOf("campaign");
  const campaignId = campaignIndex !== -1 ? segments[campaignIndex + 1] : null;
  const { data: isHeroExisting } = useGetHeroExistence(Number(campaignId));

  return (
    <div
      className="
      mx-2 xl:mx-9
          rounded-b-2xl
          border-b-3
          border-x-3
        border-[var(--page-container-bg-darker)] 
        bg-[var(--page-container-bg-darker)]
          
          grid
          grid-cols-6
          gap-0.5
          justify-evenly
        "
    >
      <NavBarItem
        name="Campaign"
        onClickItem={() => {
          navigate({ to: `/campaign/${campaignId}#campaign` });
        }}
        userIsHere={location.pathname === `/campaign/${campaignId}`}
      />
      <NavBarItem
        name="Compendium"
        onClickItem={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}#campaign` });
          } else navigate({ to: `/campaign/${campaignId}/compendium` });
        }}
        userIsHere={location.pathname === `/campaign/${campaignId}/compendium`}
      />
      <NavBarItem
        name="Item Boosters"
        onClickItem={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}#campaign` });
          } else navigate({ to: `/campaign/${campaignId}/boosters/item` });
        }}
        userIsHere={
          location.pathname === `/campaign/${campaignId}/boosters/item`
        }
      />
      <NavBarItem
        name="Character Boosters"
        onClickItem={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}#campaign` });
          } else navigate({ to: `/campaign/${campaignId}/boosters/character` });
        }}
        userIsHere={
          location.pathname === `/campaign/${campaignId}/boosters/character`
        }
      />
      <NavBarItem
        name="Inventory"
        onClickItem={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}#campaign` });
          } else
            navigate({
              to: `/campaign/${campaignId}/inventory#inventory-start`,
            });
        }}
        userIsHere={location.pathname === `/campaign/${campaignId}/inventory`}
      />
      <NavBarItem
        name="Battles"
        onClickItem={() => {
          if (!isHeroExisting) {
            navigate({ to: `/campaign/${campaignId}#campaign` });
          } else
            navigate({ to: `/campaign/${campaignId}/battle#battle-start` });
        }}
        userIsHere={location.pathname === `/campaign/${campaignId}/battle`}
      />
    </div>
  );
};

export default NavBar;
