import type { CampaignDto } from "../../types/campaignTypes";
const CampaignItem = ({ id, name, theme }: CampaignDto) => {
  return (
    <div className="flex flex-col">
      Campaign:
      <p>Id: {id}</p>
      <p>Name: {name}</p>
      <p>Theme:</p>
      Wanted Themes:
      <ul>
        {theme.wantedThemes.map((theme) => {
          return <li>{theme}</li>;
        })}
      </ul>
      Unwanted Themes:
      <ul>
        {theme.unwantedThemes.map((theme) => {
          return <li>{theme}</li>;
        })}
      </ul>
    </div>
  );
};

export default CampaignItem;
