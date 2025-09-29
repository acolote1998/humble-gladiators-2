export type CreateCampaignType = {
  campaignName: string;
  theme: ThemeForCampaignType;
};

type ThemeForCampaignType = {
  wantedThemes: string[];
  unwantedThemes: string[];
};
