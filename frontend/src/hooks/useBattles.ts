import { useAuth } from "@clerk/clerk-react";
import { useQuery } from "@tanstack/react-query";
import { fetchBattleCreationPossibility } from "../api/fetchAvailabilities";
import { useMutation } from "@tanstack/react-query";
import {
  castActionInBattle,
  createABattleForTodayForCampaignAndUser,
  fetchBattleForTodayForCampaignAndUser,
  fetchIsBattleOngoingForCampaignAndUser,
  fetchLostBattlesForHeroForCampaignAndUser,
  fetchRewardsForFinishedBattleOfTodayForCampaignAndUser,
  fetchWonBattlesForHeroForCampaignAndUser,
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
        queryKey: ["todays-battle", data.campaignId],
      });
      queryClient.invalidateQueries({
        queryKey: ["battle-ongoing-check", data.campaignId],
      });
      //We wait a bit for the battle UI to render, and then we scroll for it to be in the top of the screen
      setTimeout(() => {
        const battleDiv = document.getElementById("battle-start");
        if (battleDiv) {
          battleDiv.scrollIntoView({ behavior: "smooth", block: "start" });
        }
      }, 500);
    },
  });
  return mutation;
};

export const useGetBattleForTodayByCampaignIdAndUsery = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["todays-battle", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchBattleForTodayForCampaignAndUser(bearerToken, campaignId);
    },
    // This makes the battle creation faster, but by not retrying, we would potentially never load a valid battle
    // that just happened to have a fetch error. Review?
    retry: false,
  });
  return { data, isError, isLoading };
};

export const useGetWonBattlesForHeroForCampaignIdAndUsery = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["won-battles", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchWonBattlesForHeroForCampaignAndUser(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};

export const useGetLostBattlesForHeroForCampaignIdAndUsery = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["lost-battles", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchLostBattlesForHeroForCampaignAndUser(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};

export const useGetRewardsForFinishedBattleOfTodayByCampaignIdAndUsery = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["rewards-finished-battle", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchRewardsForFinishedBattleOfTodayForCampaignAndUser(
        bearerToken,
        campaignId
      );
    },
  });
  return { data, isError, isLoading };
};

export const useGetIsBattleOngoing = (campaignId: number) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["battle-ongoing-check", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchIsBattleOngoingForCampaignAndUser(bearerToken, campaignId);
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
