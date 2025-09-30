import { useEffect, useState } from "react";
import { useCreateCampaign } from "../../hooks/useCampaigns";
import { useGetCreationCampaignState } from "../../hooks/useCampaigns";
import { useQueryClient } from "@tanstack/react-query";
import CreationProgressBar from "./CreationProgressBar";
const CreateCampaign = () => {
  const { data: campaignCreationState } = useGetCreationCampaignState();
  const { mutate: createCampaign } = useCreateCampaign();
  const [campaignName, setCampaignName] = useState<string>("");
  const [wantedThemes, setWantedThemes] = useState<string[]>([]);
  const [unwantedThemes, setUnwantedThemes] = useState<string[]>([]);
  const [isCreatingCampaign, setIsCreatingCampaign] = useState<boolean>(false);
  const queryClient = useQueryClient();

  useEffect(() => {
    if (!isCreatingCampaign) return;

    const interval = setInterval(() => {
      queryClient.invalidateQueries({ queryKey: ["gameCreationState"] });
    }, 300);

    // Stop polling once the game is created
    if (campaignCreationState === "GAME_CREATED") {
      setIsCreatingCampaign(false);
      clearInterval(interval);
    }

    return () => clearInterval(interval);
  }, [isCreatingCampaign, campaignCreationState, queryClient]);

  const getThemesFromInput = (themesWithComma: string): string[] => {
    const themes = themesWithComma.split(",");
    return themes;
  };

  return (
    <div
      className="
    bg-[var(--div-bg)]
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
      text-2xl text-[var(--text-for-bg)]"
      >
        New Campaign
      </h2>
      <div className="flex">
        <h3 className="text-lg text-[var(--text-for-bg)]">Campaign name:</h3>
        <input
          onChange={(e) => {
            setCampaignName(e.target.value);
          }}
          className="bg-[var(--div-lighter-bg)] rounded-md border-1 mx-2"
          type="text"
        />
      </div>
      <div className="flex">
        <h3 className="text-lg text-[var(--text-for-bg)]">Wanted themes:</h3>
        <input
          onChange={(e) => {
            setWantedThemes(getThemesFromInput(e.target.value));
          }}
          className="bg-[var(--div-lighter-bg)] rounded-md border-1 mx-2"
          type="text"
        />
      </div>
      <div className="flex">
        <h3 className="text-lg text-[var(--text-for-bg)]">Unwanted themes:</h3>
        <input
          onChange={(e) => {
            setUnwantedThemes(getThemesFromInput(e.target.value));
          }}
          className="bg-[var(--div-lighter-bg)] rounded-md border-1 mx-2"
          type="text"
        />
      </div>
      <p
        className="bg-[var(--div-lighter-bg)] p-2 rounded-md"
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
        campaignCreationState && (
          <CreationProgressBar creationState={campaignCreationState} />
        )}
    </div>
  );
};

export default CreateCampaign;
