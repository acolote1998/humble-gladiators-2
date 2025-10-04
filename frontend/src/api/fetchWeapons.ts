import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { WeaponTemplateType } from "../types/weaponTypes";

export const fetchAllWeaponTemplatesForCampaignByUser = async (
  bearerToken: string,
  campaignId: number
): Promise<WeaponTemplateType[]> => {
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
