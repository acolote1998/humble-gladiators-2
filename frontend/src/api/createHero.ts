import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { CreateHeroType } from "../types/characterTypes";
export const createHeroForACampaignPost = async (
  heroToCreate: CreateHeroType,
  campaignId: number,
  bearerToken: string
) => {
  try {
    const response = await axios.post(
      `${BACKEND_URL}/campaign/${campaignId}/character-instances/hero`,
      heroToCreate,
      {
        headers: { Authorization: `Bearer ${bearerToken}` },
      }
    );
    return response.data;
  } catch (error) {
    console.log(error);
  }
};
