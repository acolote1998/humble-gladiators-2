export type CreateCampaignType = {
  campaignName: string;
  theme: ThemeForCampaignType;
};

type ThemeForCampaignType = {
  wantedThemes: string[];
  unwantedThemes: string[];
};

export type CampaignCreationStateType =
  | "CAMPAIGN_NOT_FOUND"
  | "STARTING_NEW_CAMPAIGN"
  | "CREATING_THEMES"
  | "THEMES_CREATED"
  | "CREATING_CAMPAIGN"
  | "CAMPAIGN_CREATED"
  | "CREATING_ARMORS"
  | "ARMORS_CREATED"
  | "CREATING_BOOTS"
  | "BOOTS_CREATED"
  | "CREATING_CONSUMABLES"
  | "CONSUMABLES_CREATED"
  | "CREATING_HELMETS"
  | "HELMETS_CREATED"
  | "CREATING_SHIELDS"
  | "SHIELDS_CREATED"
  | "CREATING_SPELLS"
  | "SPELLS_CREATED"
  | "CREATING_WEAPONS"
  | "WEAPONS_CREATED"
  | "CREATING_NPCS_PHASE_ONE"
  | "CREATING_NPCS_PHASE_TWO"
  | "CREATING_NPCS_PHASE_THREE"
  | "CREATING_NPCS_PHASE_FOUR"
  | "CREATING_NPCS_PHASE_FIVE"
  | "NPCS_CREATED"
  | "CREATING_CAMPAIGN_COVER_IMAGE"
  | "CAMPAIGN_COVER_IMAGE_CREATED"
  | "CREATING_CAMPAIGN_CARD_BACK_IMAGE"
  | "CAMPAIGN_CARD_BACK_IMAGE_CREATED"
  | "GAME_CREATED";

export type CampaignDto = {
  id: number;
  name: string;
  theme: ThemeForCampaignType;
  campaignCreationState: CampaignCreationStateType;
  coverImgBase64: string;
  cardBackImgBase64: string;
};

export type CardBackDto = {
  cardBackImgBase64: string;
};
