import { useAuth } from "@clerk/clerk-react";
import { useMutation } from "@tanstack/react-query";
import { createItemBooster } from "../api/createItemBooster";
export const useCreateCampaign = () => {
  const { getToken } = useAuth();
  const mutation = useMutation({
    mutationFn: async () => {
      const bearerToken = await getToken();
      if (!bearerToken) {
        throw new Error("No bearer token available");
      }
      return createItemBooster(bearerToken);
    },
  });
  return mutation.data;
};
