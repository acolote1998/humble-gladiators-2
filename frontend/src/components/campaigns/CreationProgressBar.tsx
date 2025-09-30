import type { CampaignCreationStateType } from "../../types/campaignTypes";

type ProgressBarPercentType = {
  creationState: CampaignCreationStateType | undefined;
};

type PercentAndStateFormat = {
  state: string;
  percent: string;
};
const CreationProgressBar = ({ creationState }: ProgressBarPercentType) => {
  const getPercentWidth = (percentToFormat: number) => {
    return `${percentToFormat}%`;
  };

  const getPercentAndStateFormat = (): PercentAndStateFormat => {
    let formattedState: string;
    let formattedPercent: string;
    switch (creationState) {
      case "CAMPAIGN_NOT_FOUND":
        formattedState = "Campaign not found";
        formattedPercent = getPercentWidth(0);
        break;
      case "CREATING_THEMES":
        formattedState = "Starting new campaign...";
        formattedPercent = getPercentWidth(0);
        break;
      case "THEMES_CREATED":
        formattedState = "Creating themes...";
        formattedPercent = getPercentWidth(3);
        break;
      case "CREATING_CAMPAIGN":
        formattedState = "Creating campaign...";
        formattedPercent = getPercentWidth(6);
        break;
      case "CAMPAIGN_CREATED":
        formattedState = "Campaign created";
        formattedPercent = getPercentWidth(10);
        break;
      case "CREATING_ARMORS":
        formattedState = "Creating armors...";
        formattedPercent = getPercentWidth(20);
        break;
      case "ARMORS_CREATED":
        formattedState = "Armors created";
        formattedPercent = getPercentWidth(25);
        break;
      case "CREATING_BOOTS":
        formattedState = "Creating boots...";
        formattedPercent = getPercentWidth(30);
        break;
      case "BOOTS_CREATED":
        formattedState = "Boots created";
        formattedPercent = getPercentWidth(35);
        break;
      case "CREATING_CONSUMABLES":
        formattedState = "Creating consumables...";
        formattedPercent = getPercentWidth(40);
        break;
      case "CONSUMABLES_CREATED":
        formattedState = "Consumables created";
        formattedPercent = getPercentWidth(45);
        break;
      case "CREATING_HELMETS":
        formattedState = "Creating helmets...";
        formattedPercent = getPercentWidth(50);
        break;
      case "HELMETS_CREATED":
        formattedState = "Helmets created";
        formattedPercent = getPercentWidth(55);
        break;
      case "CREATING_SHIELDS":
        formattedState = "Creating shields...";
        formattedPercent = getPercentWidth(60);
        break;
      case "SHIELDS_CREATED":
        formattedState = "Shields created";
        formattedPercent = getPercentWidth(65);
        break;
      case "CREATING_SPELLS":
        formattedState = "Creating spells...";
        formattedPercent = getPercentWidth(70);
        break;
      case "SPELLS_CREATED":
        formattedState = "Spells created";
        formattedPercent = getPercentWidth(75);
        break;
      case "CREATING_WEAPONS":
        formattedState = "Creating weapons...";
        formattedPercent = getPercentWidth(80);
        break;
      case "WEAPONS_CREATED":
        formattedState = "Weapons created";
        formattedPercent = getPercentWidth(85);
        break;
      case "CREATING_NPCS_PHASE_ONE":
        formattedState = "Creating NPCs...";
        formattedPercent = getPercentWidth(90);
        break;
      case "CREATING_NPCS_PHASE_TWO":
        formattedState = "Creating NPCs...";
        formattedPercent = getPercentWidth(92);
        break;
      case "CREATING_NPCS_PHASE_THREE":
        formattedState = "Creating NPCs...";
        formattedPercent = getPercentWidth(94);
        break;
      case "CREATING_NPCS_PHASE_FOUR":
        formattedState = "Creating NPCs...";
        formattedPercent = getPercentWidth(96);
        break;
      case "CREATING_NPCS_PHASE_FIVE":
        formattedState = "Creating NPCs...";
        formattedPercent = getPercentWidth(98);
        break;
      case "NPCS_CREATED":
        formattedState = "NPCs created";
        formattedPercent = getPercentWidth(100);
        break;
      case "GAME_CREATED":
        formattedState = "Game created!";
        formattedPercent = getPercentWidth(100);
        break;
      default:
        formattedState = "Unknown state";
        formattedPercent = getPercentWidth(0);
    }

    return { state: formattedState, percent: formattedPercent };
  };

  return (
    <div className="w-full h-6 bg-gray-200 rounded-full dark:bg-gray-700">
      <div
        className="transition-width ease-in-out duration-1000 h-6 bg-blue-600 rounded-full dark:bg-blue-500 flex items-center justify-center"
        style={{ width: getPercentAndStateFormat().percent }}
      >
        <p className="text-white text-sm font-medium">
          {getPercentAndStateFormat().state}
        </p>
      </div>
    </div>
  );
};

export default CreationProgressBar;
