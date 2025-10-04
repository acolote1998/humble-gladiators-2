import { useAuth } from "@clerk/clerk-react";
import { useQuery } from "@tanstack/react-query";
import { fetchAllConsumableTemplatesForCampaignByUser } from "../api/fetchConsumables";

export const useGetAllConsumableTemplatesForCampaignByUser = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["all-consumable-templates", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchAllConsumableTemplatesForCampaignByUser(
        bearerToken,
        campaignId
      );
    },
  });
  return { data, isError, isLoading };
};
