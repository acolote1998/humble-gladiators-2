import { useAuth } from "@clerk/clerk-react";
import { useQuery } from "@tanstack/react-query";
import { fetchBattleCreationPossibility } from "../api/fetchAvailabilities";
import { useMutation } from "@tanstack/react-query";
import {
  castActionInBattle,
  createABattleForTodayForCampaignAndUser,
  getBattleForTodayForCampaignAndUser,
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
        queryKey: ["active-battle", data.campaignId],
      });
    },
  });
  return mutation;
};

export const useGetBattleForTodayByCampaignIdAndUsery = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError, refetch } = useQuery({
    queryKey: ["active-battle", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return getBattleForTodayForCampaignAndUser(bearerToken, campaignId);
    },
    enabled: false,
  });
  return { data, isError, isLoading, refetch };
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
