import { createCampaignPost } from "../api/createCampaign";
import { useAuth } from "@clerk/clerk-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import type { CreateCampaignType } from "../types/campaignTypes";
import { fetchCampaignCreationState } from "../api/fetchGameCreationState";
import {
  fetchAllChampaignsForAUser,
  fetchCampaignByIdForAUser,
} from "../api/fetchCampaigns";
export const useCreateCampaign = () => {
  const { getToken } = useAuth();
  const mutation = useMutation({
    mutationFn: async (campaignToCreate: CreateCampaignType) => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return createCampaignPost(campaignToCreate, bearerToken);
    },
  });
  return mutation;
};

export const useGetCreationCampaignState = () => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["gameCreationState"],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchCampaignCreationState(bearerToken);
    },
  });
  return { data, isError, isLoading };
};

export const useGetAllCampaignsForAUser = () => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["campaigns"],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchAllChampaignsForAUser(bearerToken);
    },
  });
  return { data, isError, isLoading };
};

export const useGetCampaignByIdForAUser = (campaignId: number) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["campaign", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchCampaignByIdForAUser(campaignId, bearerToken);
    },
  });
  return { data, isError, isLoading };
};
