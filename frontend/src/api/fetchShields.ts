import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { ShieldTemplateType } from "../types/shieldTypes";

export const fetchAllShieldTemplatesForCampaignByUser = async (
  bearerToken: string,
  campaignId: number
): Promise<ShieldTemplateType[]> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/shield-templates`,
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
