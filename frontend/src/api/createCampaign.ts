import axios from "axios";
import { BACKEND_URL } from "../util/urls";
import type { CreateCampaignType } from "../types/campaignTypes";

export const createCampaignPost = async (
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
    return response;
  } catch (error) {
    console.log(error);
  }
};
