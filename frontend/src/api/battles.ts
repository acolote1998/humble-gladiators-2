import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { BattleResponseDto } from "../types/battleTypes";

export const createABattleForTodayForCampaignAndUser = async (
  bearerToken: string,
  campaignId: number
): Promise<BattleResponseDto> => {
  try {
    const response = await axios.post(
      `${BACKEND_URL}/campaign/${campaignId}/battle/new`,
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

export const getBattleForTodayForCampaignAndUser = async (
  bearerToken: string,
  campaignId: number
): Promise<BattleResponseDto> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/battle`,
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
