import axios from "axios";
import { BACKEND_URL } from "../util/urls";
import type { WeaponType } from "../types/weaponTypes";

export const fetchAllWeaponTemplatesForCampaignByUser = async (
  bearerToken: string,
  campaignId: number
): Promise<WeaponType[]> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/weapon-templates`,
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
