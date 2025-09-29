import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { CreateCampaignType } from "../types/campaignTypes";

export const createCampaign = async (
  campaignToCreate: CreateCampaignType,
  bearerToken: string
) => {
  try {
    const response = await axios.post(
      `${BACKEND_URL}/game/campaign`,
      campaignToCreate,
      {
        headers: { Authorization: `Bearer ${bearerToken}` },
      }
    );
    return response.data;
  } catch (error) {
    console.log(error);
  }
};
