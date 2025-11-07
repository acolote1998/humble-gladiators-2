import { useEffect, useState } from "react";
import { useCreateCampaign } from "../../hooks/useCampaigns";
import { useGetCreationCampaignState } from "../../hooks/useCampaigns";
import CreationProgressBar from "./CreationProgressBar";
import { PageContainer } from "../ui/PageContainer";
const CreateCampaign = () => {
  const { data: campaignCreationState } = useGetCreationCampaignState();
  const { mutate: createCampaign, isPending: isCreatePending } =
    useCreateCampaign();
  const [campaignName, setCampaignName] = useState<string>("");
  const [wantedThemesInput, setWantedThemesInput] = useState<string>("");
  const [unwantedThemesInput, setUnwantedThemesInput] = useState<string>("");
  const [isCreatingCampaign, setIsCreatingCampaign] = useState<boolean>(false);

  useEffect(() => {
    if (!campaignCreationState) return;
    const isActive =
      campaignCreationState !== "CAMPAIGN_NOT_FOUND" &&
      campaignCreationState !== "GAME_CREATED";
    setIsCreatingCampaign(isActive);
  }, [campaignCreationState]);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!campaignName.trim()) return;

    const formatThemes = (themes: string) =>
      themes
        .split(",")
        .map((theme) => theme.trim())
        .filter((theme) => theme.length > 0);

    setIsCreatingCampaign(true);
    createCampaign({
      campaignName: campaignName.trim(),
      theme: {
        wantedThemes: formatThemes(wantedThemesInput),
        unwantedThemes: formatThemes(unwantedThemesInput),
      },
    });
  };

  return (
    <PageContainer>
      <div className="flex flex-col items-center gap-8 justify-center">
        <form
          className="w-full max-w-3xl bg-(--page-container-bg-darker) border border-(--page-container-border) rounded-2xl shadow-md px-10 py-8 flex flex-col gap-6"
          onSubmit={handleSubmit}
        >
          <div className="text-center">
            <h2 className="text-3xl font-semibold text-(--dark-text)">
              Launch a New Campaign
            </h2>
            <p className="text-sm text-(--dark-text) opacity-70 mt-2">
              Give your world a name and curate its themes. Separate multiple
              themes with commas.
            </p>
          </div>

          <label className="flex flex-col gap-2 text-(--dark-text)">
            <span className="text-lg font-semibold">Campaign Name</span>
            <input
              value={campaignName}
              onChange={(e) => setCampaignName(e.target.value)}
              disabled={isCreatingCampaign || isCreatePending}
              placeholder="E.g. Tales of the Sapphire Isles"
              className="bg-(--page-container-bg) border border-(--page-container-border) rounded-lg px-4 py-2 text-(--dark-text) focus:outline-none focus:ring-2 focus:ring-emerald-300 transition-shadow disabled:opacity-70"
              type="text"
              required
            />
          </label>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <label className="flex flex-col gap-2 text-(--dark-text)">
              <span className="text-lg font-semibold">Wanted Themes</span>
              <input
                value={wantedThemesInput}
                onChange={(e) => setWantedThemesInput(e.target.value)}
                disabled={isCreatingCampaign || isCreatePending}
                placeholder="mystic forest, clockwork cities"
                className="bg-(--page-container-bg) border border-(--page-container-border) rounded-lg px-4 py-2 text-(--dark-text) focus:outline-none focus:ring-2 focus:ring-emerald-300 transition-shadow disabled:opacity-70"
                type="text"
              />
              <span className="text-xs opacity-70">
                These themes will be favored when generating content.
              </span>
            </label>

            <label className="flex flex-col gap-2 text-(--dark-text)">
              <span className="text-lg font-semibold">Unwanted Themes</span>
              <input
                value={unwantedThemesInput}
                onChange={(e) => setUnwantedThemesInput(e.target.value)}
                disabled={isCreatingCampaign || isCreatePending}
                placeholder="space opera, high technology"
                className="bg-(--page-container-bg) border border-(--page-container-border) rounded-lg px-4 py-2 text-(--dark-text) focus:outline-none focus:ring-2 focus:ring-emerald-300 transition-shadow disabled:opacity-70"
                type="text"
              />
              <span className="text-xs opacity-70">
                Avoid these tones to keep your campaign focused.
              </span>
            </label>
          </div>

          <button
            type="submit"
            disabled={isCreatingCampaign || isCreatePending}
            className={`px-6 py-3 text-lg font-semibold rounded-md border border-(--page-container-bg-darkerer) bg-(--page-container-border) text-(--light-text) transition-all ease-in-out duration-300 ${
              isCreatingCampaign || isCreatePending
                ? "opacity-70 cursor-progress"
                : "hover:text-(--dark-text) hover:bg-[var(--creation-color)]  hover:scale-105 cursor-pointer"
            }`}
          >
            {isCreatingCampaign || isCreatePending
              ? "Creating..."
              : "Create Campaign"}
          </button>
        </form>
        {campaignCreationState !== "CAMPAIGN_NOT_FOUND" &&
          campaignCreationState && (
            <div className="w-full max-w-3xl">
              <CreationProgressBar creationState={campaignCreationState} />
            </div>
          )}
      </div>
    </PageContainer>
  );
};

export default CreateCampaign;
