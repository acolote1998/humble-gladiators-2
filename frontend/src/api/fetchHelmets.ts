import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { HelmetTemplateType } from "../types/helmetTypes";

export const fetchAllHelmetTemplatesForCampaignByUser = async (
  bearerToken: string,
  campaignId: number
): Promise<HelmetTemplateType[]> => {
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
