import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";

type TypeItemToEquip = "armor" | "boots" | "helmet" | "shield" | "weapon";
type EquipItemType = {
  campaignId: number;
  itemId: number;
  typeItemToEquip: TypeItemToEquip;
};

export const equipItems = async (
  itemToEquipRequest: EquipItemType,
  bearerToken: string
) => {
  try {
    const response = await axios.post(
      `${BACKEND_URL}
      /campaign
      /${itemToEquipRequest.campaignId}
      /character-instances
      /hero
      /equip
      /${itemToEquipRequest.typeItemToEquip}
      /${itemToEquipRequest.itemId}`,
      {
        headers: { Authorization: `Bearer ${bearerToken}` },
      }
    );
    return response.data;
  } catch (error) {
    console.log(error);
  }
};
