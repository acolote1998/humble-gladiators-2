import { useAuth } from "@clerk/clerk-react";
import { useQuery } from "@tanstack/react-query";
import { fetchBattleCreationPossibility } from "../api/fetchAvailabilities";

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
