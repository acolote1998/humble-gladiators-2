import { useEffect, useState } from "react";
import { useCreateCampaign } from "../../hooks/useCampaigns";
import { useGetCreationCampaignState } from "../../hooks/useCampaigns";
import { useQueryClient } from "@tanstack/react-query";
const CreateCampaign = () => {
  const {
    data: campaignCreationState,
    isError: errorGetitngCampaignCreationState,
    isLoading: loadingGettingCampaignCreationState,
  } = useGetCreationCampaignState();
  const { mutate: createCampaign } = useCreateCampaign();
  const [campaignName, setCampaignName] = useState<string>("");
  const [wantedThemes, setWantedThemes] = useState<string[]>([]);
  const [unwantedThemes, setUnwantedThemes] = useState<string[]>([]);
  const [isCreatingCampaign, setIsCreatingCampaign] = useState<boolean>(false);
  const queryClient = useQueryClient();
  useEffect(() => {
    if (isCreatingCampaign && campaignCreationState !== "GAME_CREATED") {
      setTimeout(() => {
        queryClient.invalidateQueries({ queryKey: ["gameCreationState"] });
      }, 300);
    } else {
      if (campaignCreationState === "GAME_CREATED")
        setIsCreatingCampaign(false);
    }
  }, [queryClient, isCreatingCampaign, campaignCreationState]);

  const getThemesFromInput = (themesWithComma: string): string[] => {
    const themes = themesWithComma.split(",");
    return themes;
  };

  return (
    <div
      className="
    bg-gray-300
    m-10
    rounded-md
    p-5
    flex
    flex-col
    text-center
    justify-center
    items-center
    gap-5
    "
    >
      <h2
        className="
      font-semibold
      text-2xl"
      >
        New Campaign
      </h2>
      <div className="flex">
        <h3 className="text-lg">Campaign name:</h3>
        <input
          onChange={(e) => {
            setCampaignName(e.target.value);
          }}
          className="bg-gray-200 rounded-md border-1 mx-2"
          type="text"
        />
      </div>
      <div className="flex">
        <h3 className="text-lg">Wanted themes:</h3>
        <input
          onChange={(e) => {
            setWantedThemes(getThemesFromInput(e.target.value));
          }}
          className="bg-gray-200 rounded-md border-1 mx-2"
          type="text"
        />
      </div>
      <div className="flex">
        <h3 className="text-lg">Unwanted themes:</h3>
        <input
          onChange={(e) => {
            setUnwantedThemes(getThemesFromInput(e.target.value));
          }}
          className="bg-gray-200 rounded-md border-1 mx-2"
          type="text"
        />
      </div>
      <p
        onClick={() => {
          setIsCreatingCampaign(true);
          createCampaign({
            campaignName: campaignName,
            theme: {
              wantedThemes: wantedThemes,
              unwantedThemes: unwantedThemes,
            },
          });
        }}
      >
        Create Campaign
      </p>

      {campaignCreationState !== "CAMPAIGN_NOT_FOUND" &&
        campaignCreationState !== "GAME_CREATED" &&
        campaignCreationState !== undefined && <p>{campaignCreationState}</p>}
    </div>
  );
};

export default CreateCampaign;
