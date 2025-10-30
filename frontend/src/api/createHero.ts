import axios from "axios";
import { BACKEND_URL } from "../util/urls";
import type { CreateHeroType } from "../types/characterTypes";
export const createHeroForACampaignPost = async (
  heroToCreateVariables: CreateHeroType,
  bearerToken: string
) => {
  try {
    const response = await axios.post(
      `${BACKEND_URL}/campaign/${heroToCreateVariables.campaignId}/character-instances/hero`,
      { heroName: heroToCreateVariables.heroName },
      {
        headers: { Authorization: `Bearer ${bearerToken}` },
      }
    );
    return response.data;
  } catch (error) {
    console.log(error);
  }
};
