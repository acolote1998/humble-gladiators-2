import type { CampaignDto } from "../../types/campaignTypes";
const CampaignItem = ({
  id,
  name,
  theme,
  campaignCreationState,
}: CampaignDto) => {
  return (
    <div className="flex flex-col">
      Campaign:
      <p>Id: {id}</p>
      <p>State: {campaignCreationState}</p>
      <p>Name: {name}</p>
      <p>Theme:</p>
      Wanted Themes:
      <ul>
        {theme.wantedThemes.map((theme, index) => {
          return <li key={index}>{theme}</li>;
        })}
      </ul>
      Unwanted Themes:
      <ul>
        {theme.unwantedThemes.map((theme, index) => {
          return <li key={index}>{theme}</li>;
        })}
      </ul>
    </div>
  );
};

export default CampaignItem;
