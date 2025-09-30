import type { CampaignCreationStateType } from "../../types/campaignTypes";

type ProgressBarPercent = {
  percent: number;
  creationState: CampaignCreationStateType;
};

const CreationProgressBar = ({
  percent,
  creationState,
}: ProgressBarPercent) => {
  const getPercentWidth = () => {
    return `${percent}%`;
  };

  const getFormattedState = () => {
    let formattedState: string;

    switch (creationState) {
      case "CAMPAIGN_NOT_FOUND":
        formattedState = "Campaign not found";
        break;
      case "STARTING_NEW_CAMPAIGN":
        formattedState = "Starting new campaign...";
        break;
      case "CREATING_THEMES":
        formattedState = "Creating themes...";
        break;
      case "THEMES_CREATED":
        formattedState = "Themes created";
        break;
      case "CREATING_CAMPAIGN":
        formattedState = "Creating campaign...";
        break;
      case "CAMPAIGN_CREATED":
        formattedState = "Campaign created";
        break;
      case "CREATING_ARMORS":
        formattedState = "Creating armors...";
        break;
      case "ARMORS_CREATED":
        formattedState = "Armors created";
        break;
      case "CREATING_BOOTS":
        formattedState = "Creating boots...";
        break;
      case "BOOTS_CREATED":
        formattedState = "Boots created";
        break;
      case "CREATING_CONSUMABLES":
        formattedState = "Creating consumables...";
        break;
      case "CONSUMABLES_CREATED":
        formattedState = "Consumables created";
        break;
      case "CREATING_HELMETS":
        formattedState = "Creating helmets...";
        break;
      case "HELMETS_CREATED":
        formattedState = "Helmets created";
        break;
      case "CREATING_SHIELDS":
        formattedState = "Creating shields...";
        break;
      case "SHIELDS_CREATED":
        formattedState = "Shields created";
        break;
      case "CREATING_SPELLS":
        formattedState = "Creating spells...";
        break;
      case "SPELLS_CREATED":
        formattedState = "Spells created";
        break;
      case "CREATING_WEAPONS":
        formattedState = "Creating weapons...";
        break;
      case "WEAPONS_CREATED":
        formattedState = "Weapons created";
        break;
      case "CREATING_NPCS":
        formattedState = "Creating NPCs...";
        break;
      case "NPCS_CREATED":
        formattedState = "NPCs created";
        break;
      case "GAME_CREATED":
        formattedState = "Game created!";
        break;
      default:
        formattedState = "Unknown state";
    }

    return formattedState;
  };

  return (
    <div className="w-full h-6 bg-gray-200 rounded-full dark:bg-gray-700">
      <div
        className="h-6 bg-blue-600 rounded-full dark:bg-blue-500 flex items-center justify-center"
        style={{ width: getPercentWidth() }}
      >
        <p className="text-white text-sm font-medium">{getFormattedState()}</p>
      </div>
    </div>
  );
};

export default CreationProgressBar;
