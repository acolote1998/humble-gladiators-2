import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { ArmorTemplateType } from "../types/armorTypes";
export const fetchAllArmorTemplatesForCampaignByUser = async (
  bearerToken: string,
  campaignId: number
): Promise<ArmorTemplateType[]> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/armor-templates`,
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
