import { useAuth } from "@clerk/clerk-react";
import { useQuery } from "@tanstack/react-query";
import { fetchAllBootsTemplatesForCampaignByUser } from "../api/fetchBoots";

export const useGetAllBootsTemplatesForCampaignByUser = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["all-boots-templates", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchAllBootsTemplatesForCampaignByUser(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};
