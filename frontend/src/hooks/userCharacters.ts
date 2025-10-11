import { useAuth } from "@clerk/clerk-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { fetchAllCharactersForCampaignByUser } from "../api/fetchCharacters";
import { fetchHeroForCampaignByUser } from "../api/fetchCharacters";
import type { CreateHeroType } from "../types/characterTypes";
import { createHeroForACampaignPost } from "../api/createHero";
import { useNavigate } from "@tanstack/react-router";

export const useGetCharactersByCampaignAndUser = (campaignId: number) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["all-character-instances", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchAllCharactersForCampaignByUser(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};

export const useGetHeroByCampaignAndUser = (campaignId: number) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["hero-character", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchHeroForCampaignByUser(bearerToken, campaignId);
    },
    retry: false,
  });
  return { data, isLoading, isError };
};

export const useCreateHero = () => {
  const navigate = useNavigate();
  const { getToken } = useAuth();
  const mutation = useMutation({
    mutationFn: async (heroToCreateVariables: CreateHeroType) => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return createHeroForACampaignPost(heroToCreateVariables, bearerToken);
    },
    onSuccess: () => {
      navigate({ reloadDocument: true });
    },
  });
  return mutation;
};
