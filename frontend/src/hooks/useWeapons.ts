import { useAuth } from "@clerk/clerk-react";
import { useQuery } from "@tanstack/react-query";
import { fetchAllWeaponTemplatesForCampaignByUser } from "../api/fetchWeapons";

export const useGetAllWeaponTemplatesForCampaignByUser = (
  campaignId: number
) => {
  const { getToken } = useAuth();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["all-weapon-templates", campaignId],
    queryFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return fetchAllWeaponTemplatesForCampaignByUser(bearerToken, campaignId);
    },
  });
  return { data, isError, isLoading };
};
