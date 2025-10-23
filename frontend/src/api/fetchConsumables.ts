import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { ConsumableType } from "../types/consumablesTypes";

export const fetchAllConsumableTemplatesForCampaignByUser = async (
  bearerToken: string,
  campaignId: number
): Promise<ConsumableType[]> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/consumable-templates`,
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
