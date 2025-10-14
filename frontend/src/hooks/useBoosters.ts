import { useAuth } from "@clerk/clerk-react";
import { useMutation } from "@tanstack/react-query";
import {
  createCharacterBooster,
  createItemBooster,
} from "../api/createBoosters";

export const useCreateItemBooster = () => {
  const { getToken } = useAuth();
  const mutation = useMutation({
    mutationFn: async (campaignId: number) => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return createItemBooster(bearerToken, campaignId);
    },
  });
  return mutation;
};

export const useCreateCharacterBooster = () => {
  const { getToken } = useAuth();
  const mutation = useMutation({
    mutationFn: async (campaignId: number) => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return createCharacterBooster(bearerToken, campaignId);
    },
  });
  return mutation;
};
