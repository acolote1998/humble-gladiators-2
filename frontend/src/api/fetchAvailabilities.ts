import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";

export const fetchCharacterBoosterAvailability = async (
  bearerToken: string,
  campaignId: number
): Promise<boolean> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/character-booster/check-if-available`,
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
