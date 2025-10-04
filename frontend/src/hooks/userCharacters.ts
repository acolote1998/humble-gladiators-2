import { useAuth } from "@clerk/clerk-react";
import { useQuery } from "@tanstack/react-query";
import { fetchAllCharactersForCampaignByUser } from "../api/fetchCharacters";
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
