import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";
import type { EquipItemType } from "../types/equipItemsTypes";

export const equipItemsRequest = async (
  itemToEquipRequest: EquipItemType,
  bearerToken: string
) => {
  try {
    const response = await axios.patch(
      `${BACKEND_URL}/campaign/${itemToEquipRequest.campaignId}/character-instances/hero/equip/${itemToEquipRequest.typeItemToEquip}/${itemToEquipRequest.itemId}`,
      {},
      {
        headers: { Authorization: `Bearer ${bearerToken}` },
      }
    );
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

export const unequipItemsRequest = async (
  itemToUnequipRequest: EquipItemType,
  bearerToken: string
) => {
  try {
    const response = await axios.patch(
      `${BACKEND_URL}/campaign/${itemToUnequipRequest.campaignId}/character-instances/hero/unquip/${itemToUnequipRequest.typeItemToEquip}`,
      {},
      {
        headers: { Authorization: `Bearer ${bearerToken}` },
      }
    );
    return response.data;
  } catch (error) {
    console.log(error);
  }
};
