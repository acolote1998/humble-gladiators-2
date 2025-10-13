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
        formattedState = "Starting new campaign";
        formattedPercent = getPercentWidth(2);
        break;
      case "THEMES_CREATED":
        formattedState = "Creating themes";
        formattedPercent = getPercentWidth(4);
        break;
      case "CREATING_CAMPAIGN":
        formattedState = "Creating campaign";
        formattedPercent = getPercentWidth(6);
        break;
      case "CAMPAIGN_CREATED":
        formattedState = "Campaign created";
        formattedPercent = getPercentWidth(8);
        break;
      case "CREATING_ARMORS":
        formattedState = "Creating armors";
        formattedPercent = getPercentWidth(12);
        break;
      case "ARMORS_CREATED":
        formattedState = "Armors created";
        formattedPercent = getPercentWidth(16);
        break;
      case "CREATING_BOOTS":
        formattedState = "Creating boots";
        formattedPercent = getPercentWidth(20);
        break;
      case "BOOTS_CREATED":
        formattedState = "Boots created";
        formattedPercent = getPercentWidth(24);
        break;
      case "CREATING_CONSUMABLES":
        formattedState = "Creating consumables";
        formattedPercent = getPercentWidth(28);
        break;
      case "CONSUMABLES_CREATED":
        formattedState = "Consumables created";
        formattedPercent = getPercentWidth(32);
        break;
      case "CREATING_HELMETS":
        formattedState = "Creating helmets";
        formattedPercent = getPercentWidth(36);
        break;
      case "HELMETS_CREATED":
        formattedState = "Helmets created";
        formattedPercent = getPercentWidth(40);
        break;
      case "CREATING_SHIELDS":
        formattedState = "Creating shields";
        formattedPercent = getPercentWidth(44);
        break;
      case "SHIELDS_CREATED":
        formattedState = "Shields created";
        formattedPercent = getPercentWidth(48);
        break;
      case "CREATING_SPELLS":
        formattedState = "Creating spells";
        formattedPercent = getPercentWidth(52);
        break;
      case "SPELLS_CREATED":
        formattedState = "Spells created";
        formattedPercent = getPercentWidth(56);
        break;
      case "CREATING_WEAPONS":
        formattedState = "Creating weapons";
        formattedPercent = getPercentWidth(60);
        break;
      case "WEAPONS_CREATED":
        formattedState = "Weapons created";
        formattedPercent = getPercentWidth(64);
        break;
      case "CREATING_NPCS_PHASE_ONE":
        formattedState = "Creating NPCs";
        formattedPercent = getPercentWidth(68);
        break;
      case "CREATING_NPCS_PHASE_TWO":
        formattedState = "Creating NPCs";
        formattedPercent = getPercentWidth(71);
        break;
      case "CREATING_NPCS_PHASE_THREE":
        formattedState = "Creating NPCs";
        formattedPercent = getPercentWidth(74);
        break;
      case "CREATING_NPCS_PHASE_FOUR":
        formattedState = "Creating NPCs";
        formattedPercent = getPercentWidth(77);
        break;
      case "CREATING_NPCS_PHASE_FIVE":
        formattedState = "Creating NPCs";
        formattedPercent = getPercentWidth(80);
        break;
      case "NPCS_CREATED":
        formattedState = "NPCs created";
        formattedPercent = getPercentWidth(84);
        break;
      case "CREATING_CAMPAIGN_COVER_IMAGE":
        formattedState = "Creating Campaign Cover Image";
        formattedPercent = getPercentWidth(92);
        break;
      case "CAMPAIGN_COVER_IMAGE_CREATED":
        formattedState = "Campaign's cover image created";
        formattedPercent = getPercentWidth(96);
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
    <div className="w-full h-6 bg-gray-200 rounded-full dark:bg-gray-700 relative overflow-hidden">
      <div
        className="transition-width ease-in-out duration-1000 h-6 bg-blue-600 rounded-full dark:bg-blue-500"
        style={{ width: getPercentAndStateFormat().percent }}
      ></div>
      <div className="absolute inset-0 flex items-center justify-center">
        <p className="text-white text-sm font-medium">
          {getPercentAndStateFormat().state}{" "}
        </p>
        <p className="loader"></p>
      </div>
    </div>
  );
};

export default CreationProgressBar;
