import axios from "axios";
import { BACKEND_URL } from "../util/urls";
import type { BootsType } from "../types/bootsTypes";

export const fetchAllBootsTemplatesForCampaignByUser = async (
  bearerToken: string,
  campaignId: number
): Promise<BootsType[]> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/boots-templates`,
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
