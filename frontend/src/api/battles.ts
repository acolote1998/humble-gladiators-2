import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { BattleResponseDto } from "../types/battleTypes";

export const createABattleForTodayForCampaignAndUser = async (
  bearerToken: string,
  campaignId: number
): Promise<BattleResponseDto> => {
  try {
    const response = await axios.post(
      `${BACKEND_URL}/campaign/${campaignId}/character-instances/hero`,
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
