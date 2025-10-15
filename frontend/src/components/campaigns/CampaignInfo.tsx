import type { CampaignDto } from "../../types/campaignTypes";
const CampaignInfo = ({ name, theme }: CampaignDto) => {
  return (
    <div
      className="
      flex
      flex-col
    bg-blue-300
      p-3
      rounded-lg
      border-1
      border-blue-600
      text-lg
      w-fit
    "
    >
      <p
        className="
      bg-blue-400
      p-2
      rounded-md
      "
      >
        {name}
      </p>
      <p>Wanted themes:</p>
      <div className="flex gap-2">
        {theme.wantedThemes.map((theme, index) => {
          return (
            <p
              className="rounded-md bg-gray-200 w-fit p-0.5 font-light"
              key={index}
            >
              {theme}
            </p>
          );
        })}
      </div>
      <p>Unwanted themes:</p>
      <div className="flex gap-2">
        {theme.unwantedThemes.map((theme, index) => {
          return (
            <p
              className="rounded-md bg-gray-200 w-fit p-0.5 font-light"
              key={index}
            >
              {theme}
            </p>
          );
        })}
      </div>
    </div>
  );
};

export default CampaignInfo;
