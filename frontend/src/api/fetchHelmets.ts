import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { HelmetType } from "../types/helmetTypes";

export const fetchAllHelmetTemplatesForCampaignByUser = async (
  bearerToken: string,
  campaignId: number
): Promise<HelmetType[]> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/helmet-templates`,
      {
        headers: { Authorization: `Bearer ${bearerToken}` },
      }
    );
    return response.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};
