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

export const fetchItemBoosterAvailability = async (
  bearerToken: string,
  campaignId: number
): Promise<boolean> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/items-booster/check-if-available`,
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

export const fetchHeroExistence = async (
  bearerToken: string,
  campaignId: number
): Promise<boolean> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/character-instances/hero/check-if-exists`,
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

export const fetchBattleCreationPossibility = async (
  bearerToken: string,
  campaignId: number
): Promise<boolean> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/battle/check-availability`,
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
