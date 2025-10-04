import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { SpellTemplateType } from "../types/spellTypes";

export const fetchAllSpellTemplatesForCampaignByUser = async (
  bearerToken: string,
  campaignId: number
): Promise<SpellTemplateType[]> => {
  try {
    const response = await axios.get(
      `${BACKEND_URL}/campaign/${campaignId}/spell-templates`,
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
