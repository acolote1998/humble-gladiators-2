import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type {
  BattleResponseDto,
  TurnResponseDto,
  TurnRequest,
  BattleRewardsResponseDto,
} from "../types/battleTypes";

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

export const fetchIsBattleOngoingForCampaignAndUser = async (
  bearerToken: string,
  campaignId: number
): Promise<boolean> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/battle/check-ongoing`,
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

export const fetchBattleForTodayForCampaignAndUser = async (
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

export const fetchRewardsForFinishedBattleOfTodayForCampaignAndUser = async (
  bearerToken: string,
  campaignId: number
): Promise<BattleRewardsResponseDto> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/battle/get-rewards`,
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

export const castActionInBattle = async (
  bearerToken: string,
  campaignId: number,
  battleId: number,
  turnRequest: TurnRequest
): Promise<TurnResponseDto> => {
  const getActionForUrl = () => {
    switch (turnRequest.action) {
      case "SPELL":
        return "spell";
      case "PHYSICAL_ATTACK":
        return "attack";
      case "CONSUMABLE":
        return "consumable";
      case "NOTHING":
        return "";
    }
  };
  try {
    const response = await axios.post(
      `${BACKEND_URL}/campaign/${campaignId}/battle/${battleId}/action/${getActionForUrl()}`,
      turnRequest,
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

export const triggerNpcTurnForTodaysBattle = async (
  bearerToken: string,
  campaignId: number
) => {
  try {
    const response = await axios.post(
      `${BACKEND_URL}/campaign/${campaignId}/battle/action/trigger-npc-turn`,
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
