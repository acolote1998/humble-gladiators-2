import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { CharacterInstanceType } from "../types/characterTypes";
export const fetchAllCharactersForCampaignByUser = async (
  bearerToken: string,
  campaignId: number
): Promise<CharacterInstanceType[]> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/character-instances`,
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
