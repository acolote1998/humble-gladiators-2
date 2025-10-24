import { useAuth } from "@clerk/clerk-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import type { EquipItemType } from "../types/equipItemsTypes";
import { unequipItemsRequest } from "../api/equipItems";
import { equipItemsRequest } from "../api/equipItems";

export const useEquipItems = () => {
  const queryClient = useQueryClient();
  const { getToken } = useAuth();
  const mutation = useMutation({
    mutationFn: async (itemToEquipRequest: EquipItemType) => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return equipItemsRequest(itemToEquipRequest, bearerToken);
    },
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: ["hero-character", data.campaignId],
      });
    },
  });
  return mutation;
};

export const useUnequipItems = () => {
  const queryClient = useQueryClient();
  const { getToken } = useAuth();
  const mutation = useMutation({
    mutationFn: async (itemToUnequipRequest: EquipItemType) => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return unequipItemsRequest(itemToUnequipRequest, bearerToken);
    },
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: ["hero-character", data.campaignId],
      });
    },
  });
  return mutation;
};
