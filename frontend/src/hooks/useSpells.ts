import { useAuth } from "@clerk/clerk-react";
import { useQuery } from "@tanstack/react-query";
import { fetchAllSpellTemplatesForCampaignByUser } from "../api/fetchSpells";

export const useGetAllSpellTemplatesForCampaignByUser = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["all-spell-templates", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchAllSpellTemplatesForCampaignByUser(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};
