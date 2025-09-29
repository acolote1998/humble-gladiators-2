import axios, { HttpStatusCode } from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { CreateCampaignType } from "../types/campaignTypes";

export const createCampaign = async (
  campaignToCreate: CreateCampaignType,
  bearerToken: string
) => {
  const response = await axios.post(BACKEND_URL, campaignToCreate, {
    headers: { Authorization: `Bearer ${bearerToken}` },
  });
  if (response.status === HttpStatusCode.Ok) {
    return response.data;
  }
  throw new Error("Could not create campaign");
};
