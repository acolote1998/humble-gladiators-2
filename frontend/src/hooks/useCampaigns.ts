import { createCampaignPost } from "../api/createCampaign";
import { useAuth } from "@clerk/clerk-react";
import { useMutation } from "@tanstack/react-query";
import type { CreateCampaignType } from "../types/campaignTypes";
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
