import { useAuth } from "@clerk/clerk-react";
import { useQuery } from "@tanstack/react-query";
import { fetchAllShieldTemplatesForCampaignByUser } from "../api/fetchShields";

export const useGetAllShieldTemplatesForCampaignByUser = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["all-shield-templates", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchAllShieldTemplatesForCampaignByUser(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};
