import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { CampaignDto } from "../types/campaignTypes";

export const fetchAllChampaignsForAUser = async (
  bearerToken: string
): Promise<CampaignDto[]> => {
  try {
    const response = await axios.get(`${BACKEND_URL}/campaigns`, {
      headers: { Authorization: `Bearer ${bearerToken}` },
    });
    return response.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export const fetchCampaignByIdForAUser = async (
  campaignId: number,
  bearerToken: string
): Promise<CampaignDto> => {
  try {
    const response = await axios.get(`${BACKEND_URL}/campaigns/${campaignId}`, {
      headers: { Authorization: `Bearer ${bearerToken}` },
    });
    return response.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};
