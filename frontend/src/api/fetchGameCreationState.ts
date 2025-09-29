import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { CampaignCreationStateType } from "../types/campaignTypes";

export const fetchCampaignCreationState = async (
  bearerToken: string
): Promise<CampaignCreationStateType> => {
  try {
    const response = await axios.get(`${BACKEND_URL}/game/state`, {
      headers: { Authorization: `Bearer ${bearerToken}` },
    });
    return response.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};
