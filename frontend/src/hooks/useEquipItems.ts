import { useAuth } from "@clerk/clerk-react";
import { useMutation } from "@tanstack/react-query";
import type { EquipItemType } from "../types/equipItemsTypes";
import { equipItemsRequest } from "../api/equipItems";

export const useEquipItems = () => {
  const { getToken } = useAuth();
  const mutation = useMutation({
    mutationFn: async (itemToEquipRequest: EquipItemType) => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return equipItemsRequest(itemToEquipRequest, bearerToken);
    },
  });
  return mutation;
};
