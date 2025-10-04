import { useAuth } from "@clerk/clerk-react";
import { useQuery } from "@tanstack/react-query";
import { fetchAllHelmetTemplatesForCampaignByUser } from "../api/fetchHelmets";

export const useGetAllHelmetTemplatesForCampaignByUser = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["all-helmet-templates", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchAllHelmetTemplatesForCampaignByUser(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};
