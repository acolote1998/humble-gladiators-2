import { useAuth } from "@clerk/clerk-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import {
  createCharacterBooster,
  createItemBooster,
} from "../api/createBoosters";
import { fetchItemBoosterAvailability } from "../api/fetchAvailabilities";
import { fetchCharacterBoosterAvailability } from "../api/fetchAvailabilities";

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

export const useGetItemBoosterAvailability = (campaignId: number) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["items-booster-availability", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchItemBoosterAvailability(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};

export const useGetCharacterBoosterAvailability = (campaignId: number) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["character-booster-availability", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchCharacterBoosterAvailability(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};
