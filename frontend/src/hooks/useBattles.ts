import { useAuth } from "@clerk/clerk-react";
import { useQuery } from "@tanstack/react-query";
import { fetchBattleCreationPossibility } from "../api/fetchAvailabilities";
import { useMutation } from "@tanstack/react-query";
import {
  castActionInBattle,
  createABattleForTodayForCampaignAndUser,
  fetchFinishedBattleForTodayForCampaignAndUser,
  fetchOngoingBattleForTodayForCampaignAndUser,
  getCheckIfThereIsAnOngoingBattleForToday,
  triggerNpcTurnForTodaysBattle,
} from "../api/battles";
import { useQueryClient } from "@tanstack/react-query";
import type { TurnRequest } from "../types/battleTypes";

export const useGetBattleCreationAvailability = (campaignId: number) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["battle-creation-availability", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchBattleCreationPossibility(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};

export const useCreateABattleForTodayByCampaignIdAndUser = () => {
  const queryClient = useQueryClient();
  const { getToken } = useAuth();
  const mutation = useMutation({
    mutationFn: async (campaignId: number) => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return createABattleForTodayForCampaignAndUser(bearerToken, campaignId);
    },
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: ["battle-ongoing-check", data.campaignId],
      });
      queryClient.invalidateQueries({
        queryKey: ["active-battle", data.campaignId],
      });
      queryClient.invalidateQueries({
        queryKey: ["battle-ongoing-check", data.campaignId],
      });
    },
  });
  return mutation;
};

export const useGetOngoingBattleForTodayByCampaignIdAndUsery = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["active-battle", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchOngoingBattleForTodayForCampaignAndUser(
        bearerToken,
        campaignId
      );
    },
  });
  return { data, isError, isLoading };
};

export const useGetFinishedBattleForTodayByCampaignIdAndUsery = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["finished-battle", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchFinishedBattleForTodayForCampaignAndUser(
        bearerToken,
        campaignId
      );
    },
  });
  return { data, isError, isLoading };
};

export const useGetCheckIfThereIsAnOngoingBattleForToday = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["battle-ongoing-check", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return getCheckIfThereIsAnOngoingBattleForToday(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};

export const useCastActionInBattle = () => {
  const { getToken } = useAuth();
  const mutation = useMutation({
    mutationFn: async (turnRequest: TurnRequest) => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return castActionInBattle(
        bearerToken,
        turnRequest.campaignId,
        turnRequest.battleId,
        turnRequest
      );
    },
  });
  return mutation;
};

export const useTriggerNpcTurnForTodaysBattle = () => {
  const { getToken } = useAuth();
  const mutation = useMutation({
    mutationFn: async (campaignId: number) => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return triggerNpcTurnForTodaysBattle(bearerToken, campaignId);
    },
  });
  return mutation;
};
