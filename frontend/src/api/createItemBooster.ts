import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { ItemBoosterType } from "../types/boosterTypes";

export const createItemBooster = async (
  bearerToken: string,
  campaignId: number
): Promise<ItemBoosterType> => {
  try {
    const response = await axios.post(
      `${BACKEND_URL}/campaign/${campaignId}/items-booster`,
      {},
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
